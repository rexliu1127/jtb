package io.grx.auth.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.grx.common.utils.Constant;
import io.grx.common.utils.HttpContextUtils;
import io.grx.common.utils.HttpUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import io.grx.auth.service.YiXiangService;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.entity.YxReportEntity;
import io.grx.modules.auth.enums.VerifyStatus;
import io.grx.modules.auth.enums.YiXiangType;
import io.grx.modules.auth.service.AuthUserService;
import io.grx.modules.auth.service.YxReportService;

@Controller
public class YiXiangConroller {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private YxReportService yxReportService;

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private YiXiangService yiXiangService;

    @Value("${yixiang.userPrefix}")
    private String userPrefix;

    @Autowired
    private Environment env;

    private Interner<String> pool;

    public YiXiangConroller() {
        pool = Interners.newWeakInterner();
    }

    /**
     * 获取天机h5 url
     */
    @RequestMapping(value = "/auth/yx/h5", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> forwardH5Page(String yiXiangType) {
        return yiXiangService.getH5Url(YiXiangType.valueOf(StringUtils.upperCase(yiXiangType)));
    }

    /**
     * 获取全知taskId
     */
    @RequestMapping(value = "/auth/yx/taskId", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getTaskId(String yiXiangType) {
        return yiXiangService.getTaskId(YiXiangType.valueOf(StringUtils.upperCase(yiXiangType)));
    }

    /**
     * 天机认证return
     *
     * @return
     */
    @RequestMapping(value = "/yx/return/{userId}/{type}/{taskId}", method = RequestMethod.GET)
    public String yxReturn(@PathVariable(value = "type") String type, @PathVariable(value = "userId")  Long userId,
                        @PathVariable(value = "taskId") String taskId, HttpServletRequest request) {
        logger.info("YX return. type: {}, taskId: {}, userId: {}", type, taskId, userId);

        AuthUserEntity user = authUserService.queryObject(userId);
        if (user == null) {
            throw new RuntimeException("Not found userId " + userId);
        }

        synchronized (pool.intern(taskId)) {
            YxReportEntity reportEntity = yxReportService.queryByTaskId(taskId);
            if (reportEntity == null) {
                reportEntity = new YxReportEntity();
                reportEntity.setUserId(userId);
                reportEntity.setIdNo(user.getIdNo());
                reportEntity.setName(user.getName());
                reportEntity.setMobile(user.getMobile());
                reportEntity.setTaskId(taskId);
                reportEntity.setCreateTime(new Date());
                reportEntity.setYiXiangType(YiXiangType.valueOf(StringUtils.upperCase(type)));
                reportEntity.setVerifyStatus(VerifyStatus.SUBMITTED);

                yxReportService.save(reportEntity);
            } else {
                if (reportEntity.getUserId() == null) {
                    reportEntity.setUserId(userId);
                }
                if (StringUtils.isBlank(reportEntity.getIdNo())) {
                    reportEntity.setIdNo(user.getIdNo());
                }
                if (StringUtils.isBlank(reportEntity.getName())) {
                    reportEntity.setName(user.getName());
                }
                if (StringUtils.isBlank(reportEntity.getMobile())) {
                    reportEntity.setMobile(user.getMobile());
                }

                if (reportEntity.getVerifyStatus() == VerifyStatus.NEW) {
                    reportEntity.setVerifyStatus(VerifyStatus.SUBMITTED);
                }
                yxReportService.update(reportEntity);
            }
        }

        if (HttpContextUtils.isMidouAppClient(request)) {
            return "redirect:/return_android.html";
        }
        return "redirect:/auth/apply.html";
    }

    /**
     * 天机认证return
     *
     * @return
     */
    @RequestMapping(value = "/yx/callback")
    public void yxCallback(@RequestBody Map<String, String> params, HttpServletResponse response) throws Exception {
        logger.info("YX callback. params: {}", params);

        String token = MapUtils.getString(params, "token");
        String taskId = MapUtils.getString(params, "taskId");
        String sign = MapUtils.getString(params, "apiEnc");
        String website = MapUtils.getString(params, "apiName");
        boolean success = MapUtils.getBoolean(params, "success", false);

        if (StringUtils.startsWith(taskId, userPrefix + "-")) {
            synchronized (pool.intern(taskId)) {
                YxReportEntity reportEntity = yxReportService.queryByTaskId(taskId);
                if (reportEntity == null) {
                    reportEntity = new YxReportEntity();
                    reportEntity.setTaskId(taskId);
                    reportEntity.setYiXiangType(YiXiangType.valueOf(StringUtils.upperCase(website)));
                    reportEntity.setCreateTime(new Date());
                }

                if (success) {
                    reportEntity.setSearchId(token);
                    reportEntity.setVerifyStatus(VerifyStatus.SUCCESS);
                } else {
                    reportEntity.setVerifyStatus(VerifyStatus.FAILED);
                }

                if (reportEntity.getId() == null) {
                    yxReportService.save(reportEntity);
                } else {
                    yxReportService.update(reportEntity);
                }

                if (reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
                    yiXiangService.asyncDownloadReport(reportEntity);
                }
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("success");
        } else {
            String prefix = StringUtils.contains(taskId, "-") ?
                    StringUtils.substringBefore(taskId, "-") : "CXB";
            String otherHost = env.getProperty(String.format("yixiang.otherHost%1$s", prefix));
            logger.debug("{}={}", String.format("yixiang.otherHost%1$s", prefix), otherHost);
            String otherPath = env.getProperty(String.format("yixiang.otherPath%1$s", prefix));
            logger.debug("{}={}", String.format("yixiang.otherPath%1$s", prefix), otherPath);

            try {
                logger.info("Sending YX callback to {}{}: {}", otherHost, params);
                final String requestJson = new ObjectMapper().writeValueAsString(params);

                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");

                final HttpResponse res = HttpUtils.doPost(otherHost,
                        otherPath + "/yx/callback",
                        headers, MapUtils.EMPTY_MAP, requestJson);
                String responseStr = EntityUtils.toString(res.getEntity(), Constant.ENCODING_UTF8);
                logger.info("send YX callback result: {}", responseStr);

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(responseStr);
                return;

            } catch (Exception e) {
                logger.error("failed to send YX callback", e);
            }

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("error");
        }

    }

}
