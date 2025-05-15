package com.stp.service.impl;

import com.stp.domain.ScenicSpot;
import com.stp.service.ScenicSpotService;
import com.stp.dao.ScenicSpotDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScenicSpotServiceImpl implements ScenicSpotService {

    @Autowired
    private ScenicSpotDao scenicSpotDao;

    @Autowired
    private BaiduMapService baiduMapService;

    @Override
    public ScenicSpot getScenicSpotById(Long id) {
        return scenicSpotDao.selectScenicSpotById(id);
    }

    @Override
    public List<ScenicSpot> getAllSpots() {
        return scenicSpotDao.selectAllSpots();
    }

    @Override
    public void addScenicSpot(ScenicSpot scenicSpot) {
        scenicSpotDao.insertScenicSpot(scenicSpot);
    }

    @Override
    public void updateScenicSpot(ScenicSpot scenicSpot) {
        scenicSpotDao.updateScenicSpot(scenicSpot);
    }

    @Override
    public void deleteScenicSpot(Long id) {
        scenicSpotDao.deleteScenicSpot(id);
    }



    @Override
    public List<ScenicSpot> getALlScenicSpotCard() {
        return scenicSpotDao.getALlScenicSpotCard();
    }


    //首页景点分页
    @Override
    public List<ScenicSpot> getPagedScenicSpots(int page, int size) {
        int offset = page * size;  // 计算 OFFSET
        return scenicSpotDao.getPagedScenicSpotCard(offset, size);
    }

    //更新经纬度
    public void updateScenicSpotCoordinates() {
        baiduMapService.updateScenicSpotCoordinates();
    }
}
