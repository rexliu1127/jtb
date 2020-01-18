package io.grx.auth.controller;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.grx.auth.service.ShujumoheDsService;
import io.grx.common.utils.R;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.entity.AuthUserReportDsEntity;
import io.grx.modules.auth.enums.DsType;
import io.grx.modules.auth.enums.VerifyStatus;
import io.grx.modules.auth.service.AuthUserReportDsService;

@RestController
@RequestMapping("/auth/ds")
public class AuthDsController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthUserReportDsService authUserReportDsService;

    @Autowired
    private ShujumoheDsService shujumoheDsService;

    /**
     * 初始化验证
     */
    @RequestMapping(value = "/init_verify", method = RequestMethod.POST)
    public Map<String, Object> initVerify(String taskId, String dsType, String username, String password,
                                          String taskStage, String requestType) {
        final AuthUserEntity user = ShiroUtils.getAuthUser();

        if (StringUtils.isBlank(user.getName())
                || StringUtils.isBlank(user.getIdNo())
                || StringUtils.isBlank(user.getMobile())) {
            return R.error("请先完善基本资料");
        }

        if (StringUtils.equalsIgnoreCase(requestType, "submit") && StringUtils.isBlank(taskId)) {
            final DsType ds = DsType.valueOf(dsType);
            R createTaskResult = shujumoheDsService.createTask(user, ds);
            if (createTaskResult.getCode() != 0) {
                return createTaskResult;
            }

            Map<String, Object> remoteResponse = MapUtils.getMap(createTaskResult, "remoteRes");
            taskId = MapUtils.getString(remoteResponse, "task_id");

            AuthUserReportDsEntity dsEntity = new AuthUserReportDsEntity();
            dsEntity.setCreateTime(new Date());
            dsEntity.setUserId(user.getUserId());
            dsEntity.setTaskId(taskId);
            dsEntity.setDsType(ds);
            dsEntity.setName(user.getName());
            dsEntity.setIdNo(user.getIdNo());
            dsEntity.setVerifyStatus(VerifyStatus.PROCESSING);
            authUserReportDsService.save(dsEntity);
        }

        return shujumoheDsService.initVerify(taskId, username, password, taskStage, requestType);
    }


    /**
     * 初始化验证
     */
    @RequestMapping(value = "/submit_verify", method = RequestMethod.POST)
    public Map<String, Object> submitVerify(String taskId, String smsCode, String authCode,
                                          String taskStage, String requestType) {
        final AuthUserEntity user = ShiroUtils.getAuthUser();

        R submitResult = shujumoheDsService.submitVerify(taskId, smsCode, authCode, taskStage, requestType);

        Map<String, Object> remoteResponse = MapUtils.getMap(submitResult, "remoteRes");
        int code = MapUtils.getIntValue(remoteResponse, "code", -1);
        handleSuccessResponse(taskId, code);
        return submitResult;
    }

    private void handleSuccessResponse(String taskId, int code) {
        int statusCode = code;

        AuthUserReportDsEntity dsEntity = authUserReportDsService.queryByTaskId(taskId);
        if (statusCode == 2007) {
            statusCode = shujumoheDsService.queryTaskResultCode(taskId);

            if (statusCode != 0 && statusCode != 137) {
                dsEntity.setVerifyStatus(VerifyStatus.FAILED);
                authUserReportDsService.update(dsEntity);
            }
        }

        if (statusCode == 0 || statusCode == 137) {
            dsEntity.setVerifyStatus(VerifyStatus.SUBMITTED);
            authUserReportDsService.update(dsEntity);
        }
    }
}
