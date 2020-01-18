package io.grx.modules.auth.service.impl;

import io.grx.modules.auth.dao.AuthUserOCRRequestLogDao;
import io.grx.modules.auth.entity.AuthUserOCRRequestLogEntity;
import io.grx.modules.auth.service.AuthUserOCRRequestLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthUserOCRRequestLogServiceImpl implements AuthUserOCRRequestLogService {

    @Autowired
    private AuthUserOCRRequestLogDao authUserOCRRequestLogDao;

    @Override
    public void save(AuthUserOCRRequestLogEntity model) {
         authUserOCRRequestLogDao.save(model);
    }

    @Override
    public int updateStatus(String orderId, int status) {

        AuthUserOCRRequestLogEntity authUserOCRRequestLogEntity = new AuthUserOCRRequestLogEntity();
        authUserOCRRequestLogEntity.setOrderId(orderId);
        authUserOCRRequestLogEntity.setStatus(status);

        return authUserOCRRequestLogDao.update(authUserOCRRequestLogEntity);
    }

    @Override
    public AuthUserOCRRequestLogEntity queryByOrderId(String orderId) {
        return authUserOCRRequestLogDao.queryByOrderId(orderId);
    }
}
