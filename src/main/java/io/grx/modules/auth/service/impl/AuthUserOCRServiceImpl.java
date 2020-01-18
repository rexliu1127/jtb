package io.grx.modules.auth.service.impl;

import io.grx.modules.auth.dao.AuthUserOCRDao;
import io.grx.modules.auth.entity.AuthUserOCREntity;
import io.grx.modules.auth.service.AuthUserOCRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthUserOCRServiceImpl implements AuthUserOCRService {

    @Autowired
    private AuthUserOCRDao authUserOCRDao;

    @Override
    public void save(AuthUserOCREntity model) {
        authUserOCRDao.save(model);
    }

    @Override
    public AuthUserOCREntity queryByUserId(long userId) {
        return authUserOCRDao.queryByUserId(userId);
    }
}
