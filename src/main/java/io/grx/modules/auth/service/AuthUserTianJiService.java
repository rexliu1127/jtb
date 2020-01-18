package io.grx.modules.auth.service;

import io.grx.modules.auth.entity.AuthUserTianJi;

public interface AuthUserTianJiService {

    void save(AuthUserTianJi model);

    void update(AuthUserTianJi model);

    AuthUserTianJi queryByUserId(long userId);
}
