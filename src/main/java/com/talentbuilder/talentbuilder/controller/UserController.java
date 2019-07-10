package com.talentbuilder.talentbuilder.controller;


import com.talentbuilder.talentbuilder.dto.ServerResponse;
import com.talentbuilder.talentbuilder.dto.SignInRequest;
import com.talentbuilder.talentbuilder.dto.SignUpRequest;
import com.talentbuilder.talentbuilder.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.talentbuilder.talentbuilder.dto.ServerResponse.exceptionMessage;


@Controller
@RequestMapping(value = "/user", produces = "application/json")
@Api(tags = "User Account Management", description = "Endpoint")
public class UserController {
	
	@Autowired
	UserService userService;

	private HttpHeaders responseHeaders = new HttpHeaders();

	@ApiOperation(value = "Register new user. Note: firstName, lastName, email and phone are required", response = ServerResponse.class)
	@RequestMapping( method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody SignUpRequest request){

		ServerResponse response = new ServerResponse();

		try {
			response = userService.create(request);
		 
		} catch (Exception e) {
			response = exceptionMessage(e);
		}
		
		return new ResponseEntity<ServerResponse>(response, responseHeaders, ServerResponse.getStatus(response.getStatus()));
	}

	@ApiOperation(value = "Update user details", response = ServerResponse.class)
	@RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<?> update(@RequestHeader("Authorization")  String authorization, @PathVariable("userId") String userId, @RequestBody SignUpRequest request){

		ServerResponse response = new ServerResponse();

		try {
			response = userService.update(userId, request);

		} catch (Exception e) {
			response = exceptionMessage(e);
		}

		return new ResponseEntity<ServerResponse>(response, responseHeaders, ServerResponse.getStatus(response.getStatus()));
	}

	
	@ApiOperation(value = "Verify and Activate otp", response = ServerResponse.class)
	@RequestMapping(value = "/otp/verify", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> ActivateUser(@RequestParam("otp") String otp){
		
		
		ServerResponse response = new ServerResponse();
		
		try {

			response = userService.userActivation(otp);
		 
		} catch (Exception e) {
			response = exceptionMessage(e);
            
		}
		
		return new ResponseEntity<ServerResponse>(response, responseHeaders, ServerResponse.getStatus(response.getStatus()));
	}
	
	@ApiOperation(value = "Generate otp", response = ServerResponse.class)
	@RequestMapping(value = "/otp", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> ResendUserActivation(@RequestParam("email") String email){

		ServerResponse response = new ServerResponse();
		
		try {

			response = userService.sendOtp(email);
		 
		} catch (Exception e) {
			response = exceptionMessage(e);
            
		}
		
		return new ResponseEntity<ServerResponse>(response, responseHeaders, ServerResponse.getStatus(response.getStatus()));
	}
	
	
	@ApiOperation(value = "Password recovery", response = ServerResponse.class)
	@RequestMapping(value = "/password-rest", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> PasswordRestRequest(@RequestParam("email") String email){

		ServerResponse response = new ServerResponse();
		
		try {
			
			response = userService.reSendUserPassword(email);
		 
		} catch (Exception e) {
			response = exceptionMessage(e);
            
		}
		
		return new ResponseEntity<ServerResponse>(response, responseHeaders, ServerResponse.getStatus(response.getStatus()));
	}
	
	@ApiOperation(value = "Change user password", response = ServerResponse.class)
	@RequestMapping(value = "/password-change", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> PasswordReset(@RequestParam("otp") String otp, @RequestParam("password") String password){
		
		
		ServerResponse response = new ServerResponse();
		
		try {
			
			response = userService.passwordReset(otp, password);
		 
		} catch (Exception e) {
			response = exceptionMessage(e);
            
		}
		
		return new ResponseEntity<ServerResponse>(response, responseHeaders, ServerResponse.getStatus(response.getStatus()));
	}
	
	
	@ApiOperation(value = "Login user", response = ServerResponse.class)
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> Login(@RequestBody SignInRequest request){
		
		ServerResponse response = new ServerResponse();
		
		try {
			
			response = userService.login(request);
		
		} catch (Exception e) {
			response = exceptionMessage(e);
		}
		
		return new ResponseEntity<ServerResponse>(response, responseHeaders, ServerResponse.getStatus(response.getStatus()));

	}

	@ApiOperation(value = "View user by userId", response = ServerResponse.class)
    @RequestMapping(value ="/{userId}",  method = RequestMethod.GET)
	@ResponseBody
    public ResponseEntity<?> getUserByUserId(@RequestHeader("Authorization")  String authorization, @PathVariable("userId") String userId){
		
		ServerResponse response = new ServerResponse();
		
		try {
			
			response = userService.getUserByUserId(userId);
		
		} catch (Exception e) {
			response = exceptionMessage(e);
		}
		
		return new ResponseEntity<ServerResponse>(response, responseHeaders, ServerResponse.getStatus(response.getStatus()));

	}
	

}
