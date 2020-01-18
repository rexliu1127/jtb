package io.grx.wx.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.stereotype.Component;

@Component
public class RestUtils {

    public String get(String url, RequestEntity entity) throws IOException {
        HttpClient httpClient = new HttpClient();
        HttpClientParams httparams = new HttpClientParams();
        httpClient.setParams(httparams);

        PostMethod method = new PostMethod(url);
        if (entity != null) {
            method.setRequestEntity(entity);
        }
        String responseBody = null;
        try {
            method.getParams().setContentCharset("utf-8");
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
            int statusCode = httpClient.executeMethod(method);
            if (statusCode != org.apache.commons.httpclient.HttpStatus.SC_OK) {
                System.out.println("\r\nMethod failed: " + method.getStatusLine() + ",url:\r\n" + url + "\r\n");
            }
            StringBuilder resultBuffer = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(),
                    method.getResponseCharSet()));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                resultBuffer.append(inputLine);
                resultBuffer.append("\r\n");
            }
            in.close();
            responseBody = resultBuffer.toString().trim();
        } catch (Exception e) {
            System.out.println(">>> http请求异常，url=" + url);
            e.printStackTrace();
            responseBody = "-2";
        } finally {
            if (method != null) {
                method.releaseConnection();
                method = null;
            }
            return responseBody;
        }

    }
}
