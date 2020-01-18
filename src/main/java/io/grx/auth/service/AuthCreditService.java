package io.grx.auth.service;

import io.grx.common.enums.ValueEnum;
import io.grx.common.utils.R;
import io.grx.modules.auth.enums.CreditType;

import java.util.Map;

public interface AuthCreditService {

    ValueEnum getVendorType(CreditType creditType);

    /**
     * 请求获取认证h5 URL
     * @param creditType
     * @return
     */
    R requestH5Url(CreditType creditType);

    /**
     * 获取当前用户所有认证状态.
     * key为所有的CreditType.name(), 全小写。例如taobaopay.
     * value为数字: -1 - 未认证, 0 - 认证已提交, 1 - 认证成功, 2 - 认证失败, 3 - 认证中, 4 - 认证失败
     * @return
     */
    Map<String, Integer> getAllCreditStatus();
}
