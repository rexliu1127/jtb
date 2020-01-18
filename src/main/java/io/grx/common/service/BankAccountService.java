package io.grx.common.service;

public interface BankAccountService {
    String validateBankAccount(String idNo, String name, String account, String mobile);
}
