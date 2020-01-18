package io.grx.auth.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.grx.auth.service.BaiQiShiService;
import io.grx.common.utils.*;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.entity.BqsReportEntity;
import io.grx.modules.auth.entity.TjReportEntity;
import io.grx.modules.auth.enums.BaiqishiType;
import io.grx.modules.auth.enums.VerifyStatus;
import io.grx.modules.auth.service.AuthUserService;
import io.grx.modules.auth.service.BqsReportService;
import io.grx.modules.oss.cloud.CloudStorageService;
import io.grx.modules.oss.cloud.OSSFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class BaiQiShiServiceImpl implements BaiQiShiService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private BqsReportService bqsReportService;
    @Autowired
    private AuthUserService authUserService;
    @Value("${baiqishi.userPrefix}")
    private String userPrefix;
    @Value("${baiqishi.mobilePath}")
    private String mobilePath;
    @Value("${baiqishi.mobileHost}")
    private String mobileHost;
    @Value("${baiqishi.partnerId}")
    private String partnerId;
    @Value("${baiqishi.verifykey}")
    private String verifykey;
    @Value("${baiqishi.htmlUrl}")
    private String htmlUrl;
    @Value("${baiqishi.jsonUrl}")
    private String jsonUrl;
    private CloudStorageService cloudStorageService;

    @Override
    public R collectUser(final BaiqishiType baiqishiType) {
        AuthUserEntity user = ShiroUtils.getAuthUser();
        if (StringUtils.isBlank(user.getName())) {
            return R.error("请先完善基本信息");
        }
        BqsReportEntity lastReport = bqsReportService.queryLatestByUserId(user.getUserId(), baiqishiType);
        if (lastReport != null) {
            if (lastReport.getVerifyStatus() == VerifyStatus.SUBMITTED) {
                return R.error("你已提交认证，请稍后刷新查看结果");
            }

            if (lastReport.getVerifyStatus() == VerifyStatus.SUCCESS && !lastReport.isExpired()) {
                return R.error("你已认证成功，请刷新查看最新结果");
            }
        }
        JSONObject jsonObject=new JSONObject();
        String outUniqueId=UUIDGenerator.getUUID();
        jsonObject.put("outUniqueId",outUniqueId);
        jsonObject.put("userId", userPrefix + user.getUserId());
        String jsonStr = null;
        try {
            jsonStr = URLEncoder.encode(jsonObject.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String urlBase = HttpContextUtils.getRequestBaseUrl();
        String name=user.getName();
        String phone=user.getMobile();
        String idNumber=user.getIdNo();
        String backUrl=urlBase + "/auth/apply.html";
        String dataBackUrl=urlBase + "/bqs/callback/" + baiqishiType.name();
        StringBuffer url=new StringBuffer();
        url.append(mobileHost);
        url.append(mobilePath);
        url.append("?partnerId=");
        url.append(partnerId);
        url.append("&name=");
        url.append(name);
        url.append("&mobile=");
        url.append(phone);
        url.append("&certNo=");
        url.append(idNumber);
        url.append("&skip=false");
        url.append("&extraParam=");
        url.append(jsonStr);
        url.append("&customParam=");
        url.append(jsonStr);
        url.append("&backUrl=");
        url.append(backUrl);
        //url.append("&dataBackUrl=");
       // url.append(dataBackUrl);
        //创建白骑士运营商认证记录
        //BqsReportEntity bqsReportEntity = bqsReportService.queryByUniqueId(outUniqueId);

        if (lastReport == null) {
            BqsReportEntity bqsReportEntity = new BqsReportEntity();
            Long authUserId = user.getUserId();
            AuthUserEntity userEntity = authUserService.queryObject(authUserId);
            bqsReportEntity.setCreateTime(new Date());
            bqsReportEntity.setUserId(userEntity.getUserId());
            bqsReportEntity.setTaskId(outUniqueId);
            bqsReportEntity.setIdNo(userEntity.getIdNo());
            bqsReportEntity.setName(userEntity.getName());
            bqsReportEntity.setMobile(userEntity.getMobile());
            bqsReportEntity.setBaiqishiType(baiqishiType);
            bqsReportEntity.setUpdateTime(new Date());
            bqsReportEntity.setVerifyStatus(VerifyStatus.PROCESSING);
            bqsReportService.save(bqsReportEntity);
        }

        return R.ok().put("url", url.toString());
    }

    private CloudStorageService getCloudStorageService() {
        if (cloudStorageService == null) {
            synchronized (this) {
                if (cloudStorageService == null) {
                    cloudStorageService = OSSFactory.build();
                }
            }
        }
        return cloudStorageService;
    }

    /**
     * 获取咨询云运营商报告页面
     * @param bqsReportEntity
     * @return
     */
    @Override
    public String getMobileRawReport(final BqsReportEntity bqsReportEntity) {

        //1. 获取token
        //时间戳
        long timeStamp = System.currentTimeMillis();
        // 用户身份证
        String certNo = bqsReportEntity.getIdNo();
        // 用户姓名
        String name = null;
        try {
            name = URLEncoder.encode(bqsReportEntity.getName(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 用户手机号
        String mobile = bqsReportEntity.getMobile();

        System.out.println("timeStamp = " + timeStamp);
        //请求token
        String token = null;
        try {
            token = getToken(partnerId,timeStamp,certNo,verifykey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtils.isBlank(token)) {
            System.out.println("token为空.");
            return null;
        }

        //2. 拼接url

        //资信云报告前缀
        String zx_prefix = htmlUrl+"?";

        String zx_url = String.format("%spartnerId=%s&certNo=%s&name=%s&mobile=%s&timeStamp=%s&token=%s",
                zx_prefix, partnerId, certNo, name, mobile, timeStamp, token);
        System.out.println("生成资信云报告url = " + zx_url);
        String reportName =StringUtils.substringBefore(bqsReportEntity.getBaiqishiType().name().toLowerCase(), "_") + "/"
                + DateUtils.formateDate(bqsReportEntity.getCreateTime(), "yyyyMMdd")
                + "/" + bqsReportEntity.getIdNo()+".html";
        if (StringUtils.isNotBlank(zx_url)) {
            try {
                uploadHtml(zx_url, reportName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return zx_url;//存储白骑士返回的html地址，oss可以按照规则拼接 不需要保存
    }

    /**
     * 获取咨询云运营商数据报告
     * @param bqsReportEntity
     * @return
     */
    @Override
    public String getMobileRawReportJson(final BqsReportEntity bqsReportEntity) {
        // 用户身份证
        String certNo = bqsReportEntity.getIdNo();
        // 用户姓名
        String name = null;
        try {
            name = URLEncoder.encode(bqsReportEntity.getName(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 用户手机号
        String mobile = bqsReportEntity.getMobile();
        JSONObject jsonReqObject = new JSONObject();
        jsonReqObject.put("certNo", certNo);
        jsonReqObject.put("mobile", mobile);
        jsonReqObject.put("name", name);
        jsonReqObject.put("partnerId", partnerId);
        jsonReqObject.put("verifyKey", verifykey);
        String resp = null;
        try {
            resp = sendPost(true, jsonReqObject.toJSONString(), jsonUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(resp);
        String path = StringUtils.substringBefore(bqsReportEntity.getBaiqishiType().name().toLowerCase(), "_") + "/"
                + DateUtils.formateDate(bqsReportEntity.getCreateTime(), "yyyyMMdd") +"/"+certNo+ ".json";
        //上传到oss服务器
        getCloudStorageService().upload(new ByteArrayInputStream(resp.getBytes(StandardCharsets.UTF_8)), path);
        return path;
    }

    private String getToken(String partnerId, long timeStamp, String certNo, String verifyKey) throws Exception {
        String url = "https://credit.baiqishi.com/clweb/api/common/gettoken";
        JSONObject jsonReqObject = new JSONObject();
        jsonReqObject.put("partnerId", partnerId);
        jsonReqObject.put("verifyKey", verifyKey);
        jsonReqObject.put("timeStamp", timeStamp);
        jsonReqObject.put("certNo", certNo);
        String resp = sendPost(true, jsonReqObject.toJSONString(), url);
        System.out.println("请求token, 获取到结果, result = " + resp);

        if (StringUtils.isBlank(resp)) {
            System.out.println("返回结果为空.");
            return null;
        }
        JSONObject obj = JSON.parseObject(resp);
        return obj.getString("data");
    }

    public static String sendPost(boolean isSsl, String param, String url) throws Exception {
        int timeOut = 60000;
        PrintWriter out = null;
        BufferedReader in = null;
        URL targetUrl = null;
        StringBuffer result = new StringBuffer();

        try {
            if (isSsl) {
                // 信任所有证书
                targetUrl = new URL(url);
                ignoreSsl();
            } else {
                targetUrl = new URL(url);
            }

            // 打开和URL之间的连接
            HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
            // 设置超时
            httpConnection.setConnectTimeout(timeOut);
            httpConnection.setReadTimeout(timeOut);
            // 设置通用的请求属性
            httpConnection.setRequestProperty("connection", "Keep-Alive");
            httpConnection.setRequestProperty("Charset", "UTF-8");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            // 发送POST请求
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);

            out = new PrintWriter(httpConnection.getOutputStream());
            out.print(param);
            out.flush();

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        }
        //使用finally块来关闭输出流、输入流
        finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }

            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
        return result.toString();
    }

    private static void trustAllHttpsCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    /**
     * 忽略HTTPS请求的SSL证书，必须在openConnection之前调用
     *
     * @throws Exception
     */
    public static void ignoreSsl() throws Exception {
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
        trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    static class miTM implements TrustManager, X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            return;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            return;
        }
    }
    public void uploadHtml(String url,String reportName) throws Exception {
        InputStream inputStream;//接收字节输入流
        URL htmlUrl = new URL(url);
        inputStream = htmlUrl.openStream();
        getCloudStorageService().upload(inputStream,reportName);
        inputStream.close();
    }
    @Override
    public String getJsonReport(final BqsReportEntity bqsReportEntity) {
        return getReportContent(bqsReportEntity, "json");
    }

    @Override
    public String getHtmlReport(final BqsReportEntity bqsReportEntity) {
        return getReportContent(bqsReportEntity, "html");
    }

    private String getReportContent(final BqsReportEntity bqsReportEntity, String ext) {
        String path = StringUtils.substringBefore(bqsReportEntity.getBaiqishiType().name().toLowerCase(), "_") + "/"
                + DateUtils.formateDate(bqsReportEntity.getCreateTime(), "yyyyMMdd") + "/" + bqsReportEntity
                .getIdNo()+"." + ext;
        try (InputStream is = getCloudStorageService().get(path)) {
            String result = IOUtils.toString(is, Constant.ENCODING_UTF8);
            return result;
        } catch (Exception e) {
            logger.error("Failed to load report from OSS: {}", path, e);
            throw new RuntimeException("Failed to load report", e);
        }
    }

}

