package io.grx.auth.service;

import io.grx.common.utils.R;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.enums.DsType;

public interface ShujumoheDsService {

    R createTask(AuthUserEntity authUser, DsType dsType);

    R initVerify(final String taskId, final String userName, final String password, String taskStage,
                 String requestType);

    R submitVerify(final String taskId, final String smsCode, final String authCode, String taskStage,
                   String requestType);

    String queryTaskResult(final String taskId);

    int queryTaskResultCode(final String taskId);
}
