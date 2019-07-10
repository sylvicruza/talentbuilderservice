package com.talentbuilder.talentbuilder.userService;

import com.google.gson.Gson;
import com.talentbuilder.talentbuilder.TalentbuilderApplication;
import com.talentbuilder.talentbuilder.dto.*;
import com.talentbuilder.talentbuilder.model.User;
import com.talentbuilder.talentbuilder.service.UserService;
import com.talentbuilder.talentbuilder.utilities.Utility;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
classes = TalentbuilderApplication.class)
public class UserServiceTest {

	@Autowired
	UserService userService;
	Gson gson;

	@Test
	public void create(){
		SignUpRequest signUpRequest = new SignUpRequest();
		signUpRequest.setFirstName(Utility.generateRandomString(8));
		signUpRequest.setLastName(Utility.generateRandomString(8));
		signUpRequest.setEmail(Utility.generateRandomString(8) + "@gmail.com");
		signUpRequest.setPassword(Utility.generateRandomString(8));
		signUpRequest.setUsername(Utility.generateRandomString(8));

		ServerResponse response = userService.create(signUpRequest);
		assertEquals(response.isSuccess(), true);

	}

	@Test
	public void findByPhoneOrEmail() throws Exception {
		User user = userService.findByEmail("admin@talentbuilder.com");
		assertEquals(user.getEmail(), "admin@talentbuilder.com");
	}
	
	
	@Test
	public void Login() throws Exception {
		
		SignInRequest signInRequest = new SignInRequest();
		signInRequest.setPassword("password");
		signInRequest.setUsername("admin@talentbuilder.com");
		ServerResponse response = userService.login(signInRequest);

		gson = new Gson();
		String gsonString = gson.toJson(response.getData());
		LoginResponse loginResponse = gson.fromJson(gsonString, LoginResponse.class);

		assertEquals(response.isSuccess(), true);
		assertEquals(loginResponse.getUser().getEmail(), signInRequest.getUsername());
	}
	
	@Test
	public void ActivateUser() throws Exception {
		String otp = "090909";
		ServerResponse response = userService.userActivation(otp);
		System.out.println(response.getMessage());
		assertEquals(response.isSuccess(), false);
	}
	
	@Test
	public void PasswordRestRequest() throws Exception {
		String email = "admin@talentbuilder.com";
		ServerResponse response = userService.reSendUserPassword(email);
		assertEquals(response.isSuccess(), true);
		assertEquals(response.getMessage(), "OTP sent successful");
	}
	
	@Test
	public void ResendUserActivation() throws Exception {
		String email = "admin@talentbuilder.com";
		ServerResponse response = userService.sendOtp(email);
		assertEquals(response.isSuccess(), true);
		assertEquals(response.getMessage(), "OTP sent successful");
	}
	
	@Test
	public void PasswordReset() throws Exception {
		PasswordRestDto request = new PasswordRestDto();
		String otp = "913961";
		String password = "password";
		ServerResponse response = userService.passwordReset(otp, password);
		assertEquals(response.isSuccess(), false);

	}
}
