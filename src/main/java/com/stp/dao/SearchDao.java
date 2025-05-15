package com.stp.dao;

import com.stp.domain.Result;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SearchDao {
    @Select("SELECT * FROM scenic_spots WHERE name LIKE #{query}")
    List<Result> searchByQuery(String query);
}
