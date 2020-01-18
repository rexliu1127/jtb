package io.grx.pay.service;

import io.grx.modules.tx.entity.TxUserWithdrawEntity;

public interface AllinPayWithdrawService {
    int txUserWithdraw(TxUserWithdrawEntity txUserWithdrawEntity);
}
