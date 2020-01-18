package io.grx.modules.auth.service.impl;

import io.grx.modules.auth.dao.AuthUserBankCardDao;
import io.grx.modules.auth.entity.AuthUserBankCard;
import io.grx.modules.auth.service.AuthUserBankCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthUserBankCardServiceImpl implements AuthUserBankCardService {

    @Autowired
    private AuthUserBankCardDao authUserBankCardDao;

    @Override
    public void save(AuthUserBankCard model) {

        authUserBankCardDao.save(model);
    }

    @Override
    public AuthUserBankCard queryByUserId(long userId) {
        return authUserBankCardDao.queryByUserId(userId);
    }
}
