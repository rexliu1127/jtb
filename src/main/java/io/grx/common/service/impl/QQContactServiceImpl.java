package io.grx.common.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.grx.common.service.QQContactService;
import io.grx.common.utils.CSVUtils;
import io.grx.common.utils.CacheUtils;
import io.grx.common.utils.CharUtils;
import io.grx.common.utils.CompressUtil;
import io.grx.common.utils.DateUtils;
import io.grx.common.utils.HttpUtils;
import io.grx.common.utils.R;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.oss.cloud.CloudStorageService;
import io.grx.modules.oss.cloud.OSSFactory;

@Service
public class QQContactServiceImpl implements QQContactService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    static final String host = "https://ic.qq.com";

    static final String referPath = "/pim/login.jsp";
    static final String loginPath = "/mobile_login.jsp";
    static final String captchaPath = "/pim/captcha.jsp?";
    static final String verifyPath = "/pim/safeMobileVerify.jsp";
    static final String exportPath = "/pim/contact/export_contact/export3csv.jsp";

    static final String userAgent = "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/27.0.1453.110 Safari/537.36 CoolNovo/2.0.9.19";

    @Value("${upload.path}")
    private String fileDirectory;


    private CloudStorageService cloudStorageService;

    @Autowired
    private CacheUtils cacheUtils;

    public static void main(String[] args) throws Exception {
        System.out.println(new QQContactServiceImpl().readContacts(new File("D:/2018-03-13-00-38-43-contact-99")));
    }

    @Override
    public void prepareLogin(String url) {

    }

    private void loginPage(String url) {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", userAgent);

        try {
            Map<String, String> cookieMap = cacheUtils.getObject(getCookieCacheKey(), HashMap.class);
            if (cookieMap != null) {
                String headerCookie = toCookieString(cookieMap);
                logger.info("headerCookie: {}", headerCookie);
                header.put("Cookie", headerCookie);
            } else {
                cookieMap = new HashMap<>();
            }

            HttpResponse response = HttpUtils.doGet(url, header);

            Header[] headers = response.getHeaders("Set-Cookie");
            for (Header h : headers) {
                cookieMap.putAll(splitCookieValues(h.getValue()));
            }

            logger.info("response cookie: {}", cookieMap);
            cacheUtils.putObject(getCookieCacheKey(), cookieMap, 1800);

        } catch (Exception e) {
            logger.error("Failed to go to QQ contact login page", e);
        }
    }

    @Override
    public String getCaptchaImageBase64() {
        Map<String, String> header = new HashMap<>();
        header.put("Referer", host + referPath);

        Map<String, String> querys = new HashMap<>();
        querys.put(String.valueOf(System.currentTimeMillis()), null);

        try {
            Map<String, String> cookieMap = cacheUtils.getObject(getCookieCacheKey(), HashMap.class);
            if (cookieMap != null) {
                String headerCookie = toCookieString(cookieMap);
                logger.info("headerCookie: {}", headerCookie);
                header.put("Cookie", headerCookie);
            } else {
                cookieMap = new HashMap<>();
            }

            HttpResponse response = HttpUtils.doGet(host, captchaPath, header, querys);
            byte[] bytes = IOUtils.toByteArray(response.getEntity().getContent());

            Header[] headers = response.getHeaders("Set-Cookie");
            for (Header h : headers) {
                cookieMap.putAll(splitCookieValues(h.getValue()));
            }

            logger.info("response cookie: {}", cookieMap);
            cacheUtils.putObject(getCookieCacheKey(), cookieMap, 1800);

            return Base64.encodeBase64String(bytes);
        } catch (Exception e) {
            logger.error("Failed to QQ contact captcha", e);
        }
        return null;
    }

    private Map<String, String> splitCookieValues(String cookie) {
        Map<String, String> result = new HashMap<>();
        String[] keyValues = StringUtils.split(StringUtils.trim(cookie), ";");
        for (String keyValue : keyValues) {
            result.put(StringUtils.substringBefore(keyValue, "="),
                    StringUtils.substringAfter(keyValue, "="));
        }
        return result;
    }

    private String getCookieCacheKey() {
        AuthUserEntity user = ShiroUtils.getAuthUser();
        return "qq-contact-cookie:" + user.getUserId();
    }

    private static String toCookieString(Map<String, String> cookie) {
        StringBuilder cookiesSB = new StringBuilder();

        for (Map.Entry<String, String> entry : cookie.entrySet()) {
            cookiesSB.append(entry.getKey()).append("=")
                    .append(entry.getValue()).append("; ");
        }
        return cookiesSB.toString();
    }

    @Override
    public R login1(final String mobile, final String password, final String captcha) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Origin", host);
        headers.put("Referer", host + referPath);
        headers.put("User-Agent", userAgent);

        Map<String, String> cookieMap = cacheUtils.getObject(getCookieCacheKey(), HashMap.class);
        if (cookieMap != null) {
            String headerCookie = toCookieString(cookieMap);
            logger.info("headerCookie: {}", headerCookie);
            headers.put("Cookie", headerCookie);
        } else {
            cookieMap = new HashMap<>();
        }

        Map<String, String> querys = new HashMap<>();
        querys.put(String.valueOf(System.currentTimeMillis()), null);

        Map<String, String> bodys = new HashMap<>();
        bodys.put("area", "+86");
        bodys.put("mobile", mobile);
        bodys.put("password", CharUtils.getMD5(password));
        bodys.put("verify", captcha);

        try {
            HttpResponse response = HttpUtils.doPost(host, loginPath, headers, querys, bodys);

            String html = EntityUtils.toString(response.getEntity());
            logger.debug("html {}: {}", mobile, html);

            if (StringUtils.containsIgnoreCase(html, "The URL has moved")) {
                String redirectUrl = StringUtils.substringBefore(StringUtils.substringAfter(html, "href=\""), "\">");

                loginPage(redirectUrl);

                return R.ok().put("refresh", true);
            }

            String s = StringUtils.substringAfter(html, "id=\"csrfToken\"");

            if (StringUtils.isBlank(s)) {
                return R.error(1, "登录错误, 请检查密码或验证码是否正确");
            }
            String csrfToken = StringUtils.substringAfter(
                    StringUtils.substringBefore(s, "\"/"), "value=\"");

            Header[] headerArr = response.getHeaders("Set-Cookie");
            for (Header h : headerArr) {
                cookieMap.putAll(splitCookieValues(h.getValue()));
            }

            logger.info("response cookie: {}", cookieMap);
            cacheUtils.putObject(getCookieCacheKey(), cookieMap, 1800);

            return R.ok().put("csrfToken", csrfToken);
        } catch (Exception e) {
            logger.error("login1", e);
        }

        return R.error("未知错误");
    }

    @Override
    public R sendVerifyCode(final String mobile, final String csrfToken) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Origin", host);
        headers.put("Referer", host + referPath);
        headers.put("User-Agent", userAgent);

        Map<String, String> cookieMap = cacheUtils.getObject(getCookieCacheKey(), HashMap.class);
        if (cookieMap != null) {
            headers.put("Cookie", toCookieString(cookieMap));
        } else {
            cookieMap = new HashMap<>();
        }

        Map<String, String> querys = new HashMap<>();
        querys.put(String.valueOf(System.currentTimeMillis()), null);

        Map<String, String> bodys = new HashMap<>();
        bodys.put("method", "getSmsVerifyCode");
        bodys.put("mobile", mobile);
        bodys.put("csrfToken", csrfToken);

        bodys.put("scenario", "2");

        try {
            HttpResponse response = HttpUtils.doPost(host, verifyPath, headers, querys, bodys);

            Header[] headerArr = response.getHeaders("Set-Cookie");
            for (Header h : headerArr) {
                cookieMap.putAll(splitCookieValues(h.getValue()));
            }

            cacheUtils.putObject(getCookieCacheKey(), cookieMap, 1800);

            String responseStr = EntityUtils.toString(response.getEntity());
            logger.info("get verify code response: {}", responseStr);

            int responseCode = NumberUtils.toInt(StringUtils.trim(responseStr), -1);
            if (responseCode == 0) {
                return R.ok("验证码发送成功，有效时间为5分钟，请尽快操作！")
                        .put("csrfToken", csrfToken);
            } else if (responseCode == -20005) {
                return R.error("您的请求过于频繁，请稍后再试！");
            } else if (responseCode == -20006) {
                return R.error("您的请求过于频繁，请24小时后再试！");
            } else if (responseCode == -20004) {
                return R.error("验证码输入有误，请稍后再试！");
            }

        } catch (Exception e) {
            logger.error("sendVerifyCode", e);
        }

        return R.error("系统繁忙, 请稍后再试!");
    }

    @Override
    public R login2(final String mobile, String verifyCode, final String csrfToken) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Origin", host);
        headers.put("Referer", host + referPath);
        headers.put("User-Agent", userAgent);

        Map<String, String> cookieMap = cacheUtils.getObject(getCookieCacheKey(), HashMap.class);
        if (cookieMap != null) {
            headers.put("Cookie", toCookieString(cookieMap));
        } else {
            cookieMap = new HashMap<>();
        }

        Map<String, String> querys = new HashMap<>();
        querys.put(String.valueOf(System.currentTimeMillis()), null);

        Map<String, String> bodys = new HashMap<>();
        bodys.put("method", "verify");
        bodys.put("mobile", mobile);
        bodys.put("csrfToken", csrfToken);
        bodys.put("verifyCode", verifyCode);

        try {
            HttpResponse response = HttpUtils.doPost(host, verifyPath, headers, querys, bodys);

            Header[] headerArr = response.getHeaders("Set-Cookie");
            for (Header h : headerArr) {
                cookieMap.putAll(splitCookieValues(h.getValue()));
            }

            cacheUtils.putObject(getCookieCacheKey(), cookieMap, 1800);

            String responseStr = EntityUtils.toString(response.getEntity());
            logger.info("login2 response: {}", responseStr);

            int responseCode = NumberUtils.toInt(StringUtils.trim(responseStr), -1);
            if (responseCode == 0) {
                return R.ok();
            } else if (responseCode == -20004) {
                return R.error("验证码输入有误或已过期，请稍后再试！");
            }

        } catch (Exception e) {
            logger.error("login2", e);
        }

        return R.error("验证失败，请稍后再试！");
    }

    @Override
    public Map<String, String> downloadContacts(final String mobile) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Origin", host);
        headers.put("Referer", host + referPath);
        headers.put("User-Agent", userAgent);

        Map<String, String> cookieMap =cacheUtils.getObject(getCookieCacheKey(), HashMap.class);
        if (cookieMap != null) {
            headers.put("Cookie", toCookieString(cookieMap));
        }

        Map<String, String> querys = new HashMap<>();
        querys.put("t", String.valueOf(System.currentTimeMillis()));
        querys.put("groupId", "-1");
        querys.put("type", "csv");
        querys.put("id", "2");

        Map<String, String> result = new HashMap<>();
        File zipFile = null;
        File[] unzipFiles = null;
        try {
            HttpResponse response = HttpUtils.doGet(host, exportPath, headers, querys);

            AuthUserEntity userEntity = ShiroUtils.getAuthUser();
            String pathPostfix =  mobile + "_"
                    + System.currentTimeMillis() + ".zip";
            String zipFileName = fileDirectory + "/contact/" + pathPostfix;

            zipFile = new File(zipFileName);

            logger.debug("Going to write contact zip file for {}, {}", mobile, zipFileName);
            try (OutputStream os = FileUtils.openOutputStream(zipFile)) {
                IOUtils.copy(response.getEntity().getContent(), os);
            }

            String path = "qqcontact/" + DateUtils
                    .formateDate(new Date(), "yyyyMMdd") + "/" + pathPostfix;
            try (InputStream is = new FileInputStream(zipFile)) {
                getCloudStorageService().upload(is, path);
            }

            unzipFiles = CompressUtil.unzip(zipFileName, fileDirectory + "/contact/" + userEntity.getUserId(),
                    mobile);
            if (unzipFiles.length == 0) {
                return result;
            }

            return readContacts(unzipFiles[0]);
        } catch (Exception e) {
            logger.error("downloadContacts", e);
        } finally {
            if (zipFile != null) {
                zipFile.delete();
                logger.info("deleted {}", zipFile.getAbsolutePath());
            }

            if (unzipFiles != null) {
                for (File f : unzipFiles) {
                    f.delete();
                    logger.info("deleted {}", f.getAbsolutePath());
                }
            }
        }

        return null;
    }

    private Map<String, String> readContacts(File contactFile) throws Exception {
        Map<String, String> result = new HashMap<>();
        BufferedReader b = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(contactFile), "GBK"));

        String header = b.readLine();
        List<String> headers = CSVUtils.parseLine(header);

        int firstNameIdx = -1;
        int lastNameIdx = -1;
        List<Integer> phoneIdx = new ArrayList<>();
        for (int i = 0; i < headers.size(); i++) {
            String h = headers.get(i);
            if (firstNameIdx == -1 && StringUtils.equalsIgnoreCase(h, "姓")) {
                firstNameIdx = i;
            } else if (lastNameIdx == -1 && StringUtils.equalsIgnoreCase(h, "名")) {
                lastNameIdx = i;
            } else if (StringUtils.contains(h, "电话")) {
                phoneIdx.add(i);
            }
        }

        String line;
        while ((line = b.readLine()) != null) {
            List<String> values = CSVUtils.parseLine(line);
            String name = "";
            if (firstNameIdx > -1 && lastNameIdx < values.size()) {
                name = name + StringUtils.defaultString(values.get(firstNameIdx));
            }
            if (lastNameIdx > -1 && lastNameIdx < values.size()) {
                name += StringUtils.defaultString(values.get(lastNameIdx));
            }

            for (int i : phoneIdx) {
                if (i > values.size() -1) {
                    continue;
                }
                String value = StringUtils.trim(values.get(i));
                if (StringUtils.isNotBlank(value)) {
                    value = StringUtils.replace(value, "+86", "");
                    value = StringUtils.replace(value, " ", "");
                    value = StringUtils.replace(value, "-", "");
                    result.put(value, name);
                }
            }
        }
        return result;
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
}
