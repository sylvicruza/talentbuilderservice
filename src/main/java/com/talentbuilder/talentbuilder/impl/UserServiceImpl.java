package com.talentbuilder.talentbuilder.impl;

import com.talentbuilder.talentbuilder.constant.AppConstants;
import com.talentbuilder.talentbuilder.constant.ServerResponseStatus;
import com.talentbuilder.talentbuilder.dto.*;
import com.talentbuilder.talentbuilder.enumType.UserPrivilageType;
import com.talentbuilder.talentbuilder.enumType.UserRoleType;
import com.talentbuilder.talentbuilder.mail.EmailService;
import com.talentbuilder.talentbuilder.mail.Mail;
import com.talentbuilder.talentbuilder.model.Privilege;
import com.talentbuilder.talentbuilder.model.User;
import com.talentbuilder.talentbuilder.repository.PrivilegeRepository;
import com.talentbuilder.talentbuilder.repository.UserRepository;
import com.talentbuilder.talentbuilder.service.UserService;
import com.talentbuilder.talentbuilder.utilities.AmazonClient;
import com.talentbuilder.talentbuilder.utilities.OtpService;
import com.talentbuilder.talentbuilder.utilities.PasswordValidator;
import com.talentbuilder.talentbuilder.utilities.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;

import static com.talentbuilder.talentbuilder.dto.ServerResponse.exceptionMessage;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    AppConstants appConstants;
    @Autowired
    PrivilegeRepository privilegeRepository;
    @Autowired
    OtpService otpService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AmazonClient amazonClient;

    @Override
    public Collection<User> findAll() {
        try {
            return (Collection<User>) userRepository.findAll();
        }
        catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }


    @Override
    public User findById(long id) {

        try {
            return userRepository.findById(id);
        }
        catch (Exception e) {

            e.printStackTrace();
        }
        return null;

    }

    @Override
    public User findByEmail(String email) {
        try {
            return userRepository.findByEmail(email);
        }
        catch (Exception e) {

            e.printStackTrace();
        }
        return null;

    }

    @Override
    public User findByUsernameOrEmail(String emailOrUsername) {
        try {
            return userRepository.findByUsernameOrEmail(emailOrUsername, emailOrUsername);
        }
        catch (Exception e) {

            e.printStackTrace();
        }
        return null;

    }

    public ServerResponse validateSignUpRequest(SignUpRequest request) {
        if (!Utility.isValidEmail(request.getEmail())) {
            return ServerResponse.badRequest("Enter a correct email address");
        }

        if (!Utility.isValidInput(request.getUsername())) {
            return ServerResponse.badRequest("Please provide username");
        }

        if (!Utility.isValidInput(request.getFirstName())) {
            return ServerResponse.badRequest("Please enter firstname");
        }

        if (!Utility.isValidInput(request.getLastName())) {
            return ServerResponse.badRequest("Please enter lastname");
        }

        if (!Utility.isValidInput(request.getPassword())) {
            return ServerResponse.badRequest("Please provide password");
        }

        if (!PasswordValidator.validate(request.getPassword())) {
            return ServerResponse.badRequest("Wrong password format!!!");
        }
        return null;
    }

    @Override
    @Transactional
    public ServerResponse create(SignUpRequest request) {
        ServerResponse response = new ServerResponse();

        if (validateSignUpRequest(request) != null) {
            return validateSignUpRequest(request);
        }

        try {

            User user = userRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail());

            if (user != null) {
                return ServerResponse.badRequest("User email and username already exist");
            }

            Privilege privilege = privilegeRepository.findByName(UserPrivilageType.member);

            Collection<Privilege> adminPrivileges = new HashSet<>();
            adminPrivileges.add(privilege);

            user = new User();
            user.setPrivileges(adminPrivileges);

            user.setRole(UserRoleType.MEMBER);
            user.setEmail(request.getEmail());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setUsername(request.getUsername());
            user.setDateCreated(new Date());
            user.setActive(false);
            user.setLock(false);
            user.setUserId("TB" + System.currentTimeMillis());
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            userRepository.save(user);

            Mail mail = new Mail();
            mail.setTo(request.getEmail());
            mail.setFrom(appConstants.MAIL_USERNAME);

            Map<String, Object> model = new HashMap<String, Object>();
            model.put("user", request.getFirstName());
            mail.setSubject("Welcome");
            mail.setModel(model);
            mail.setTemplate("welcome.ftl");
            emailService.sendSimpleMessage(mail);

            String otp = Utility.generateRandomString(40);
            user.setOtp(otp);

            model.put("code", appConstants.APP_WEB_URL + "/confirmation?token=" + otp);
            mail.setSubject("Verify");
            mail.setModel(model);
            mail.setTemplate("verify.ftl");
            emailService.sendSimpleMessage(mail);

            return new ServerResponse(true,
                    "Successfully created",
                    user,
                    ServerResponseStatus.OK);

        }
        catch (Exception e) {
            return exceptionMessage(e);

        }

    }

    @Override
    public ServerResponse update(String userId, UpdateUserDto request) {
        try {

            User user = userRepository.findByUserId(userId);

            if (user == null) {
                return ServerResponse.badRequest("User not found");
            }

            user.setDateUpdated(new Date());
            if (Utility.isValidInput(request.getFirstName())) {
                user.setFirstName(request.getFirstName());
            }

            if (Utility.isValidInput(request.getLastName())) {
                user.setLastName(request.getLastName());
            }

            if (Utility.isValidInput(request.getBiography())) {
                user.setBiography(request.getBiography());
            }

            if (Utility.isValidInput(request.getFacebook())) {
                user.setFacebook(request.getFacebook());
            }

            if (Utility.isValidInput(request.getLinkendin())) {
                user.setLinkendin(request.getLinkendin());
            }

            if (Utility.isValidInput(request.getTwitter())) {
                user.setTwitter(request.getTwitter());
            }

            if (Utility.isValidInput(request.getWebsite())) {
                user.setWebsite(request.getWebsite());
            }
            user.setAssignMentor(request.getAssignMentor());

            return new ServerResponse(true,
                    "Updated successfully",
                    user,
                    ServerResponseStatus.OK);


        }
        catch (Exception e) {
            return exceptionMessage(e);
        }

    }

    @Override
    public ServerResponse userActivation(String otp) {

        try {

            User user = userRepository.findByOtp(otp);

            if (user == null) {
                return ServerResponse.badRequest("Invalid verification code. Please Retry!");
            }

            user.setActive(true);
            user.setOtp(null);

            Mail mail = new Mail();
            mail.setTo(user.getEmail());
            mail.setFrom(appConstants.MAIL_USERNAME);
            mail.setSubject("Verified");

            Map<String, Object> model = new HashMap<String, Object>();
            model.put("title", "Account verified");
            model.put("salutation", "Hello " + user.getFirstName());
            model.put("message", "Your account has been verified. Kindly logon.");
            model.put("link", appConstants.APP_WEB_URL + "/signin");
            mail.setModel(model);
            mail.setTemplate("general.ftl");
            emailService.sendSimpleMessage(mail);

            return new ServerResponse(true,
                    "Activation successful",
                    user,
                    ServerResponseStatus.OK);

        }
        catch (Exception e) {
            return exceptionMessage(e);
        }

    }

    @Override
    public ServerResponse sendOtp(String email) {
        ServerResponse response = new ServerResponse();

        try {

            User user = userRepository.findByEmail(email);

            if (user == null) {
                return ServerResponse.badRequest("Email does not exist");
            }

            if (user.isActive()) {
                return ServerResponse.badRequest("Account has been activated");
            }

            String otp = Utility.generateRandomString(40);
            user.setOtp(otp);

            Mail mail = new Mail();
            mail.setTo(email);
            mail.setFrom(appConstants.MAIL_USERNAME);

            Map<String, Object> model = new HashMap<String, Object>();
            model.put("code", appConstants.APP_WEB_URL + "/confirmation?token=" + otp);
            mail.setSubject("Verify");
            mail.setModel(model);
            mail.setTemplate("verify.ftl");
            emailService.sendSimpleMessage(mail);

            return new ServerResponse(true,
                    "OTP sent successful",
                    "",
                    ServerResponseStatus.OK);

        }
        catch (Exception e) {
            return exceptionMessage(e);
        }
    }

    @Override
    public ServerResponse reSendUserPassword(String email) {
        ServerResponse response = new ServerResponse();

        try {

            User user = userRepository.findByEmail(email);

            if (user == null) {
                return ServerResponse.badRequest("Email does not exist");
            }

            String otp = Utility.generateRandomString(40);
            user.setOtp(otp);

            Mail mail = new Mail();
            mail.setTo(user.getEmail());
            mail.setFrom(appConstants.MAIL_USERNAME);

            Map<String, Object> model = new HashMap<String, Object>();
            model.put("link", appConstants.APP_WEB_URL + "/password-reset?token=" + otp);
            mail.setSubject("Password reset");
            mail.setModel(model);
            mail.setTemplate("password_reset.ftl");
            emailService.sendSimpleMessage(mail);

            return new ServerResponse(true,
                    "OTP sent successful",
                    "",
                    ServerResponseStatus.OK);

        }
        catch (Exception e) {
            return exceptionMessage(e);
        }
    }

    @Override
    public ServerResponse passwordReset(String otp, String password) {
        ServerResponse response = new ServerResponse();

        try {

            User user = userRepository.findByOtp(otp);

            if (user == null) {
                return ServerResponse.badRequest("Invalid verification code. Please Retry!");
            }

            if (!PasswordValidator.validate(password)) {
                return ServerResponse.badRequest("Wrong password format!!!");
            }

            user.setActive(true);
            user.setPassword(passwordEncoder.encode(password));
            user.setOtp(null);

            Mail mail = new Mail();
            mail.setTo(user.getEmail());
            mail.setFrom(appConstants.MAIL_USERNAME);
            mail.setSubject("Password Reset");

            Map<String, Object> model = new HashMap<String, Object>();
            model.put("title", "Password Reset");
            model.put("salutation", "Hello " + user.getFirstName());
            model.put("message", "Your password has been reset. Kindly logon.");
            model.put("link", appConstants.APP_WEB_URL + "/signin");
            mail.setModel(model);
            mail.setTemplate("general.ftl");
            emailService.sendSimpleMessage(mail);

            return new ServerResponse(true,
                    "Activation successful",
                    user,
                    ServerResponseStatus.OK);

        }
        catch (Exception e) {
            return exceptionMessage(e);
        }
    }

    @Override
    public ServerResponse login(SignInRequest request) {

        ServerResponse response = new ServerResponse();
        try {


            //convert client id and client secret to base64 token
            String authorization = Utility.getCredentials(appConstants.CLIENT_ID, appConstants.CLIENT_SECRET);
            request.setGrant_type(appConstants.GRANT_TYPE);

            //send login request
            response = Utility.loginHttpRequest(appConstants.APP_LOGIN_URL, request, authorization);

        }
        catch (Exception e) {
            return exceptionMessage(e);
        }

        return response;
    }

    @Override
    public ServerResponse getUserByUserId(String userId) {
        if (!Utility.isValidInput(userId)) {
            return ServerResponse.badRequest("Please provide userId");
        }

        try {

            User user = userRepository.findByUserId(userId);

            if (user == null) {
                return ServerResponse.badRequest("User not found");
            }

            return new ServerResponse(true,
                    "Get data successfully",
                    user,
                    ServerResponseStatus.OK);
        }
        catch (Exception e) {
            return exceptionMessage(e);
        }
    }

    @Override
    public ServerResponse deleteUserDetails(String userId,String password) {
        ServerResponse response = new ServerResponse();

        if (!Utility.isValidInput(userId)) {
            return new ServerResponse(true,
                    "",
                    "",
                    ServerResponseStatus.FAILED);
        }

        try {

            User user = userRepository.findByUserId(userId);

            if (user == null) {
                return new ServerResponse(false,
                        "User not found",
                        "",
                        ServerResponseStatus.FAILED);
            }
            if(!user.getPassword().matches(password)){
                return new ServerResponse(false,
                        "Incorrect password",
                        "",
                        ServerResponseStatus.FAILED);
            }


           userRepository.softDelete("Y",new Date(),user.getId());

            return new ServerResponse(true,
                    "Deleted successfully",
                    userRepository.findById(user.getId()),
                    ServerResponseStatus.OK);


        } catch (Exception e) {
            return exceptionMessage(e);
        }
    }

    @Override
    public ServerResponse changePassword(String userId, PasswordRest request) {
        ServerResponse response = new ServerResponse();

        try {

            if (!Utility.isValidInput(request.getOldPassword())) {
                return ServerResponse.badRequest("Please provide old password");
            }

            if (PasswordValidator.validate(request.getNewPassword())) {
                return ServerResponse.badRequest("Wrong password format!!!");
            }

            User user = userRepository.findByUserId(userId);

            if (user == null) {
                return ServerResponse.badRequest("User not found");
            }

            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                return ServerResponse.badRequest("Invalid password");
            }

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));

            Mail mail = new Mail();
            mail.setTo(user.getEmail());
            mail.setFrom(appConstants.MAIL_USERNAME);
            mail.setSubject("Password reset");


            Map<String, Object> model = new HashMap<String, Object>();
            model.put("title", "Password Reset");
            model.put("salutation", "Hello " + user.getFirstName());
            model.put("message", "Password reset successfully.");
    Res        model.put("link", "");
            mail.setModel(model);
            mail.setTemplate("general.ftl");
            emailService.sendSimpleMessage(mail);

            response.setData("Password reset successfully");
            response.setMessage("Password reset successfully");
            response.setSuccess(true);
            response.setStatus(ServerResponseStatus.OK);

        }
        catch (Exception e) {
            return exceptionMessage(e);
        }
        return response;
    }

    @Override
    public ServerResponse uploadProfileImage(String userId, MultipartFile file) {

        if (!Utility.isValidInput(userId)) {
            return ServerResponse.badRequest("Please provide userId");
        }

        try {
            User user = userRepository.findByUserId(userId);

            if (user == null) {
                return ServerResponse.badRequest("User not found");
            }

            String url = amazonClient.uploadFile(userId, file);
            user.setProfileImage(url);

            return new ServerResponse(true,
                    "Upload successfully",
                    user,
                    ServerResponseStatus.OK);

        }
        catch (Exception e) {
            return exceptionMessage(e);
        }
    }

    @Override
    public ServerResponse deleteProfileImage(String userId) {
        if (!Utility.isValidInput(userId)) {
            return ServerResponse.badRequest("Please provide userId");
        }

        try {
            User user = userRepository.findByUserId(userId);

            if (user == null) {
                return ServerResponse.badRequest("User not found");
            }

            amazonClient.deleteFileFromS3Bucket(user.getUserId());
            user.setProfileImage(null);

            return new ServerResponse(true,
                    "Successfully deleted",
                    user,
                    ServerResponseStatus.OK);

        }
        catch (Exception e) {
            return exceptionMessage(e);
        }
    }
}
