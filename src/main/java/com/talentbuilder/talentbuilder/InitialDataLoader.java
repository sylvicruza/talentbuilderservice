package com.talentbuilder.talentbuilder;


import com.talentbuilder.talentbuilder.constant.AppConstants;
import com.talentbuilder.talentbuilder.enumType.UserPrivilageType;
import com.talentbuilder.talentbuilder.enumType.UserRoleType;
import com.talentbuilder.talentbuilder.model.Privilege;
import com.talentbuilder.talentbuilder.model.User;
import com.talentbuilder.talentbuilder.repository.PrivilegeRepository;
import com.talentbuilder.talentbuilder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;

@Component
@Transactional
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private AppConstants appConstants;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	private boolean hasBeenSetup;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (hasBeenSetup) {
			return;
		}

		createPrivilagesIfNotFound();
		createAdminAccountIfNotFound();
		
		hasBeenSetup = true;
	}

	private Privilege createPrivilagesIfNotFound() {

		Privilege privileges;
		Privilege admin = privilegeRepository.findByName(UserPrivilageType.admin);
		Privilege superAdmin = privilegeRepository.findByName(UserPrivilageType.super_admin);
		Privilege agent = privilegeRepository.findByName(UserPrivilageType.member);

		if (admin == null) {
			privileges = new Privilege();
			privileges.setName(UserPrivilageType.admin);
			privilegeRepository.save(privileges);
		}
		
		if (superAdmin == null) {
			privileges = new Privilege();
			privileges.setName(UserPrivilageType.super_admin);
			privilegeRepository.save(privileges);
		}
		
		if (agent == null) {
			privileges = new Privilege();
			privileges.setName(UserPrivilageType.member);
			privilegeRepository.save(privileges);
		}
		

		return null;
	}

	private User createAdminAccountIfNotFound() {

		User userAccount = userRepository.findByEmail(appConstants.APP_ADMIN_EMAIL);
		Privilege admin = privilegeRepository.findByName(UserPrivilageType.super_admin);
		
		Collection<Privilege> adminPrivileges = new HashSet<>();
		adminPrivileges.add(admin);

		if (userAccount != null) {
			return null;
		}

		User user = new User();
		user.setActive(true);
		user.setLock(false);
		user.setEmail(appConstants.APP_ADMIN_EMAIL);
		user.setUsername(appConstants.APP_DEFAULT_ADMIN_NAME);
		user.setFirstName(appConstants.APP_DEFAULT_ADMIN_NAME);
		user.setLastName(appConstants.APP_DEFAULT_ADMIN_NAME);
		user.setPassword(passwordEncoder.encode(appConstants.APP_ADMIN_PASSWORD));
		user.setRole(UserRoleType.SUPERADMIN);
		user.setPrivileges(adminPrivileges);
		user.setUserId("TMA" + System.currentTimeMillis());
		User findEmail = userRepository.findByEmail(user.getEmail());

		if (findEmail != null) {
			return null;
		}

		userRepository.save(user);

		return null;
	}

}
