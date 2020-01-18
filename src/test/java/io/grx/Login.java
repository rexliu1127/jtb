package io.grx;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import io.grx.common.utils.CSVUtils;
import io.grx.common.utils.CompressUtil;
import io.grx.common.utils.HttpUtils;


public class Login {
    public static DefaultHttpClient httpclient = new DefaultHttpClient();
    public static String login_page_url = "https://ic.qq.com/pim/login.jsp";
    public static String login_url = "https://ic.qq.com/mobile_login.jsp";
    public static String img_path_url = "https://ic.qq.com/pim/captcha.jsp?";
    public static String sendCodeUrl = "https://ic.qq.com/pim/safeMobileVerify.jsp?";
    public static String image_save_path = "d://vcode.png";


    static final String host = "https://ic.qq.com";

    static final String referPath = "/pim/login.jsp";
    static final String loginPath = "/mobile_login.jsp";
    static final String captchaPath = "/pim/captcha.jsp?";
    static final String verifyPath = "/pim/safeMobileVerify.jsp";
    static final String exportPath = "/pim/contact/export_contact/export3csv.jsp";

    static final String userAgent = "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/27.0.1453.110 Safari/537.36 CoolNovo/2.0.9.19";

    public static void main(String[] args) throws Exception {
        HttpUtils.sslClient(httpclient);

//        Map<String, String> cookie = new HashMap<>();
////
////        String cookie = firstCookie();
//
////        System.out.println("first cookie: " + cookie);
//
//        imgCookie(cookie);
////
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//
//        System.out.println("Enter captcha code:");
//        String captcha = br.readLine();
////
//////        String code = identifyImg();
//////
//        String token = loginCookie(captcha, cookie);
////
//        System.out.println("token: " + token);
//        System.out.println("login cookie2 " + cookie);
//
//        String json = login1(token,
//                cookie);
//
//        System.out.println("json= " + json);
//
//
//        System.out.println("Enter sendVerifyCode code:");
//        String verifyCode = br.readLine();
//
//        String response = realLogin(token, verifyCode,
//                cookie);
////
//        System.out.println("response= " + response);
//
//        System.out.println("cookie= " + toCookieString(cookie));
//
//
//        String go = br.readLine();

//        downloadZip();

        Map<String, String> contacts =
        downloadContacts("13631235365",
                "t_user_agent=293CEF46B5B6314504B2C3EC2DFEBA55; pim_mobile=+8613631235365;  path=/; " +
                        "bid_uid=-596353552;  expires=Tue, 20-Mar-2018 13:52:34 GMT; skey=@123456789; SESSION_JSESSIONID=b15217ba-9174-495a-9169-feb7e2356df0;  domain=.qq.com;");

        System.out.println(contacts);

    }
        // <span></span> }

