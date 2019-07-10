package com.talentbuilder.talentbuilder.repository;


import com.talentbuilder.talentbuilder.enumType.UserRoleType;
import com.talentbuilder.talentbuilder.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	User findById(long id);

	User findByEmail(String email);
		
	User findByUserId(String userId);
	
	User findByUsername(String username);
	
	User findByUsernameOrEmail(String username, String email);

	User findByOtp(String otp);

	Collection<User> findByRole(UserRoleType role);

	Collection<User> findByActive(boolean active);
	
}
