package io.grx;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.grx.common.utils.Constant;
import io.grx.common.utils.HttpUtils;

public class BodyGuardApiInvoker {

    private static final Log log = LogFactory.getLog(BodyGuardApiInvoker.class);
    private static final String API_HOST = "https://apitest.tongdun.cn";
    private static final String API_PATH = "/bodyguard/apply/v4";
    private static final String apiUrl = "https://apitest.tongdun.cn/bodyguard/apply/v4";
    private static final String PARTNER_CODE = "zhejin";// 合作方标识
    private static final String PARTNER_KEY  = "5569163436c94ab5b672f6a38b1bb916";//合作方密钥
    private static final String PARTNER_APP  = "zhejin_web";//应用名

    public BodyGuardApiResponse invoke(Map<String, String> params) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            Map<String, String> queries = new LinkedHashMap<>();
            queries.put("partner_code", PARTNER_CODE);
            queries.put("partner_key", PARTNER_KEY);
            queries.put("app_name", PARTNER_APP);
            // 组织请求参数

            HttpResponse response = HttpUtils.doPost(API_HOST, API_PATH, headers, queries,
                    params);

            String responseStr = EntityUtils.toString(response.getEntity(), Constant.ENCODING_UTF8);
            System.out.println("result: " + responseStr);
            return new ObjectMapper().readValue(responseStr, BodyGuardApiResponse.class);
//            return JSON.parseObject(result.toString().trim(), BodyGuardApiResponse.class);
        } catch (Exception e) {
            log.error("[BodyGuardApiInvoker] invoke throw exception, details: " + e);
        }
        return null;
    }

    public static void main(String[] args) {
        Map<String, String> params = new LinkedHashMap<>();
//        params.put("account_mobile", "14773439001");
//        params.put("id_number", "370404199006301915");
//        params.put("account_name", "test");
//        params.put("biz_code", "loanweb");
        params.put("company_type", "私营");
        params.put("coborrower_home_address", "浙江省杭州市西湖区古荡新村2幢201");
        params.put("career", "半专业人员");
        params.put("occupation", "见习专员");
        params.put("contact3_relation", "test");
        params.put("customer_channel", "test");
        params.put("contact5_name", "test");
        params.put("work_phone", "0571-111111111");
        params.put("surety_name", "刘能");
        params.put("contact1_id_number", "test");
        params.put("contact5_id_number", "test");
        params.put("loan_purpose", "车贷");
        params.put("coborrower_id_number", "321282555555555555");
        params.put("coborrower_phone", "0571-10101010");
        params.put("surety_phone", "0571-222222222");
        params.put("trueip_address", "test");
        params.put("token_id", "test");
        params.put("house_property", "有房");
        params.put("contact2_id_number", "test");
        params.put("diploma", "研究生");
        params.put("annual_income", "100000-200000");
        params.put("id_number", "123123123123123000");
        params.put("surety_id_number", "321282333333333333");
        params.put("card_number", "6333380402564890000");
        params.put("contact1_mobile", "test");
        params.put("account_phone", "0571-42331233");
        params.put("loan_amount", "10000");
        params.put("qq_number", "313131313");
        params.put("monthly_income", "12000以上");
        params.put("apply_province", "四川");
        params.put("surety_mobile", "15223456789");
        params.put("contact4_relation", "test");
        params.put("contact5_mobile", "test");
        params.put("loan_term", "12");
        params.put("account_mobile", "13113131313");
        params.put("organization_address", "浙江省杭州市阿里巴巴西溪园区");
        params.put("contact3_mobile", "test");
        params.put("work_time", "1年以下");
        params.put("contact3_id_number", "test");
        params.put("contact3_name", "test");
        params.put("coborrower_name", "王五");
        params.put("loan_date", "2015-11-19");
        params.put("applyer_type", "在职");
        params.put("is_cross_loan", "否");
        params.put("industry", "金融业");
        params.put("surety_company_address", "浙江省杭州市下城区潮王路18号");
        params.put("contact2_name", "test");
        params.put("resp_detail_type", "test");
        params.put("apply_city", "成都");
        params.put("account_email", "212121212@qq.com");
        params.put("surety_home_address", "浙江省杭州市西湖区古荡新村");
        params.put("home_address", "浙江省杭州市西湖区古荡新村2幢101");
        params.put("marriage", "未婚");
        params.put("account_name", "张三");
        params.put("contact5_relation", "test");
        params.put("house_type", "商品房");
        params.put("contact_address", "浙江省杭州市西湖区古荡新村2幢101");
        params.put("contact1_name", "test");
        params.put("contact4_id_number", "test");
        params.put("contact2_relation", "test");
        params.put("coborrower_mobile", "17012345678");
        params.put("apply_channel", "app申请");
        params.put("contact4_name", "test");
        params.put("ip_address", "test");
        params.put("coborrower_company_address", "杭州市江干区2号大街928号");
        params.put("graduate_school", "南京大学");
        params.put("contact1_relation", "test");
        params.put("contact4_mobile", "test");
        params.put("organization", "阿里巴巴西溪园区");
        params.put("contact2_mobile", "test");
        BodyGuardApiResponse bodyGuardApiResponse = new BodyGuardApiInvoker().invoke(params);
        System.out.println(bodyGuardApiResponse.toString());
    }
}
