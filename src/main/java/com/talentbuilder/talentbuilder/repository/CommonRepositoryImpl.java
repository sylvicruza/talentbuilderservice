package com.talentbuilder.talentbuilder.repository;

import com.talentbuilder.talentbuilder.model.AbstractEntity;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Date;

@Transactional
public class CommonRepositoryImpl<T extends AbstractEntity, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements CommonRepository<T, ID> {

    public CommonRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    public CommonRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
    }

    @Override
    @Transactional
    public void deleteById(ID id) {
        T t = getOne(id);
        t.setDelFlag("Y");
        t.setDeletedOn(new Date());

        super.save(t);
    }

    @Override
    @Transactional
    public void delete(T entity) {
        entity.setDelFlag("Y");
        entity.setDeletedOn(new Date());
        super.save(entity);
    }


}
