package io.grx.auth.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.grx.auth.service.JuxinliService;
import io.grx.common.utils.R;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.auth.entity.AuthRequestEntity;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.enums.RequestStatus;
import io.grx.modules.auth.enums.VerifyStatus;
import io.grx.modules.auth.service.AuthRequestService;
import io.grx.modules.opt.service.ChannelService;

@Controller
@RequestMapping("/auth/jxl")
public class JuxinlinController {
    @Autowired
    private JuxinliService juxinliService;

    @Autowired
    private AuthRequestService authRequestService;

    @Autowired
    private ChannelService channelService;

    /**
     * 开始验证证
     */
    @RequestMapping(value = "/create_task")
    @ResponseBody
    public Map<String, Object> createTask() {
        AuthUserEntity user = ShiroUtils.getAuthUser();

//        ChannelEntity channelEntity = channelService.queryObject(user.getChannelId());
        AuthRequestEntity request = authRequestService.queryLatestByUserId(user.getUserId(), null);

        if (request != null) {
            if (request.getVerifyStatus() == VerifyStatus.PROCESSING) {
//                int code = shujuMoheService.queryTaskResultCode(request.getVerifyToken());
//                if (code == 0 || code == 137) {
//                    request.setVerifyStatus(VerifyStatus.SUBMITTED);
//                    authRequestService.update(request);
//
//                    return R.error("您有审核中的认证, 请勿重复提交!");
//                } else {
//                    request.setVerifyStatus(VerifyStatus.FAILED);
//                    authRequestService.update(request);
//                }
            } else if (request.getVerifyStatus() == VerifyStatus.SUBMITTED
                    && (request.getStatus() == RequestStatus.PENDING || request.getStatus() == RequestStatus
                    .PROCESSING)) {
                return R.error("您有审核中的认证, 请勿重复提交!");
            } else if (request.getStatus() == RequestStatus.REJECTED) {
                return R.error("您的认证已被拒绝, 请勿重复提交!");
            }
        }

        List<AuthRequestEntity> requests = authRequestService.queryByUserAndStatus(user.getUserId(), RequestStatus
                .PENDING, RequestStatus.PROCESSING);
        if (CollectionUtils.isNotEmpty(requests)) {
            return R.error("您有审核中的认证, 请勿重复提交!");
        }

        return juxinliService.sendApplications(user);
    }

    /**
     * 开始验证证
     */
    @RequestMapping(value = "/verify")
    @ResponseBody
    public Map<String, Object> verify(String token, String website, String mobilePass, String verifyCode,
                                      String type, String queryPwd) {
        AuthUserEntity user = ShiroUtils.getAuthUser();

        R result = juxinliService.sendMessages(user.getMobile(), token, website, mobilePass,
                queryPwd, verifyCode, type);
        if (MapUtils.getIntValue(result, "code", -1) == 0) {
            Map<String, Object> dataMap = MapUtils.getMap(result, "data");
            if (StringUtils.equals(MapUtils.getString(dataMap, "process_code"), "10008")) {
                AuthRequestEntity request = authRequestService.queryByToken(token);
                request.setVerifyStatus(VerifyStatus.SUBMITTED);

                authRequestService.update(request);
            }
        }

        return result;
    }
}
