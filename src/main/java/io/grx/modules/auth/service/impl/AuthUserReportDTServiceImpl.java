package io.grx.modules.auth.service.impl;

import io.grx.modules.auth.dao.AuthUserReportDTDao;
import io.grx.modules.auth.entity.AuthUserReportDT;
import io.grx.modules.auth.service.AuthUserReportDTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthUserReportDTServiceImpl implements AuthUserReportDTService {

    @Autowired
    private AuthUserReportDTDao authUserReportDTDao;

    @Override
    public void save(AuthUserReportDT model) {
        authUserReportDTDao.save(model);
    }

    @Override
    public AuthUserReportDT queryByUserId(long userId) {
        return authUserReportDTDao.queryByUserId(userId);
    }
}

