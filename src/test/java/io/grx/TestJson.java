package io.grx;

import com.rong360.tianji.sample.OpenapiClient;
import com.rong360.tianji.utils.RequestUtil;
import net.sf.json.JSONObject;

public class TestJson {
    private static String appId = "2010581";
    private static String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK6Y5EHv3Ki8m+cp" +
            "\njAKiONab9pcT5J3SoM9UPiPXm3ulGAt8mX3c5yebfduvjs4vKFLiwTo3Bf/D/Ek9\nDpOJA+3Z+GC11JtVb3IqtGXP4sZOMF3j9+IvkrqhWCEXp8OGuedbA7xU2nwkydNA\nST6sgD9Mkh1UgmW0V+VxOHUlbaDpAgMBAAECgYEAmPOPYsQCBj/UQ9l9sgDy0e6n\nQwpGSIvwHDCsjzGeH98tBUMOI9iVF3l79CwDalSdep7yr1DsjHbgWDiIwG5TZWJ/\nfLwB5PqjHUEBcHdneqx0INQKp4LcFxRVRLiLlq3vz6EJorGj4RzYurjUAZwH+kUR\nIlE83H5Q6uVIvmk3CgECQQDn8hlWawy4VlcTpw4KFfHveGQzuUTEpnH6fEhkwyRp\ncM2kPI8TtR6pFc5VKBhk/Rl+pWx6/L42OSBEfDW8OWA5AkEAwLRBJnNF9x2sHSz6\nM4cCnnhG/Sfbsbc/rauqsRKI62w/ilM550bZULXSlA9SDHfUUy4P/n/wZFvV0uEU\nk/fmMQJAKca1QZduZxVGAcgpAzAIr3Ujtx07gZ/pD5CrCVsMh+FFaLtvmcEZkKLY\n0wWxvx7HJMRu0YgMSn/ni+5DT2+WIQJBALZOkcg/i/RyZO8hKv9ufeLQJVDA0Y46\nsAqseoqU32Xh/ebuP7x2gYdizHp4WAYlo4Ch9k2uWg2H+C1N9TrbbzECQGYuLtGe\nb8wAhb6aDmnRcDaKeN1gcWfZStEBq/AIjwpdHBnFeYxC+CpvuQjNQCSyBWsomnJ/\nk31zFoLoWRRjzuA=";
    public static void main (String[] args) throws Exception {
        OpenapiClient sample = new OpenapiClient();

        sample.setAppId(appId); // TODO 设置Appid
        sample.setPrivateKey(privateKey); // TODO 设置机构私钥，需要使用方替换private_key.pem文件
        sample.setIsTestEnv(false); // TODO 设置为请求测试环境，默认为线上环境（false），需要使用方替换，也可不替换
        sample.setPrintLog(true);
        sample.setLogid(RequestUtil.generateLogid());

//        sample.setMethod("tianji.api.tianjireport.collectuser");
//        sample.setField("type", "mobile");
//        sample.setField("platform", "web");
//        sample.setField("name", "张三");
//        sample.setField("phone", "19900000000");
//        sample.setField("idNumber", "440921198306038036");
//        sample.setField("userId", "1");
//        sample.setField("outUniqueId", UUID.randomUUID().toString());
//        sample.setField("returnUrl", "http://www.baidu.com/");
//        sample.setField("notifyUrl", "http://www.baidu.com/");

        sample.setMethod("tianji.api.tianjireport.detail");
        sample.setField("userId", "PT012");
        sample.setField("outUniqueId", "77443c59-e1e9-42b7-8854-64dcd67c1e3e");
        sample.setField("reportType", "html");

        JSONObject ret = sample.execute();
        System.out.println(ret.toString());
    }
}
