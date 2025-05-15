package com.stp.service.impl;


import com.stp.dao.ScenicSpotDao;
import com.stp.domain.ScenicSpot;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
public class BaiduMapService {
    private static final String BAIDU_PLACE_API = "https://api.map.baidu.com/place/v2/search";
    private static final String BAIDU_MAP_AK = "zFa4GBOzyyoYSu9ePlMjle1eNAezdrtP";  // 你的百度地图 API Key

    @Autowired
    private ScenicSpotDao scenicSpotDao;

    // 获取所有景点并更新经纬度
    public void updateScenicSpotCoordinates() {
        List<ScenicSpot> spots = scenicSpotDao.selectAllSpots();

        for (ScenicSpot spot : spots) {
            if (spot.getLatitude() != null || spot.getLongitude() != null) {
                String address = "吉林省" + spot.getName();
                double[] coordinates = getCoordinatesFromBaidu(address);

                if (coordinates != null) {
                    spot.setLongitude(coordinates[0]);
                    spot.setLatitude(coordinates[1]);
                    scenicSpotDao.updateScenicSpotCoordinates(spot);  // 更新数据库
                    System.out.println("更新景点：" + spot.getName() + " -> 经度：" + coordinates[0] + "，纬度：" + coordinates[1]);
                }
            }
        }
    }

    // 通过百度 API 获取经纬度
    private double[] getCoordinatesFromBaidu(String address) {
        OkHttpClient client = new OkHttpClient();
        String url = BAIDU_PLACE_API + "?address=" + address + "&output=json&ak=" + BAIDU_MAP_AK;

        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JSONObject json = new JSONObject(responseBody);  // 使用 org.json.JSONObject

                if (json.getInt("status") == 0) {  // API 返回成功
                    JSONObject location = json.getJSONObject("result").getJSONObject("location");
                    return new double[]{location.getDouble("lng"), location.getDouble("lat")};
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;  // 失败返回 null
    }
}
