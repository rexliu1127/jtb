package io.grx.tianji;


import io.grx.common.utils.DateUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TianjiUtils {

    private static final Logger logger = LoggerFactory.getLogger(TianjiUtils.class);

    private static String priKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM7oG/YdJDdgJhQb\n" +
            "sB9MVfOMFUl3Kvx627SW5QnuaHzrG902oAK4MMNG1UmoQMz3bv+7EcDqry6FuRxF\n" +
            "kxm+HQ1G22xlV9xwUfxE1XvHP0Md0EDMr78+w0umZzUHBYWiuPBmqsfHa/5pf5ui\n" +
            "FvmLmYH93FtY7BJ2pTCMHuv9SPs7AgMBAAECgYA2hravslhT+5OtYuqUJPLVvwnx\n" +
            "FWWPqfTyb32mk1yX5wGt13JtpQq8u5MD2UJSgbaP0EXKeE54TSdOSwuMEiznVIIU\n" +
            "xsTCu3pyS+ye+KtQPBHl1N/J0JMiIs50hKJ4G/NCYC4J+GKx3TZcqMyllKOfWVfG\n" +
            "qw3Vw1IQEwrFzUur2QJBAObfe781xEGKBZch6kPYVL4H11RHQlcC048Efzkms+4L\n" +
            "4624Z/ckEesNcb/lRAobdEtI2dCyDVk/XbfEpI1nTrcCQQDlbOIvNDrVk5G7R7TU\n" +
            "wj1InP+NSK7w1ryhs2JpSOwzxhSDOUh2OWTefhzK+kCU2P1sRWQcpuyB0rBwjgbe\n" +
            "evOdAkEAkvlotPRMiRCNuIW9tg0s/YDOYveuUugYuC47s61g1EU7XydLADk1Mvbv\n" +
            "GCOi6fBKW3b4OvRurQc66TdFLkO7BwJAQtQOppVeNexAXP8sK3VRCJ/CyNLNMpY6\n" +
            "aBeqqOR2+TXPr9G1y8o4GZ8+n9l5imISn8EuwjHVXpOSlZapZZEG+QJAAVpK89qF\n" +
            "fYswOXrvHgiZg1cBHjw4qNvKC9sFx59RA2QhS5nI4yFBoGBOk98AlhRH2jCJlCj9\n" +
            "ckWls9lzT/HfuA==";

    private static String appId = "2011182";

    private static boolean isTestEnv = false;

    public static JSONObject getTianjiData(String searchId) throws Exception {
        OpenapiClient sample = new OpenapiClient();
        sample.setLogid(DateUtils.getStringDate());
        sample.setAppId(appId);
        sample.setPrivateKey(priKey);
        sample.setIsTestEnv(isTestEnv);
        sample.setMethod("wd.api.mobilephone.getdatav2");
        sample.setField("user_id", searchId);
        sample.setField("merchant_id", appId);
        return sample.execute();
    }

    public static String getTianjiUrl(String name,String idNo,String phone,long userId,String outUniqueId,String domain) {
        JSONObject result = null;
        String url = "";
        OpenapiClient sample = new OpenapiClient();
        sample.setLogid(DateUtils.getStringDate());
        sample.setMethod("tianji.api.tianjireport.collectuser");
        sample.setAppId(appId);
        sample.setPrivateKey(priKey);
        sample.setIsTestEnv(isTestEnv);
        sample.setField("type", "mobile");
        sample.setField("platform", "web");
        sample.setField("name", name);
        sample.setField("phone", phone);
        sample.setField("idNumber", idNo);
        sample.setField("userId", userId+"");
        sample.setField("outUniqueId", outUniqueId);
        sample.setField("notifyUrl", domain+"/tj/operator/notify"); //通知地址
        sample.setField("returnUrl", domain+"/tj/operator/return"); //结果显示页面
        try {
            result = sample.execute();
        } catch (Exception e) {
            logger.error("TJ运营商H5URL接口调用失败", e);
        }
        logger.info("TJ运营商H5URL接口返回" + result);
        if (result != null && result.getInt("error") == 200) {
            url = result.getJSONObject("tianji_api_tianjireport_collectuser_response").getString("redirectUrl");
        } else {
            logger.info("TJ运营商H5URL接口调用失败" + result);
        }
        return url;
    }



    public static JSONObject getTianjiReportDetail(String userId, String outUniqueId, String reportType) {
        JSONObject result = null;
        OpenapiClient sample = new OpenapiClient();
        sample.setLogid(DateUtils.getStringDate());
        sample.setAppId(appId);
        sample.setPrivateKey(priKey);
        sample.setIsTestEnv(isTestEnv);
        sample.setMethod("tianji.api.tianjireport.detail");
        sample.setField("userId", userId);
        sample.setField("outUniqueId", outUniqueId);
        sample.setField("reportType", reportType);
        try {
            result = sample.execute();
        } catch (Exception e) {
            logger.error("调用TJ运营商报告接口失败", e);
        }

        return result;
    }

    public static void main(String[] args) throws Exception {

        //System.out.println(getTianjiReport("12833","12833-20190809142601","html"));



        System.out.println(getTianjiData("15653517156929699466"));
    }
}
