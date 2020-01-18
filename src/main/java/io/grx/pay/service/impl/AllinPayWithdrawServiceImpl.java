package io.grx.pay.service.impl;

import io.grx.modules.tx.entity.TxUserWithdrawEntity;
import io.grx.pay.service.AllinPayWithdrawService;
import org.springframework.stereotype.Service;

@Service
public class AllinPayWithdrawServiceImpl implements AllinPayWithdrawService {
    private AllinPayTranxServiceImpl tranxService;

    public AllinPayWithdrawServiceImpl() {
        tranxService = new AllinPayTranxServiceImpl();
    }

    @Override
    public int txUserWithdraw(TxUserWithdrawEntity txUserWithdrawEntity) {
        return 0;
    }
}
