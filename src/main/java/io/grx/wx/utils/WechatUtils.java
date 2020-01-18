package io.grx.wx.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import io.grx.common.utils.CacheUtils;
import io.grx.common.utils.Constant;
import io.grx.common.utils.HttpContextUtils;
import io.grx.common.utils.HttpUtils;

@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatUtils {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CacheUtils cacheUtils;

    private static final String WECHAT_PATH_USER_INFO = "/sns/userinfo";

    private static final String WECHAT_PATH_SEND_TEMPLATE_MSG = "/cgi-bin/message/template/send";

    private static final int RETRY_COUNT = 5;

    private static final int TOKEN_EXPIRE_TIME = 7200 - 900; // 2小时(减15分钟)

    private String wechatHost;
    private String appId;
    private String appSecret;
    private String callbackHost;
    private String keywordColor;

    public String buildLoginUri(String returnUrl, String stateId) {
        try {
            String serverCallback = HttpContextUtils.getRequestBaseUrl() + "/rcpt/wx_callback.html";
            if (StringUtils.isNotBlank(returnUrl)) {
                serverCallback = serverCallback + "?returnUrl=" + URLEncoder.encode(returnUrl, Constant.ENCODING_UTF8);
            }
            final String encodedServerCallback = URLEncoder.encode(serverCallback, Constant.ENCODING_UTF8);
            return "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
                        + appId
                        + "&redirect_uri=" + encodedServerCallback
                        + "&response_type=code&scope=snsapi_userinfo&state="
                        + stateId + "#wechat_redirect";
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String buildScanPayLoginUri(String stateId, String returnUrl, String successUrl) {
        try {
            String serverCallback = HttpContextUtils.getRequestBaseUrl() + "/wx_pay_login_callback";
            if (StringUtils.isNotBlank(returnUrl)) {
                serverCallback += "?" + URLEncoder.encode(returnUrl, Constant.ENCODING_UTF8);
            }

            if (StringUtils.isNotBlank(successUrl)) {
                serverCallback += "&successUrl=" + URLEncoder.encode(successUrl, Constant.ENCODING_UTF8);
            }

            final String encodedServerCallback = URLEncoder.encode(serverCallback, Constant.ENCODING_UTF8);
            return "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
                    + appId
                    + "&redirect_uri=" + encodedServerCallback
                    + "&response_type=code&scope=snsapi_base&state="
                    + stateId + "#wechat_redirect";
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String buildAuthUri(String code) {
        return wechatHost + "/sns/oauth2/access_token?" +
                "appid=" + appId +
                "&secret=" + appSecret +
                "&grant_type=authorization_code&code=" + code;
    }

    public String buildUserInfoUrl(String accessToken, String openId) {
        return wechatHost + WECHAT_PATH_USER_INFO + "?access_token=" + accessToken + "&openid=" + openId;
    }

    public String getAccessToken(boolean refresh) {
        String cacheKey = "token:" + appId;
        String cacheTimeKey = "tokenTime:" + appId;

        String token = null;
        long lastTime = NumberUtils.toLong(cacheUtils.get(cacheTimeKey), -1);
        if (lastTime > 0 && System.currentTimeMillis() - lastTime < TOKEN_EXPIRE_TIME * 1000) {
            token = cacheUtils.get(cacheKey);
            if (!refresh && StringUtils.isNotBlank(token)) {
                return token;
            }
        }

        String path = String.format("/cgi-bin/token?grant_type=client_credential" +
                "&appid=%1$s&secret=%2$s", appId, appSecret);

        for (int i = 0; i < RETRY_COUNT; i++) {
            try {

                HttpResponse response = HttpUtils.doGet(wechatHost, path, new HashMap<>(), new HashMap<>());
                JSONObject responseJson = new JSONObject(EntityUtils.toString(response.getEntity(), Constant.ENCODING_UTF8));

                if (responseJson.has("access_token")) {
                    token = responseJson.getString("access_token");
                }

                if (StringUtils.isNotBlank(token)) {
                    break;
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to get token", e);
            }
        }

        if (StringUtils.isNotBlank(token)) {
            cacheUtils.put(cacheTimeKey, String.valueOf(System.currentTimeMillis()), TOKEN_EXPIRE_TIME);
            cacheUtils.put(cacheKey, token, TOKEN_EXPIRE_TIME);
        }
        return token;
    }


    public String getJsApiTicket() {
        String token = getAccessToken(false);

        String cacheKey = "ticket:" + token;
        String cacheTimeKey = "ticketTime:" + token;

        String ticket = null;
        long lastTime = NumberUtils.toLong(cacheUtils.get(cacheTimeKey), -1);
        if (lastTime > 0 && System.currentTimeMillis() - lastTime < TOKEN_EXPIRE_TIME * 1000) {
            ticket = cacheUtils.get(cacheKey);
            if (StringUtils.isNotBlank(ticket)) {
                return ticket;
            }
        }


        for (int i = 0; i < RETRY_COUNT; i++) {
            try {
                String path = String.format("/cgi-bin/ticket/getticket?access_token=%1$s&type=jsapi", token);

                HttpResponse response = HttpUtils.doGet(wechatHost, path, new HashMap<>(), new HashMap<>());
                JSONObject responseJson = new JSONObject(EntityUtils.toString(response.getEntity(), Constant.ENCODING_UTF8));

                int returnCode = NumberUtils.toInt(responseJson.getString("errcode"), -1);
                if (returnCode != 0) {
                    token = getAccessToken(true);

                    continue;
                }

                if (responseJson.has("ticket")) {
                    ticket = responseJson.getString("ticket");
                    break;
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to get token", e);
            }
        }

        if (StringUtils.isNotBlank(ticket)) {
            cacheUtils.put(cacheTimeKey, String.valueOf(System.currentTimeMillis()), TOKEN_EXPIRE_TIME);
            cacheUtils.put(cacheKey, ticket, TOKEN_EXPIRE_TIME);
        }
        return ticket;
    }

    public boolean sendTemplateMessage(String openId, String templateId, String url,
                                       Map<String, String> params) {
        if (StringUtils.isBlank(openId)) {
            return false;
        }

        String token = getAccessToken(false);

        for (int i = 0; i < RETRY_COUNT; i++) {
            Map<String, String> queryMap = new HashMap<>();
            queryMap.put("access_token", token);

            Map<String, Object> jsonMap = new LinkedHashMap<>();
            jsonMap.put("touser", openId);
            jsonMap.put("template_id", templateId);
            jsonMap.put("url", url);

            Map<String, Object> dataMap = new LinkedHashMap<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (StringUtils.isNotBlank(entry.getValue())) {
                    Map<String, String> valueMap = new LinkedHashMap<>();
                    valueMap.put("value", entry.getValue());
                    if (StringUtils.isNotBlank(keywordColor)) {
                        valueMap.put("color", keywordColor);
                    }
                    dataMap.put(entry.getKey(), valueMap);
                }
            }

            jsonMap.put("data", dataMap);

            try {
                Map<String, Object> responseMap = HttpUtils.postJsonData(wechatHost, WECHAT_PATH_SEND_TEMPLATE_MSG,
                        queryMap, jsonMap);

                int returnCode = MapUtils.getIntValue(responseMap, "errcode", -1);
                if (returnCode == 0) {
                    return true;
                }

                if (returnCode == 42001) {
                    token = getAccessToken(true);
                }
            } catch (Exception e) {
                logger.error("Failed to send template message", e);
            }
        }

        return false;
    }

    public Map<String, String> sign( String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String jsapi_ticket = getJsApiTicket();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";
        // 注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str
                + "&timestamp=" + timestamp + "&url=" + url;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes(Constant.ENCODING_UTF8));
            signature = byteToHex(crypt.digest());
        } catch (Exception e) {
            logger.warn("Failed to create wecat signature", e);
        }

        ret.put("url", url);
        //注意这里 要加上自己的appId
        ret.put("appId", appId);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public InputStream getFileInputStream(String mediaId) {
        InputStream is = null;
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="+ getAccessToken(false) + "&media_id=" + mediaId;
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            // 获取文件转化为byte流
            is = http.getInputStream();
        } catch (Exception e) {
            logger.error("Failed to convert inputStream from weixin server,,mediaId:{}",mediaId);
        }
        return is;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }
    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(final String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(final String appSecret) {
        this.appSecret = appSecret;
    }

    public String getWechatHost() {
        return wechatHost;
    }

    public void setWechatHost(final String wechatHost) {
        this.wechatHost = wechatHost;
    }

    public String getCallbackHost() {
        return callbackHost;
    }

    public void setCallbackHost(final String callbackHost) {
        this.callbackHost = callbackHost;
    }

    public String getKeywordColor() {
        return keywordColor;
    }

    public void setKeywordColor(final String keywordColor) {
        this.keywordColor = keywordColor;
    }
}
