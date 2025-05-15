package com.stp.service.impl;

import com.stp.dao.SearchDao;
import com.stp.domain.Result;
import com.stp.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchDao searchDao;

    /**
     * 根据查询条件进行模糊搜索
     * @param query 搜索关键字
     * @return 返回与查询匹配的结果列表
     */
    @Override
    public List<Result> search(String query) {
        // 使用 "%" 包裹查询关键字，进行模糊查询
        String likeQuery = "%" + query + "%";
        return searchDao.searchByQuery(likeQuery);
    }
}
