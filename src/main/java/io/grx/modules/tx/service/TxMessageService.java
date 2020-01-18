package io.grx.modules.tx.service;

import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxExtensionEntity;
import io.grx.modules.tx.entity.TxRepaymentEntity;

public interface TxMessageService {

    void sendMsgForCancelingTransaction(TxBaseEntity txBaseEntity);

    void sendMsgForExpiredTransaction(TxBaseEntity txBaseEntity);

    void sendMsgForConfirmingTransaction(TxBaseEntity txBaseEntity);

    void sendMsgForRepay(TxRepaymentEntity txRepaymentEntity);

    void sendMsgForConfirmingRepay(TxRepaymentEntity txRepaymentEntity);

    void sendMsgForRejectingRepay(TxRepaymentEntity txRepaymentEntity);

    void sendMsgForPendingExtension(TxExtensionEntity txExtensionEntity);

    void sendMsgForConfirmingExtension(TxExtensionEntity txExtensionEntity);

    void sendMsgForRejectingExtension(TxExtensionEntity txExtensionEntity);

    /**
     * 出借人销账
     * @param txBaseEntity
     */
    void sendMsgForLenderCompleteTx(TxBaseEntity txBaseEntity);
}
