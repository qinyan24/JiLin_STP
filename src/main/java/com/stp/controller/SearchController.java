package com.stp.controller;


import com.stp.domain.Result;
import com.stp.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public List<Result> search(@RequestParam("query") String query){
        return searchService.search(query);
    }

}
