package io.grx.modules.auth.service;

import io.grx.modules.auth.entity.AuthUserOCRRequestLogEntity;

public interface AuthUserOCRRequestLogService {

    void save(AuthUserOCRRequestLogEntity model);

    int updateStatus(String orderId,int status);

    AuthUserOCRRequestLogEntity queryByOrderId(String orderId);
}
