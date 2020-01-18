package io.grx.auth.service;

import io.grx.common.utils.R;
import io.grx.modules.auth.entity.AuthUserEntity;

public interface ShujuMoheMobileService {

    R createTask(AuthUserEntity authUser);

    R initVerifyTask(String taskId, String mobile, String password);

    R queryInitVerifyTask(String taskId, String mobile, String password);

    R verifyTask(String taskId, String smsCode, String authCode, String taskStage);

    R queryVerifyTask(String taskId, String smsCode, String authCode, String taskStage);

    R retryTask(String taskId);

    String queryTaskResult(String taskId);

    int queryTaskResultCode(String taskId);

    String queryReportResult(String taskId, AuthUserEntity userEntity);
}
