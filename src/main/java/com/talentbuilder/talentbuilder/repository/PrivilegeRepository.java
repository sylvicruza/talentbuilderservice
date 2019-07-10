package com.talentbuilder.talentbuilder.repository;


import com.talentbuilder.talentbuilder.enumType.UserPrivilageType;
import com.talentbuilder.talentbuilder.model.Privilege;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {

	public Privilege findById(long id);

	public Privilege findByName(UserPrivilageType name);
	
}
