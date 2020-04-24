package com.talentbuilder.talentbuilder.controller;

import com.talentbuilder.talentbuilder.dto.SearchDto;
import com.talentbuilder.talentbuilder.service.SearchService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.repository.DataTablesUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/user", produces = "application/json")
@Api(tags = "Search Courses", description = "Endpoint")
public class SearchController {
    @Autowired
    SearchService searchService;



    @GetMapping(path = "/all")
    @ResponseBody
    public List<SearchDto> getUsers(@RequestHeader("Authorization")  String authorization, @RequestParam(required = false) String search){

        List<SearchDto> searchDtos = null;
        if (StringUtils.isNoneBlank(search)) {
            searchDtos = searchService.findCourses(search);
        }else{
            searchDtos = searchService.findAllCourses();
        }
        return searchDtos;
    }
}
