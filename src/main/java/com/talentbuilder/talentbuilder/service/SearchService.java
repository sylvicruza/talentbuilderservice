package com.talentbuilder.talentbuilder.service;

import com.talentbuilder.talentbuilder.dto.SearchDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SearchService {

    List<SearchDto> findCourses(String pattern);

    List<SearchDto> findAllCourses();
}
