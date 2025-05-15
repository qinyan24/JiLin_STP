package com.stp.service;

import com.stp.domain.ScenicSpot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ScenicSpotService {

    // 获取某个景点信息
    ScenicSpot getScenicSpotById(Long id);

    // 获取所有景点
    List<ScenicSpot> getAllSpots();

    // 添加新的景点
    void addScenicSpot(ScenicSpot scenicSpot);

    // 更新景点信息
    void updateScenicSpot(ScenicSpot scenicSpot);

    // 删除景点
    void deleteScenicSpot(Long id);

    List<ScenicSpot> getALlScenicSpotCard();


    void updateScenicSpotCoordinates();
    List<ScenicSpot> getPagedScenicSpots(int page,int size);
}
