package io.grx.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);


    public static HttpResponse doGet(String url, Map<String, String> headers) throws Exception {

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        return client.execute(request);

    }

    /**
     * get
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @return
     * @throws Exception
     */
    public static HttpResponse doGet(String host, String path,
                                     Map<String, String> headers,
                                     Map<String, String> querys)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpGet request = new HttpGet(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        return httpClient.execute(request);
    }


    public static HttpResponse doPost(String url,
                                      Map<String, String> headers,
                                      Map<String, String> bodys)
            throws Exception {
        HttpClient httpClient = wrapClient(url);

        HttpPost request = new HttpPost(url);
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (bodys != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

            for (String key : bodys.keySet()) {
                nameValuePairList.add(new BasicNameValuePair(key, bodys.get(key)));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, "utf-8");
//            formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            request.setEntity(formEntity);
        }

        return httpClient.execute(request);
    }

    /**
     * post form
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param bodys
     * @return
     * @throws Exception
     */
    public static HttpResponse doPost(String host, String path,
                                      Map<String, String> headers,
                                      Map<String, String> querys,
                                      Map<String, String> bodys)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (bodys != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

            for (String key : bodys.keySet()) {
                nameValuePairList.add(new BasicNameValuePair(key, bodys.get(key)));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, "utf-8");
//            formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            request.setEntity(formEntity);
        }

        return httpClient.execute(request);
    }

    /**
     * Post String
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws Exception
     */
    public static HttpResponse doPost(String host, String path,
                                      Map<String, String> headers,
                                      Map<String, String> querys,
                                      String body)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (StringUtils.isNotBlank(body)) {
            request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }

    /**
     * Post stream
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws Exception
     */
    public static HttpResponse doPost(String host, String path,
                                      Map<String, String> headers,
                                      Map<String, String> querys,
                                      byte[] body)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (body != null) {
            request.setEntity(new ByteArrayEntity(body));
        }

        return httpClient.execute(request);
    }

    /**
     * Put String
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws Exception
     */
    public static HttpResponse doPut(String host, String path,
                                     Map<String, String> headers,
                                     Map<String, String> querys,
                                     String body)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPut request = new HttpPut(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (StringUtils.isNotBlank(body)) {
            request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }

    /**
     * Put stream
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws Exception
     */
    public static HttpResponse doPut(String host, String path,
                                     Map<String, String> headers,
                                     Map<String, String> querys,
                                     byte[] body)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPut request = new HttpPut(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (body != null) {
            request.setEntity(new ByteArrayEntity(body));
        }

        return httpClient.execute(request);
    }

    /**
     * Delete
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @return
     * @throws Exception
     */
    public static HttpResponse doDelete(String host, String path,
                                        Map<String, String> headers,
                                        Map<String, String> querys)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpDelete request = new HttpDelete(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        return httpClient.execute(request);
    }

    private static String buildUrl(String host, String path, Map<String, String> querys) throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(host);
        if (!StringUtils.isBlank(path)) {
            sbUrl.append(path);
        }
        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append("&");
                }
                if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!StringUtils.isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isBlank(query.getValue())) {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
                    }
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append("?").append(sbQuery);
            }
        }

        return sbUrl.toString();
    }

    public static HttpClient wrapClient(String host) {
        HttpClient httpClient = new DefaultHttpClient();
        if (host.startsWith("https://")) {
            sslClient(httpClient);
        }

        return httpClient;
    }

    public static void sslClient(HttpClient httpClient) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String str) {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String str) {

                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry registry = ccm.getSchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Map<String, Object> postJsonData(String host, String path, Map<String, String> queryMap,
                                                   Map<String, Object> jsonMap) throws Exception {
        final String requestJson = new ObjectMapper().writeValueAsString(jsonMap);
        logger.info("request json: {}", requestJson);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        final HttpResponse response = HttpUtils.doPost(host, path,
                headers, queryMap,
                requestJson);

        final String responseJson = EntityUtils.toString(response.getEntity(), Constant.ENCODING_UTF8);
        logger.info("response json: {}", responseJson);

        return new ObjectMapper().readValue(responseJson,
                new TypeReference<Map<String, Object>>() {});
    }

    public static String readReqStr(HttpServletRequest request)
    {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
            String line = null;

            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (null != reader)
                {
                    reader.close();
                }
            } catch (IOException e)
            {

            }
        }
        return sb.toString();
    }
}