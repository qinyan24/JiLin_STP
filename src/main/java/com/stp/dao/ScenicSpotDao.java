package com.stp.dao;

import com.stp.domain.ScenicSpot;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ScenicSpotDao {

    // 根据 ID 获取景点
    @ResultMap(value = "ScenicSpotMap")
    @Select("SELECT id, name, text, ticket_price, description,image_url, open_time FROM scenic_spots WHERE id = #{id}")
    ScenicSpot selectScenicSpotById(Long id);

    @Select("SELECT id, name, description, image_url, ticket_price, open_time " +
            "FROM scenic_spots"  +
            " LIMIT #{offset}, #{size}")
    @Results(id = "ScenicSpotMap",value = {
            @Result(property = "imageUrl", column = "image_url"),
            @Result(property = "openTime", column = "open_time"),
            @Result(property = "ticketPrice", column = "ticket_price"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updateAt", column = "update_at")
    })
    List<ScenicSpot> getALlScenicSpotCard();

    @Select("SELECT id, name, description, image_url, ticket_price, open_time\n" +
            "        FROM scenic_spots\n" +
            "                 LIMIT #{offset}, #{size}")
    @ResultMap(value = "ScenicSpotMap")
    List<ScenicSpot> getPagedScenicSpotCard(@Param("offset") int offset, @Param("size") int size);

    // 获取所有景点数量
    @Select("SELECT COUNT(*) FROM scenic_spots")
    int getAllScenicSpotCount();

    // 获取所有景点
    @Select("SELECT id, name, latitude, longitude, text, ticket_price, open_time, description, image_url, created_at, updated_at FROM scenic_spots")
    @ResultMap(value = "ScenicSpotMap")
    List<ScenicSpot> selectAllSpots();

    // 添加景点
    @Insert("INSERT INTO scenic_spots(name, description, ticket_price, open_time, image_url) VALUES(#{name}, #{description}, #{ticketPrice}, #{openTime}, #{imageUrl})")
    @ResultMap(value = "ScenicSpotMap")
    void insertScenicSpot(ScenicSpot scenicSpot);

    // 更新景点
    @Update("UPDATE scenic_spots SET name=#{name}, description=#{description}, ticket_price=#{ticketPrice}, open_time=#{openTime}, image_url=#{imageUrl} WHERE id=#{id}")
    @ResultMap(value = "ScenicSpotMap")
    void updateScenicSpot(ScenicSpot scenicSpot);

    // 删除景点
    @Delete("DELETE FROM scenic_spots WHERE id=#{id}")
    @ResultMap(value = "ScenicSpotMap")
    void deleteScenicSpot(Long id);

    //更新数据库经纬度
    @Update("UPDATE scenic_spots SET longitude=#{longitude}, latitude=#{latitude} WHERE id=#{id}")
    void updateScenicSpotCoordinates(ScenicSpot scenicSpot);

}
