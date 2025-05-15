package com.stp.service.impl;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {
    private static final String API_KEY = "b0ed499a098b16b670665cef7beaf207";
    private static final String API_URL = "...";

    public Map<String, Object> getWeatherData(String city) {
        String url = String.format(API_URL, city, API_KEY);
        // 调用外部API并返回天气数据
        // 这里简化了API调用，实际中你可以使用HttpClient等工具来发起请求
        Map<String, Object> result = new HashMap<>();
        result.put("temp", 25);
        result.put("humidity", 60);
        result.put("wind_speed", 5);
        result.put("description", "晴");
        return result;
    }
}
