package io.grx.common.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.grx.common.service.GeoService;
import io.grx.common.utils.Constant;
import io.grx.common.utils.HttpUtils;

@Service
public class QqMapService implements GeoService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${qqmap.host}")
    private String host;
    @Value("${qqmap.geocoderPath}")
    private String geocoderPath;
    @Value("${qqmap.key}")
    private String key;

    @Override
    public String getAddress(final String lat, final String lng) {
        Map<String, String> querys = new HashMap<>();
        querys.put("location", lat + "," + lng);
        querys.put("key", key);
        querys.put("policy", "2");

        String addr = "";
        try {
            HttpResponse httpResponse = HttpUtils.doGet(host, geocoderPath, MapUtils.EMPTY_MAP, querys);

            String responseStr = EntityUtils.toString(httpResponse.getEntity(), Constant.ENCODING_UTF8);
            logger.debug("result: {}", responseStr);

            JSONObject responseJson = new JSONObject(responseStr);
            if (responseJson.getInt("status") == 0) {
                JSONObject resultJson = responseJson.getJSONObject("result");
                addr = resultJson.getString("address");

            }
        } catch (Exception e) {
            logger.error("Failed to get address", e);
        }

        logger.info("lat={}, lng={}, address={}", lat, lng, addr);
        return addr;
    }

    public static void main(String[] args) throws Exception {
        QqMapService service = new QqMapService();
        service.host = "http://apis.map.qq.com";
        service.geocoderPath = "/ws/geocoder/v1/";
        service.key = "GAMBZ-LL2CJ-V36FC-";

        System.out.println(service.getAddress("22.568916", "114.13245"));
    }
}
