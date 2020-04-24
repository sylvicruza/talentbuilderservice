package com.talentbuilder.talentbuilder.impl;

import com.talentbuilder.talentbuilder.dto.SearchDto;
import com.talentbuilder.talentbuilder.model.Search;
import com.talentbuilder.talentbuilder.repository.SearchRepository;
import com.talentbuilder.talentbuilder.service.SearchService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SearchRepository searchRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<SearchDto> findCourses(String pattern) {
        List<Search> page = searchRepository.findCourseBySearch(pattern);
        return convertEntitiesToDTOs(page);
    }

    @Override
    public List<SearchDto> findAllCourses() {
        List<Search> page = searchRepository.findAll();
       return convertEntitiesToDTOs(page);
    }

    private List<SearchDto> convertEntitiesToDTOs(List<Search> content) {
        return content.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    private SearchDto convertEntityToDTO(Search search) {
        return modelMapper.map(search,SearchDto.class);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
