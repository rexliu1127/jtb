package io.grx.modules.auth.service;

import io.grx.modules.auth.entity.AuthUserBankCard;

public interface AuthUserBankCardService {

    void save(AuthUserBankCard model);

    AuthUserBankCard queryByUserId(long userId);
}
