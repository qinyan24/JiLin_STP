package com.stp.service;


import com.stp.domain.Result;

import java.util.List;

public interface SearchService {

    /**
     * 根据查询条件进行模糊搜索
     * @param query 搜索关键字
     * @return 返回与查询匹配的结果列表
     */
    List<Result> search(String query);
}
