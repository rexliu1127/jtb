package io.grx.modules.auth.service.impl;

import io.grx.modules.auth.dao.AuthUserTianJiDao;
import io.grx.modules.auth.entity.AuthUserTianJi;
import io.grx.modules.auth.service.AuthUserTianJiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthUserTianJiServiceImpl implements AuthUserTianJiService {

    @Autowired
    private AuthUserTianJiDao authUserReportTJDao;

    @Override
    public void save(AuthUserTianJi model) {
        authUserReportTJDao.save(model);
    }

    @Override
    public void update(AuthUserTianJi model) {
        authUserReportTJDao.update(model);
    }

    @Override
    public AuthUserTianJi queryByUserId(long userId) {
        return authUserReportTJDao.queryByUserId(userId);
    }
}
