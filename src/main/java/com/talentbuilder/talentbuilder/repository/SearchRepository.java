package com.talentbuilder.talentbuilder.repository;

import com.talentbuilder.talentbuilder.model.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository extends CommonRepository<Search,Long> {


    @Query("select r from Search r where  r.img like %:search% or r.duration like %:search% or r.rating like %:search% or r.ratingsCount like %:search% or r.title like %:search% or r.price like %:search% or r.description like %:search%")
    List<Search> findCourseBySearch(@Param("search") String search);
}
