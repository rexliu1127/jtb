package io.grx.auth.controller;


import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.grx.auth.service.ShujuMoheMobileService;
import io.grx.common.utils.R;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.auth.entity.AuthRequestEntity;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.enums.RequestStatus;
import io.grx.modules.auth.enums.VerifyStatus;
import io.grx.modules.auth.service.AuthRequestService;
import io.grx.modules.opt.service.ChannelService;

@Controller
@RequestMapping("/auth/sjmh")
public class ShujumoheController {
    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private ShujuMoheMobileService shujuMoheMobileService;

    @Autowired
    private AuthRequestService authRequestService;

    @Autowired
    private ChannelService channelService;

    /**
     * 开始验证证
     */
    @RequestMapping(value = "/create_task", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> createTask() {
        AuthUserEntity user = ShiroUtils.getAuthUser();

        AuthRequestEntity request = authRequestService.queryLatestByUserId(user.getUserId(), null);
        if (request != null) {
            if (request.getVerifyStatus() == VerifyStatus.PROCESSING) {
                int code = shujuMoheMobileService.queryTaskResultCode(request.getVerifyToken());
                if (code == 0 || code == 137) {
                    request.setVerifyStatus(VerifyStatus.SUBMITTED);
                    authRequestService.update(request);

                    return R.error("您有审核中的认证, 请勿重复提交!");
                } else {
                    request.setVerifyStatus(VerifyStatus.FAILED);
                    authRequestService.update(request);
                }
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

        return shujuMoheMobileService.createTask(user);
    }

    /**
     * 初始化验证
     */
    @RequestMapping(value = "/init_verify", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> initVerify(String taskId) {
        AuthUserEntity user = ShiroUtils.getAuthUser();

        return shujuMoheMobileService.initVerifyTask(taskId, user.getMobile(), user.getMobilePass());
    }

    /**
     * 查询初始化验证状态
     */
    @RequestMapping(value = "/init_verify_status", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> initVerifyStatus(String taskId) {
        AuthUserEntity user = ShiroUtils.getAuthUser();

        return shujuMoheMobileService.queryInitVerifyTask(taskId, user.getMobile(), user.getMobilePass());
    }

    /**
     * 初始化验证
     */
    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> verify(String taskId, String smsCode, String authCode, String taskStage) {
        AuthUserEntity user = ShiroUtils.getAuthUser();

        R response = shujuMoheMobileService.verifyTask(taskId, smsCode, authCode, taskStage);
        Map<String, Object> remoteRes = (Map<String, Object>) response.get("remoteRes");
        int code = MapUtils.getIntValue(remoteRes, "code", -1);


        AuthRequestEntity requestEntity = authRequestService.queryByToken(taskId);
        if (requestEntity == null) {
            return R.error("token错误，请重新提交认证");
        }

        if (code == 137) {
            requestEntity.setVerifyStatus(VerifyStatus.SUBMITTED);
            authRequestService.update(requestEntity);
        } else if (code == 2007) {
            code = shujuMoheMobileService.queryTaskResultCode(taskId);
            if (code == 0 || code == 137) {
                requestEntity.setVerifyStatus(VerifyStatus.SUBMITTED);
            } else if (code == 2007) {

            } else {
                requestEntity.setVerifyStatus(VerifyStatus.FAILED);
            }
            authRequestService.update(requestEntity);
        }
        return response;
    }

    /**
     * 查询初始化验证状态
     */
    @RequestMapping(value = "/verify_status", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> verifyStatus(String taskId, String smsCode, String authCode, String taskStage) {
        AuthUserEntity user = ShiroUtils.getAuthUser();

        R response = shujuMoheMobileService.queryVerifyTask(taskId, smsCode, authCode, taskStage);
        Map<String, Object> remoteRes = (Map<String, Object>) response.get("remoteRes");
        int code = MapUtils.getIntValue(remoteRes, "code", -1);


        AuthRequestEntity requestEntity = authRequestService.queryByToken(taskId);
        if (requestEntity == null) {
            return R.error("token错误，请重新提交认证");
        }

        if (code == 137) {
            requestEntity.setVerifyStatus(VerifyStatus.SUBMITTED);
            authRequestService.update(requestEntity);
        } else if (code == 2007) {
            code = shujuMoheMobileService.queryTaskResultCode(taskId);
            if (code == 0 || code == 137) {
                requestEntity.setVerifyStatus(VerifyStatus.SUBMITTED);
            } else if (code == 2007) {

            } else {
                requestEntity.setVerifyStatus(VerifyStatus.FAILED);
            }
            authRequestService.update(requestEntity);
        }
        return response;
    }

    /**
     * 查询初始化验证状态
     */
    @RequestMapping(value = "/retry", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> retryTask(String taskId) {
        AuthUserEntity user = ShiroUtils.getAuthUser();

        return shujuMoheMobileService.retryTask(taskId);
    }
}
