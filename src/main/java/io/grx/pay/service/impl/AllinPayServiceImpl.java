package io.grx.pay.service.impl;

import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import io.grx.pay.service.AllinPayService;
import io.grx.pay.util.HttpConnectionUtil;
import io.grx.pay.util.SybUtil;
import io.jsonwebtoken.lang.Assert;

@Service
@ConfigurationProperties(prefix = "allinpay")
public class AllinPayServiceImpl implements AllinPayService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private String cusId;
    private String appId;
    private String appKey;
    private String payPath;
    private String host;

    public Map<String, String> payByWx(long amount, String orderNo,
                                   String title,
                                   String remark, String openId,
                                   String callbackUrl) throws Exception {
        Assert.notNull(openId);
        Assert.notNull(orderNo);
        Assert.isTrue(amount > 0);

        HttpConnectionUtil http = new HttpConnectionUtil(host + payPath);
        http.init();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("cusid", cusId);
        params.put("appid", appId);
        params.put("version", "11");
        params.put("trxamt", String.valueOf(amount));
        params.put("reqsn", orderNo);
        params.put("paytype", "W02");
        params.put("randomstr", SybUtil.getValidatecode(8));
        params.put("body", title);
        params.put("remark", remark);
        params.put("acct", openId);
//        params.put("authcode", authcode);
        params.put("notify_url", callbackUrl);
        params.put("limit_pay", "no_credit");
        params.put("sign", SybUtil.sign(params, appKey));
        byte[] bys = http.postParams(params, true);
        String result = new String(bys, "UTF-8");
        Map<String, String> map = handleResult(result);
        return map;

    }

    public Map<String, String> handleResult(String result) throws Exception {
        Map map = SybUtil.json2Obj(result, Map.class);
        if (map == null) {
            throw new Exception("返回数据错误");
        }
        if ("SUCCESS".equals(map.get("retcode"))) {
            TreeMap tmap = new TreeMap();
            tmap.putAll(map);
            String sign = tmap.remove("sign").toString();
            String sign1 = SybUtil.sign(tmap, appKey);
            if (sign1.toLowerCase().equals(sign.toLowerCase())) {
                return map;
            } else {
                throw new Exception("验证签名失败");
            }

        } else {
            throw new Exception(map.get("retmsg").toString());
        }
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(final String cusId) {
        this.cusId = cusId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(final String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(final String appKey) {
        this.appKey = appKey;
    }

    public String getPayPath() {
        return payPath;
    }

    public void setPayPath(final String payPath) {
        this.payPath = payPath;
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }
}
