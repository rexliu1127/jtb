package io.grx.youdun;


import com.alibaba.fastjson.JSONObject;
import io.grx.common.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;


public class YouDunUtils {

    public final Logger logger = LoggerFactory.getLogger(getClass());

    static final String CHASET_UTF_8 = "UTF-8";
    private static final CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

    static String secretkey = "5369a8c6-3298-4b77-9e42-4a7234c207b2";
    static String pub_key = "f098b5e4-e397-4d1a-8646-dea44111891f";


    public static void main(String[] args) {

        System.out.println(getDuoTouReport("430524199109151232"));
    }

    /**
     * 多头报告
     * @param idNo
     * @return
     */
    public static Map<String,Object> getDuoTouReport(String idNo)
    {
        Map<String,Object> map = new HashMap<>();
        map.put("status",0);
        map.put("respStr","");
        map.put("msg","未能获取到数据");
        try {
            String dataservice_url = "https://api4.udcredit.com/dsp-front/4.1/dsp-front/default/pubkey/%s/product_code/%s/out_order_id/%s/signature/%s";

            String orderId = "DT-" + DateUtils.getStringDate();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id_no", idNo);

            String sign = String.format("%s|%s", jsonObject, secretkey);
            String signature = MD5Encrpytion(sign.getBytes("UTF-8"));

            String url = String.format(dataservice_url, pub_key, "Y1001005", orderId, signature);

            String str = doHttpRequest(url, jsonObject);

            map.put("respStr",str);

            if(StringUtils.isNotBlank(str)){

                JSONObject json = JSONObject.parseObject(str);
                JSONObject header = json.getJSONObject("header");
                if("000000".equals(header.getString("ret_code"))){
                    map.put("status",1);
                }else{
                    map.put("msg",header.getString("ret_msg"));
                }
            }

        }catch (Exception ex)
        {
            map.put("status",-1);
            map.put("msg","获取多头报告出错");
        }

        return map;
    }





    /**
     * 银行卡四要素认证
     * @param realName
     * @param idNO
     * @param bankCardNo
     * @param mobile
     * @return
     */
    public static String BindBankCard(String realName, String idNO, String bankCardNo, String mobile)
    {
        Map<String,Object> map = new HashMap<>();
        try {

            String dataservice_url = "https://api4.udcredit.com/dsp-front/4.1/dsp-front/default/pubkey/%s/product_code/%s/out_order_id/%s/signature/%s";

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id_name", realName);
            jsonObject.put("id_no", idNO);
            jsonObject.put("bank_card_no", bankCardNo);
            jsonObject.put("mobile", mobile);
            jsonObject.put("req_type","01");

            String sign = String.format("%s|%s", jsonObject, secretkey);
            String signature = MD5Encrpytion(sign.getBytes("UTF-8"));
            String url = String.format(dataservice_url, pub_key, "O1001S0401", DateUtils.getStringDate(), signature);

            return doHttpRequest(url, jsonObject);

        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return "";
    }


    /**
     * H5拍照地址
     * @param orderId
     * @param ocr_callback_url
     * @return
     */
    public static String getOCRUrl(String orderId,String ocr_callback_url){

        try {
            String dataservice_url = "https://static.udcredit.com/id/v43/index.html";

            String time = DateUtils.getStringDate();

            String params = "pub_key=" + pub_key + "&partner_order_id=" + orderId + "&sign_time=" +time+ "&sign=" + getOCRSign(orderId,time)+"&return_url="+ocr_callback_url+"&callback_url="+ocr_callback_url;

            String encrypt = EncryptUtils.aesEncrypt(params, "4c43a8be85b64563a32244db9caf8454");
            String url = dataservice_url + "?apiparams=" + encrypt;

            return url;
        }catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return "";
    }

    /**
     * H5拍照结果查询
     * @param orderId
     * @return
     */
    public static String queryOCRResult(String orderId){

        try {
            String dataservice_url = "https://idsafe-auth.udcredit.com/front/4.3/api/order_query/pub_key/%s";

            String time = DateUtils.getStringDate();
            JSONObject jsonObject = new JSONObject();

            JSONObject jsonHeader = new JSONObject();
            jsonHeader.put("partner_order_id", orderId);
            jsonHeader.put("sign_time", time );
            jsonHeader.put("sign", getOCRSign(orderId,time));

            jsonObject.put("header",jsonHeader);

            String url = String.format(dataservice_url, pub_key);

            return doHttpRequest(url, jsonObject);

        }catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return "";
    }

    public static String getOCRSign(String orderId,String time)
    {
        return getMD5Sign(pub_key,orderId,time,secretkey);
    }


    private  static String getMD5Sign (String pub_key, String partner_order_id, String sign_time, String security_key) {
        String signStr = String.format("pub_key=%s|partner_order_id=%s|sign_time=%s|security_key=%s", pub_key, partner_order_id, sign_time, security_key);
        //System.out.println("测试输入签名signField："+ signStr);
        return MD5Encrpytion(signStr);
    }

    private static String MD5Encrpytion(String source)
    {
        try {

            char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

            byte[] strTemp = source.getBytes(Charset.forName("UTF-8"));
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
                str[(k++)] = hexDigits[(byte0 & 0xF)];
            }
            for (int m = 0; m < str.length; ++m) {
                if ((str[m] >= 'a') && (str[m] <= 'z')) {
                    str[m] = (char) (str[m] - ' ');
                }
            }
            return new String(str);
        } catch (Exception e) {
        }
        return null;

    }

    private static String MD5Encrpytion(byte[] source)
    {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(source);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
                str[(k++)] = hexDigits[(byte0 & 0xF)];
            }
            for (int m = 0; m < str.length; ++m) {
                if ((str[m] >= 'a') && (str[m] <= 'z')) {
                    str[m] = (char) (str[m] - ' ');
                }
            }
            return new String(str);
        } catch (Exception e) {
        }
        return null;
    }

    private static String doHttpRequest(String url, JSONObject jsonObject) throws Exception{
        //设置传入参数
        StringEntity stringEntity = new StringEntity(jsonObject.toJSONString(),CHASET_UTF_8);
        stringEntity.setContentEncoding(CHASET_UTF_8);
        stringEntity.setContentType("application/json");
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Connection","close");
        httpPost.setEntity(stringEntity);
        HttpResponse resp = closeableHttpClient.execute(httpPost);
        HttpEntity he = resp.getEntity();
        String respContent = EntityUtils.toString(he, CHASET_UTF_8);
        return respContent;
    }
}