    public static void imgCookie(Map<String, String> cookie) {

        BufferedReader in = null;

        try {
            httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
            HttpGet httpGet = new HttpGet(img_path_url + new Date().getTime());
            httpGet.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);

//            httpGet.setHeader("Cookie", cookie);
            httpGet.setHeader("Referer", "https://ic.qq.com/pim/login.jsp");

            HttpResponse response = httpclient.execute(httpGet);
            //保存图片
            download(response.getEntity().getContent(), image_save_path);
            List<Cookie> cookies = httpclient.getCookieStore().getCookies();
            httpGet.releaseConnection();
            StringBuilder cookiesSB = new StringBuilder();
            System.out.println("第一次cookie");
            if (cookies.isEmpty()) {
                System.out.println("None");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    cookie.put(cookies.get(i).getName(), cookies.get(i).getValue());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static String downloadZip(String cookie) {

        BufferedReader in = null;

        try {
            httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
            HttpGet httpGet = new HttpGet("https://ic.qq.com/pim/contact/export_contact/export3csv.jsp?groupId=-1&type=csv&id=2&t=?"
                + new Date().getTime());
            httpGet.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);

            httpGet.setHeader("Cookie", cookie);
            httpGet.setHeader("Referer", "https://ic.qq.com/pim/login.jsp");
            httpGet.setHeader("Origin", "https://ic.qq.com");
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.110 Safari/537.36 CoolNovo/2.0.9.19");

            HttpResponse response = httpclient.execute(httpGet);
//            String str = EntityUtils.toString(response.getEntity());

//            System.out.println(str);
            //保存图片
//            download(response.getEntity().getContent(), "D:/test2.zip");

            IOUtils.copy(response.getEntity().getContent(), FileUtils.openOutputStream(new File("D:/test3.zip")));

            httpGet.releaseConnection();
            return "";
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static String identifyImg() {
        return "";
    }

    private static String toCookieString(Map<String, String> cookie) {
        StringBuilder cookiesSB = new StringBuilder();

        for (Map.Entry<String, String> entry : cookie.entrySet()) {
            cookiesSB.append(entry.getKey()).append("=")
                    .append(entry.getValue()).append("; ");
        }
        return cookiesSB.toString();
    }

    public static String loginCookie(String code, Map<String, String> cookie) {
        try {
            httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
            // 2  用户登录
            HttpPost httppost = new HttpPost(login_url);
            httppost.setHeader("Cookie", toCookieString(cookie));
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("area", "+86"));
            nvps.add(new BasicNameValuePair("mobile", "13631235365"));
            nvps.add(new BasicNameValuePair("password", "8e944f0b60ece6cf271f37475d3f4be9"));
            nvps.add(new BasicNameValuePair("verify", code));

            httppost.setHeader("Cookie", toCookieString(cookie));
            httppost.setHeader("Origin", "https://ic.qq.com");
            httppost.setHeader("Referer", "https://ic.qq.com/pim/login.jsp");
            httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.110 Safari/537.36 CoolNovo/2.0.9.19");

            httppost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            httppost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
            HttpResponse response = httpclient.execute(httppost);

            System.out.println("Login form get: " + response.getStatusLine().getStatusCode());

            String html = EntityUtils.toString(response.getEntity());

            System.out.println("html: " + html);

            String s = StringUtils.substringAfter(html, "id=\"csrfToken\"");
            String csrfToken = StringUtils.substringAfter(
                    StringUtils.substringBefore(s, "\"/"), "value=\"");
            System.out.println("csrfToken=" + csrfToken);


            //  System.out.println(sb.toString());

            List<Cookie> cookies = httpclient.getCookieStore().getCookies();
            httppost.releaseConnection();
            StringBuilder cookiesSB = new StringBuilder();
            if (cookies.isEmpty()) {
                System.out.println("None");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    cookie.put(cookies.get(i).getName(), cookies.get(i).getValue());
                    // System.out.println("- " + cookies.get(i).toString());
//                    cookiesSB.append(cookies.get(i).getName()).append("=")
//                            .append(cookies.get(i).getValue()).append("; ");
                }
            }

            httppost.releaseConnection();

            return csrfToken;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getVerifyCode(String csrfToken, Map<String, String> cookie) {
        try {
            httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
            // 2  用户登录
            HttpPost httppost = new HttpPost(sendCodeUrl + new Date().getTime());
            httppost.setHeader("Cookie", toCookieString(cookie));
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("method", "getSmsVerifyCode"));
            nvps.add(new BasicNameValuePair("mobile", "13631235365"));
            nvps.add(new BasicNameValuePair("csrfToken", csrfToken));
            nvps.add(new BasicNameValuePair("scenario", "2"));

            httppost.setHeader("Origin", "https://ic.qq.com");
            httppost.setHeader("Referer", "https://ic.qq.com/pim/login.jsp");
            httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.110 Safari/537.36 CoolNovo/2.0.9.19");

            httppost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            httppost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
            HttpResponse response = httpclient.execute(httppost);

            List<Cookie> cookies = httpclient.getCookieStore().getCookies();
            httppost.releaseConnection();
            if (cookies.isEmpty()) {
                System.out.println("None");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    cookie.put(cookies.get(i).getName(), cookies.get(i).getValue());
                    // System.out.println("- " + cookies.get(i).toString());
//                    cookiesSB.append(cookies.get(i).getName()).append("=")
//                            .append(cookies.get(i).getValue()).append("; ");
                }
            }

            System.out.println("Login form get: " + response.getStatusLine().getStatusCode());

            String responseContent = EntityUtils.toString(response.getEntity());

            return responseContent;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String realLogin(String csrfToken, String verifyCode, Map<String, String> cookie) {
        try {
            httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
            // 2  用户登录
            HttpPost httppost = new HttpPost(sendCodeUrl + new Date().getTime());
            httppost.setHeader("Cookie", toCookieString(cookie));
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("method", "verify"));
            nvps.add(new BasicNameValuePair("mobile", "13631235365"));
            nvps.add(new BasicNameValuePair("verifyCode", verifyCode));
            nvps.add(new BasicNameValuePair("csrfToken", csrfToken));

            httppost.setHeader("Origin", "https://ic.qq.com");
            httppost.setHeader("Referer", "https://ic.qq.com/pim/login.jsp");
            httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.110 Safari/537.36 CoolNovo/2.0.9.19");

            httppost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            httppost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
            HttpResponse response = httpclient.execute(httppost);

            System.out.println("Login form get: " + response.getStatusLine().getStatusCode());

            List<Cookie> cookies = httpclient.getCookieStore().getCookies();
            httppost.releaseConnection();
            StringBuilder cookiesSB = new StringBuilder();
            System.out.println("real login cookie");
            if (cookies.isEmpty()) {
                System.out.println("None");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    cookie.put(cookies.get(i).getName(), cookies.get(i).getValue());
                }
            }

            System.out.println(cookiesSB.toString());

            String responseContent = EntityUtils.toString(response.getEntity());

            return responseContent;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String rend(String cookie) {
        BufferedReader in = null;

        try {
            // 1 获取 _tb_token_
            httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
            HttpGet httpGet = new HttpGet("http://www.400gb.com/index.php");
            httpGet.setHeader("Cookie", cookie);
            httpGet.setHeader("Host", "www.400gb.com");
            httpGet.setHeader("Referer", "http://www.400gb.com/index.php");
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.110 Safari/537.36 CoolNovo/2.0.9.19");

            httpGet.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
            HttpResponse response = httpclient.execute(httpGet);


            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
            StringBuilder sb = new StringBuilder();
            String s = "";
            while ((s = in.readLine()) != null) {
                sb.append(s.trim()).append("\n");
            }

            System.out.println(sb.toString());

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }


    public static boolean download(InputStream in, String path) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            byte b[] = new byte[1024];
            int j = 0;
            while ((j = in.read(b)) != -1) {
                out.write(b, 0, j);
            }
            out.flush();
            File file = new File(path);
            if (file.exists() && file.length() == 0)
                return false;
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            if ("FileNotFoundException".equals(e.getClass().getSimpleName()))
                System.err.println("download FileNotFoundException");
            if ("SocketTimeoutException".equals(e.getClass().getSimpleName()))
                System.err.println("download SocketTimeoutException");
            else
                e.printStackTrace();
        } finally {

            if (out != null)
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return false;
    }

    /**
     * 采集
     *
     * @param url：指定URL
     * @param times：如果采集失败，采集最少次数（2次）
     * @return
     */
    public static boolean download(String urlstr, String path) {
        if (urlstr == null || "".equals(urlstr.trim()))
            return false;


        InputStream in = null;
        FileOutputStream out = null;
        try {
            System.out.println("download url " + urlstr);
            URL url = new URL(urlstr);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);//jdk 1.5换成这个,连接超时
            //connection.setReadTimeout(5000);//jdk 1.5换成这个,读操作超时
            connection.setDoOutput(true);

            out = new FileOutputStream(path);
            in = connection.getInputStream();
            byte b[] = new byte[1024];
            int j = 0;
            while ((j = in.read(b)) != -1) {
                out.write(b, 0, j);
            }
            out.flush();
            File file = new File(path);
            if (file.exists() && file.length() == 0)
                return false;
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            if ("FileNotFoundException".equals(e.getClass().getSimpleName()))
                System.err.println("download FileNotFoundException");
            if ("SocketTimeoutException".equals(e.getClass().getSimpleName()))
                System.err.println("download SocketTimeoutException");
            else
                e.printStackTrace();
        } finally {

            if (out != null)
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return false;
    }

    public static InputStream getUrlImg(String URLName) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int HttpResult = 0; // 服务器返回的状态
        URL url = new URL(URLName); // 创建URL
        URLConnection urlconn = url.openConnection(); // 试图连接并取得返回状态码urlconn.connect();
        HttpURLConnection httpconn = (HttpURLConnection) urlconn;
        HttpResult = httpconn.getResponseCode();
        System.out.println(HttpResult);
        if (HttpResult != HttpURLConnection.HTTP_OK) { // 不等于HTTP_OK说明连接不成功
            System.out.print("连接失败！");
        } else {
            int filesize = urlconn.getContentLength(); // 取数据长度
            System.out.println(filesize);
            BufferedInputStream bis = new BufferedInputStream(
                    urlconn.getInputStream());
            BufferedOutputStream bos = new BufferedOutputStream(os);
            byte[] buffer = new byte[1024]; // 创建存放输入流的缓冲
            int num = -1; // 读入的字节数
            while (true) {
                num = bis.read(buffer); // 读入到缓冲区
                if (num == -1) {
                    bos.flush();
                    break; // 已经读完
                }
                bos.flush();
                bos.write(buffer, 0, num);
            }
            bos.close();
            bis.close();
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(os.toByteArray());
        return bis;
    }


    public static Map<String, String> downloadContacts(final String mobile, String cookie) {

//        HttpGet httpGet = new HttpGet("https://ic.qq.com/pim/contact/export_contact/export3csv.jsp?groupId=-1&type=csv&id=2&t=?"
//                + new Date().getTime());
        Map<String, String> headers = new HashMap<>();
        headers.put("Origin", host);
        headers.put("Referer", host + referPath);
        headers.put("User-Agent", userAgent);

        headers.put("Cookie", cookie);

        Map<String, String> querys = new HashMap<>();
        querys.put("t", String.valueOf(System.currentTimeMillis()));
        querys.put("groupId", "-1");
        querys.put("type", "csv");
        querys.put("id", "2");

        Map<String, String> result = new HashMap<>();
        try {
            String fileDirectory = "D:/";
            HttpResponse response = HttpUtils.doGet(host, exportPath, headers, querys);

            String zipFileName = fileDirectory + "/contact/1_" + mobile + ".zip";

            IOUtils.copy(response.getEntity().getContent(), FileUtils.openOutputStream(new File(zipFileName)));

            File[] files = CompressUtil.unzip(zipFileName, fileDirectory + "/contact/" + 1,
                    mobile);

            System.out.println("files: " + files.length);
            if (files.length == 0) {
                return result;
            }

            return readContacts(files[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Map<String, String> readContacts(File contactFile) throws Exception {
        Map<String, String> result = new HashMap<>();
        BufferedReader b = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(contactFile), "GBK"));

        String header = b.readLine();
        List<String> headers = CSVUtils.parseLine(header);

        System.out.println("header: " + header);

        int firstNameIdx = -1;
        int lastNameIdx = -1;
        List<Integer> phoneIdx = new ArrayList<>();
        for (int i = 0; i < headers.size(); i++) {

            String h = headers.get(i);
            System.out.println(h);
            if (firstNameIdx == -1 && StringUtils.equalsIgnoreCase(h, "姓")) {
                firstNameIdx = i;
            } else if (lastNameIdx == -1 && StringUtils.equalsIgnoreCase(h, "名")) {
                lastNameIdx = i;
            } else if (StringUtils.contains(h, "电话")) {
                phoneIdx.add(i);
            }
        }

        System.out.println("phoneIdx: " + phoneIdx);
        System.out.println("Reading file using Buffered Reader");

        String line;
        while ((line = b.readLine()) != null) {
            List<String> values = CSVUtils.parseLine(line);

            System.out.println(values);
            String name = "";
            if (firstNameIdx > -1) {
                name = name + StringUtils.defaultString(values.get(firstNameIdx));
            }
            if (lastNameIdx > -1) {
                name += StringUtils.defaultString(values.get(lastNameIdx));
            }

            for (int i : phoneIdx) {
                if (i > values.size() -1) {
                    continue;
                }
                String value = StringUtils.trim(values.get(i));
                System.out.println("value=" + value);
                if (StringUtils.isNotBlank(value)) {
                    result.put(value, name);
                }
            }
        }
        return result;
    }
}
