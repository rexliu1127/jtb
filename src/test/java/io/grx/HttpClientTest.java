package io.grx;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import io.grx.common.utils.Constant;
import io.grx.common.utils.HttpUtils;

/**
 * Created by zealotpz on 2017/2/9.
 */
public class HttpClientTest {
    //设置链接超时和请求超时等参数，否则会长期停止或者崩溃
    private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).setConnectionRequestTimeout(60000).build();

    public static String sendHttpsPost(String url, Map<String, String> params) {
        String responseContent = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
                nvps.add(new BasicNameValuePair(key, params.get(key)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setDefaultRequestConfig(requestConfig).build();
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                responseContent = EntityUtils.toString(httpEntity, HTTP.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseContent;
    }

    //创建SSL安全连接
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslsf;
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("account_mobile", "15544444444");
        params.put("id_number", "251123195802251873");
        params.put("account_name", "皮晴晴");
        params.put("biz_code", "loanweb");
//        params.put("company_type", "私营");
//        params.put("coborrower_home_address", "浙江省杭州市西湖区古荡新村2幢201");
//        params.put("career", "半专业人员");
//        params.put("occupation", "见习专员");
//        params.put("contact3_relation", "test");
//        params.put("customer_channel", "test");
//        params.put("contact5_name", "test");
//        params.put("work_phone", "0571-111111111");
//        params.put("surety_name", "刘能");
//        params.put("contact1_id_number", "test");
//        params.put("contact5_id_number", "test");
//        params.put("loan_purpose", "车贷");
//        params.put("coborrower_id_number", "321282555555555555");
//        params.put("coborrower_phone", "0571-10101010");
//        params.put("surety_phone", "0571-222222222");
//        params.put("trueip_address", "test");
//        params.put("token_id", "test");
//        params.put("house_property", "有房");
//        params.put("contact2_id_number", "test");
//        params.put("diploma", "研究生");
//        params.put("annual_income", "100000-200000");
//        params.put("id_number", "123123123123123000");
//        params.put("surety_id_number", "321282333333333333");
//        params.put("card_number", "6333380402564890000");
//        params.put("contact1_mobile", "test");
//        params.put("account_phone", "0571-42331233");
//        params.put("loan_amount", "10000");
//        params.put("qq_number", "313131313");
//        params.put("monthly_income", "12000以上");
//        params.put("apply_province", "四川");
//        params.put("surety_mobile", "15223456789");
//        params.put("contact4_relation", "test");
//        params.put("contact5_mobile", "test");
//        params.put("loan_term", "12");
//        params.put("account_mobile", "13113131313");
//        params.put("organization_address", "浙江省杭州市阿里巴巴西溪园区");
//        params.put("contact3_mobile", "test");
//        params.put("work_time", "1年以下");
//        params.put("contact3_id_number", "test");
//        params.put("contact3_name", "test");
//        params.put("coborrower_name", "王五");
//        params.put("loan_date", "2015-11-19");
//        params.put("applyer_type", "在职");
//        params.put("is_cross_loan", "否");
//        params.put("industry", "金融业");
//        params.put("surety_company_address", "浙江省杭州市下城区潮王路18号");
//        params.put("contact2_name", "test");
//        params.put("resp_detail_type", "test");
//        params.put("apply_city", "成都");
//        params.put("account_email", "212121212@qq.com");
//        params.put("surety_home_address", "浙江省杭州市西湖区古荡新村");
//        params.put("home_address", "浙江省杭州市西湖区古荡新村2幢101");
//        params.put("marriage", "未婚");
//        params.put("account_name", "张三");
//        params.put("contact5_relation", "test");
//        params.put("house_type", "商品房");
//        params.put("contact_address", "浙江省杭州市西湖区古荡新村2幢101");
//        params.put("contact1_name", "test");
//        params.put("contact4_id_number", "test");
//        params.put("contact2_relation", "test");
//        params.put("coborrower_mobile", "17012345678");
//        params.put("apply_channel", "app申请");
//        params.put("contact4_name", "test");
//        params.put("ip_address", "test");
//        params.put("coborrower_company_address", "杭州市江干区2号大街928号");
//        params.put("graduate_school", "南京大学");
//        params.put("contact1_relation", "test");
//        params.put("contact4_mobile", "test");
//        params.put("organization", "阿里巴巴西溪园区");
//        params.put("contact2_mobile", "test");

//        String res = sendHttpsPost("https://apitest.tongdun.cn/bodyguard/apply/v4?partner_code=zhejin" +
//                "&partner_key=5569163436c94ab5b672f6a38b1bb916&app_name=zhejin_web", params);

        Map<String, String> querys = new HashMap<>();
        querys.put("partner_code", "zhejin");
        querys.put("partner_key", "5569163436c94ab5b672f6a38b1bb916");
        querys.put("app_name", "zhejin_web");
        HttpResponse response = HttpUtils.doPost("https://apitest.tongdun.cn", "/bodyguard/apply/v4", MapUtils
                        .EMPTY_MAP,
                querys, params);
        String res = EntityUtils.toString(response.getEntity(), Constant.ENCODING_UTF8);
        System.out.println(res);
    }
}

