package com.talentbuilder.talentbuilder.repository;


import com.talentbuilder.talentbuilder.enumType.UserRoleType;
import com.talentbuilder.talentbuilder.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;


@Repository
public interface UserRepository extends CommonRepository<User, Long> {

	User findById(long id);

	User findByEmail(String email);
		
	User findByUserId(String userId);
	
	User findByUsername(String username);
	
	User findByUsernameOrEmail(String username, String email);

	User findByOtp(String otp);

	Collection<User> findByRole(UserRoleType role);

	Collection<User> findByActive(boolean active);

	//Soft delete.
	@Query("update User e set e.delFlag=:delFlag,e.deletedOn=:deletedOn  where e.id=:id")
	@Modifying
	void softDelete(@Param("delFlag") String delFlag, @Param("deletedOn") Date deletedOn, @Param("id") Long id);
	
}
