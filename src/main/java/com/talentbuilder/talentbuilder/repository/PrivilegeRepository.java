package com.talentbuilder.talentbuilder.repository;


import com.talentbuilder.talentbuilder.enumType.UserPrivilageType;
import com.talentbuilder.talentbuilder.model.Privilege;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {

	Privilege findById(long id);

	Privilege findByName(UserPrivilageType name);
	
}
