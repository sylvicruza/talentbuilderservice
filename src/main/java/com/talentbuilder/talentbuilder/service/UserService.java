package com.talentbuilder.talentbuilder.service;


import com.talentbuilder.talentbuilder.dto.ServerResponse;
import com.talentbuilder.talentbuilder.dto.SignInRequest;
import com.talentbuilder.talentbuilder.dto.SignUpRequest;
import com.talentbuilder.talentbuilder.model.User;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface UserService {

	Collection<User> findAll();
		
	User findById(long id);
	
	User findByEmail(String email);
	
	User findByPhone(String phone);
	
	User findByPhoneOrEmail(String emailOrphone);
	
	ServerResponse create(SignUpRequest request);

	ServerResponse update(String userId, SignUpRequest request);

	ServerResponse userActivation(String otp);
	
	ServerResponse sendOtp(String email);
	
	ServerResponse reSendUserPassword(String phone);
	
	ServerResponse passwordReset(String otp, String paswword);
	
	ServerResponse login(SignInRequest request);
	
	ServerResponse getUserByUserId(String userId);

}
