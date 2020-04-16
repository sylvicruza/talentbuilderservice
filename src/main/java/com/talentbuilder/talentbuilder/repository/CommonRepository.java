package com.talentbuilder.talentbuilder.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@NoRepositoryBean
public interface CommonRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {


    @Transactional
    void deleteById(ID id);

    @Transactional
    void delete(T entity);


}