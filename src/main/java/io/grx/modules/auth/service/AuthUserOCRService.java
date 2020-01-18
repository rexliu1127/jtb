package io.grx.modules.auth.service;

import io.grx.modules.auth.entity.AuthUserOCREntity;

public interface AuthUserOCRService {

    void save(AuthUserOCREntity model);
    AuthUserOCREntity queryByUserId(long userId);
}
