package io.grx.allinpay;


import java.util.Map;
import java.util.TreeMap;

import io.grx.common.utils.UUIDGenerator;
import io.grx.pay.util.HttpConnectionUtil;
import io.grx.pay.util.SybUtil;

public class SybPayService {
    public static void main(String[] args) throws Exception {
        Map<String, String> request = new SybPayService().pay(1, "2018-test-001",
                "W02", UUIDGenerator.getUUID(), "米仓云服借条服务",
                "oR-Bg0gfa5guv3ps7StsOwCzCen8", "", "http://baidu.com", "no_credit");

        System.out.println(request);
        System.out.println("end");
    }

    public Map<String, String> pay(long trxamt, String reqsn,
                                   String paytype, String body,
                                   String remark, String acct,
                                   String authcode, String notify_url,
                                   String limit_pay) throws Exception {
        HttpConnectionUtil http = new HttpConnectionUtil(SybConstants.SYB_APIURL + "/pay");
        http.init();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("cusid", SybConstants.SYB_CUSID);
        params.put("appid", SybConstants.SYB_APPID);
        params.put("version", "11");
        params.put("trxamt", String.valueOf(trxamt));
        params.put("reqsn", reqsn);
        params.put("paytype", paytype);
        params.put("randomstr", SybUtil.getValidatecode(8));
        params.put("body", body);
        params.put("remark", remark);
        params.put("acct", acct);
//        params.put("authcode", authcode);
        params.put("notify_url", notify_url);
        params.put("limit_pay", limit_pay);
        params.put("sign", SybUtil.sign(params, SybConstants.SYB_APPKEY));
        byte[] bys = http.postParams(params, true);
        String result = new String(bys, "UTF-8");
        Map<String, String> map = handleResult(result);
        return map;

    }

    public Map<String, String> cancel(long trxamt, String reqsn, String oldtrxid, String oldreqsn) throws Exception {
        HttpConnectionUtil http = new HttpConnectionUtil(SybConstants.SYB_APIURL + "/cancel");
        http.init();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("cusid", SybConstants.SYB_CUSID);
        params.put("appid", SybConstants.SYB_APPID);
        params.put("version", "11");
        params.put("trxamt", String.valueOf(trxamt));
        params.put("reqsn", reqsn);
        params.put("oldtrxid", oldtrxid);
        params.put("oldreqsn", oldreqsn);
        params.put("randomstr", SybUtil.getValidatecode(8));
        params.put("sign", SybUtil.sign(params, SybConstants.SYB_APPKEY));
        byte[] bys = http.postParams(params, true);
        String result = new String(bys, "UTF-8");
        Map<String, String> map = handleResult(result);
        return map;
    }

    public Map<String, String> close(long trxamt, String oldtrxid, String oldreqsn) throws Exception {
        HttpConnectionUtil http = new HttpConnectionUtil(SybConstants.SYB_APIURL + "/close");
        http.init();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("cusid", SybConstants.SYB_CUSID);
        params.put("appid", SybConstants.SYB_APPID);
        params.put("version", "11");
        params.put("oldreqsn", oldreqsn);
        params.put("oldtrxid", oldtrxid);
        params.put("randomstr", SybUtil.getValidatecode(8));
        params.put("sign", SybUtil.sign(params, SybConstants.SYB_APPKEY));
        byte[] bys = http.postParams(params, true);
        String result = new String(bys, "UTF-8");
        Map<String, String> map = handleResult(result);
        return map;
    }

    public Map<String, String> refund(long trxamt, String reqsn, String oldtrxid, String oldreqsn) throws Exception {
        HttpConnectionUtil http = new HttpConnectionUtil(SybConstants.SYB_APIURL + "/refund");
        http.init();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("cusid", SybConstants.SYB_CUSID);
        params.put("appid", SybConstants.SYB_APPID);
        params.put("version", "11");
        params.put("trxamt", String.valueOf(trxamt));
        params.put("reqsn", reqsn);
        params.put("oldreqsn", oldreqsn);
        params.put("oldtrxid", oldtrxid);
        params.put("randomstr", SybUtil.getValidatecode(8));
        params.put("sign", SybUtil.sign(params, SybConstants.SYB_APPKEY));
        byte[] bys = http.postParams(params, true);
        String result = new String(bys, "UTF-8");
        Map<String, String> map = handleResult(result);
        return map;
    }

    public Map<String, String> query(String reqsn, String trxid) throws Exception {
        HttpConnectionUtil http = new HttpConnectionUtil(SybConstants.SYB_APIURL + "/query");
        http.init();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("cusid", SybConstants.SYB_CUSID);
        params.put("appid", SybConstants.SYB_APPID);
        params.put("version", "11");
        params.put("reqsn", reqsn);
        params.put("trxid", trxid);
        params.put("randomstr", SybUtil.getValidatecode(8));
        params.put("sign", SybUtil.sign(params, SybConstants.SYB_APPKEY));
        byte[] bys = http.postParams(params, true);
        String result = new String(bys, "UTF-8");
        Map<String, String> map = handleResult(result);
        return map;
    }


    public static Map<String, String> handleResult(String result) throws Exception {
        Map map = SybUtil.json2Obj(result, Map.class);
        if (map == null) {
            throw new Exception("返回数据错误");
        }
        if ("SUCCESS".equals(map.get("retcode"))) {
            TreeMap tmap = new TreeMap();
            tmap.putAll(map);
            String sign = tmap.remove("sign").toString();
            String sign1 = SybUtil.sign(tmap, SybConstants.SYB_APPKEY);
            if (sign1.toLowerCase().equals(sign.toLowerCase())) {
                return map;
            } else {
                throw new Exception("验证签名失败");
            }

        } else {
            throw new Exception(map.get("retmsg").toString());
        }
    }
}

