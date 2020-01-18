package io.grx.common.service.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rong360.tianji.sample.OpenapiClient;
import com.rong360.tianji.utils.RequestUtil;
import io.grx.common.service.BankAccountService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//@Service
public class TianJiBankAccountService implements BankAccountService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${bank.enabled}")
    private boolean enabled;
    @Value("${bank.fakeBankMobile}")
    private String fakeBankMobile;
    @Value("${tianji.appId}")
    private String appId;
    @Value("${tianji.privateKey}")
    private String privateKey;

public static void main(String[] args) throws Exception {
        TianJiBankAccountService s = new TianJiBankAccountService();
        s.enabled = true;
        s.appId = "2011189";
        s.privateKey = "//zlfZ6kX3t\nzEDWyhh+NYfD+j9bN/WT6KOjF5B0GMVgV+bubh5IqwsFPueKdgoP8fXiuLH/9sGQ\nRXG3xarYJcpmjFy97Z9m+Rt7jmXcw4KQu2x8/VD5NduXn2O7PjCZIUNDMSVWZyuz\ne0xHW8a5/vWMLLk/M/SCHJXAIUZHAgMBAAECgYEAiX9hcEEdWe3Vbz2Y07JfIZpR\nA9pHQRUaFT+TpYpDMUKXYwM4gNvpYhQsTHuWbb24q1mM526624ELdipiHfIZcOwa\n2DJ1S5CJGCeaHm9SyC0lX0fc/lvszKK2dOcIDl3VohCHGvb76kv3iK7bh6D8xzqk\nW+f0Bs6a2ZFIyZ5VCqkCQQDbfnmTTmRJpTlzQC4cYpo2y8bAZVadk617J53wEB44\niiw3o7FfGb0D86DUEYSx2fWwFGuhNoOEOOHZNGqN3VUDAkEA0r3pJHE8ceXI+/B1\nXqGHbYj/Q3AFx8iUirF1nVmPc0sO5eQyQMh072AevSnBXCAnzcWmAAyp9t3viua6\nDx1cbQJAfGooR5M7zXLWKbnLaVOKzlybgBrxCcjXoONH5vd76diIT9F9jMZuGXtT\nQmHbsWQ2m2Q3zd20lpIXkqLfcyJuOQJAAtOXP+zg4aoyUua7vEQwW9C9k7r56N8Q\n1vdgW3brDKHSFlVEM0g9AUTpxKG9vS2VOxZOjqucz/nUD6nejvtW1QJAWWQ1wVZ/\ngtN1QdHnLYcXpJ14DVNYpKItVA7wCsZeqJAUYUw3CFfbRmmxk+Naplo1hqZxl0x5\ngYX9n2eUq/lOyQ==";

        System.out.println(s.validateBankAccount("", "", "", ""));
    }

 @Override
    public String validateBankAccount(final String idNo, final String name, final String account, final String mobile) {
        if (!enabled || StringUtils.equals(mobile, fakeBankMobile)) {
            return "中国**银行";
        }

       OpenapiClient sample = new OpenapiClient();

        sample.setAppId(appId); // TODO 设置Appid
        sample.setPrivateKey(privateKey); // TODO 设置机构私钥，需要使用方替换private_key.pem文件
        sample.setIsTestEnv(false); // TODO 设置为请求测试环境，默认为线上环境（false），需要使用方替换，也可不替换
        sample.setPrintLog(true);
        sample.setLogid(RequestUtil.generateLogid());

        sample.setMethod("tianji.api.xinlian.bankcardcheck4item");
        sample.setField("name", name);
        sample.setField("idNumber", idNo);
        sample.setField("phone", mobile);
        sample.setField("bankCard", account);

     try {
            logger.info("check bank for name: {}, idNumber: {}, phone: {}, bankCard: {}", name, idNo, mobile, account);
            JSONObject ret = sample.execute();
            logger.info("validate bank result: {}", ret.toString());

            if (ret.getInt("error") == 200) {
                JSONArray results = ret.getJSONArray("tianji_api_xinlian_bankcardcheck4item_response");
                if (results.size() > 0) {
                    JSONObject content = results.getJSONObject(0);

                    if (StringUtils.equalsIgnoreCase(content.getString("checkStatus"), "S")) {
                        JSONObject checkResult = content.getJSONObject("checkResult");
                        String bankName = checkResult.getString("bankName");
                        if (StringUtils.isBlank(bankName)){
                            bankName = "未知银行";
                        }
                        return bankName;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to validate bank", e);
        }
        return null;
    }
}
