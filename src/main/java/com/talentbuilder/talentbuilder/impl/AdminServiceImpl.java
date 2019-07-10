package com.talentbuilder.talentbuilder.impl;


import com.talentbuilder.talentbuilder.constant.AppConstants;
import com.talentbuilder.talentbuilder.constant.ServerResponseStatus;
import com.talentbuilder.talentbuilder.dto.AddUserDto;
import com.talentbuilder.talentbuilder.dto.ServerResponse;
import com.talentbuilder.talentbuilder.enumType.UserPrivilageType;
import com.talentbuilder.talentbuilder.enumType.UserRoleType;
import com.talentbuilder.talentbuilder.mail.EmailService;
import com.talentbuilder.talentbuilder.mail.Mail;
import com.talentbuilder.talentbuilder.model.Privilege;
import com.talentbuilder.talentbuilder.model.User;
import com.talentbuilder.talentbuilder.repository.PrivilegeRepository;
import com.talentbuilder.talentbuilder.repository.UserRepository;
import com.talentbuilder.talentbuilder.service.AdminService;
import com.talentbuilder.talentbuilder.utilities.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.talentbuilder.talentbuilder.dto.ServerResponse.exceptionMessage;


@Transactional
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AppConstants appConstants;

    @Autowired
    PrivilegeRepository privilegeRepository;

    @Autowired
    private EmailService emailService;


    @Override
    public ServerResponse addUser(AddUserDto request) {
        ServerResponse response = new ServerResponse();

        if (!Utility.isValidInput(request.getEmail()) || !Utility.isValidEmail(request.getEmail())) {
            return new ServerResponse(false,
                    "Please enter valid email address",
                    "",
                    ServerResponseStatus.FAILED);
        }

        if (!Utility.isValidInput(request.getUsername())) {
            return new ServerResponse(false,
                    "Please enter valid phone number",
                    "",
                    ServerResponseStatus.FAILED);
        }

        if (!Utility.isValidInput(request.getFirstName()) || request.getFirstName().isEmpty()) {
            return new ServerResponse(false,
                    "Please enter firstname",
                    "",
                    ServerResponseStatus.FAILED);
        }

        if (!Utility.isValidInput(request.getLastName()) || request.getLastName().isEmpty()) {
            return new ServerResponse(false,
                    "Please enter lastname",
                    "",
                    ServerResponseStatus.FAILED);
        }

        try {

            User user = userRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail());

            if (user != null) {
                return new ServerResponse(false,
                        "User email or username already exist",
                        "",
                        ServerResponseStatus.FAILED);
            }


            user = new User();
            if (request.getRole().equals(UserRoleType.ADMIN)) {
                Privilege privilege = privilegeRepository.findByName(UserPrivilageType.admin);
                Collection<Privilege> adminPrivileges = new HashSet<>();
                adminPrivileges.add(privilege);
                user.setPrivileges(adminPrivileges);
                user.setRole(UserRoleType.ADMIN);
            }
            else if (request.getRole().equals(UserRoleType.ADMIN)) {
                Privilege superPrivilege = privilegeRepository.findByName(UserPrivilageType.super_admin);
                Collection<Privilege> adminPrivileges = new HashSet<>();
                adminPrivileges.add(superPrivilege);
                user.setPrivileges(adminPrivileges);
                user.setRole(UserRoleType.SUPERADMIN);
            }

            String otp = Utility.generateRandomString(40);

            user.setEmail(request.getEmail());
            user.setUsername(request.getUsername());
            user.setFirstName(request.getFirstName());
            user.setDateCreated(new Date());
            user.setActive(false);
            user.setLock(false);
            user.setUserId("AD" + System.currentTimeMillis());
            user.setOtp(otp);

            userRepository.save(user);

            Mail mail = new Mail();
            mail.setTo(request.getEmail());
            mail.setFrom(appConstants.MAIL_USERNAME);

            Map<String, Object> model = new HashMap<String, Object>();
            model.put("user", request.getFirstName());
            model.put("link", appConstants.APP_WEB_URL + "/verify/" +otp);
            mail.setSubject("Admin Invitation");
            mail.setModel(model);
            mail.setTemplate("admin_invite.ftl");
            emailService.sendSimpleMessage(mail);

            return new ServerResponse(true,
                    "Successfully created",
                    user,
                    ServerResponseStatus.OK);

        } catch (Exception e) {
            return exceptionMessage(e);

        }
    }

    @Override
    public ServerResponse deleteUser(String userId) {
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


            userRepository.delete(user);

            return new ServerResponse(true,
                    "Deleted successfully",
                    user,
                    ServerResponseStatus.OK);


        } catch (Exception e) {
            return exceptionMessage(e);
        }
    }

    @Override
    public ServerResponse getUserByRole(UserRoleType role) {
        ServerResponse response = new ServerResponse();
        try {

            Collection<User> users = userRepository.findByRole(role);

            if (users.size() < 1) {
                 return new ServerResponse(false,
                        "No user found",
                        users,
                        ServerResponseStatus.OK);
            }

            return new ServerResponse(true,
                    "Get data successfully",
                    users,
                    ServerResponseStatus.OK);

        } catch (Exception e) {
            return exceptionMessage(e);
        }

    }

    @Override
    public ServerResponse lockUserAccount(String userId, boolean lock) {
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

            user.setLock(lock);

            return new ServerResponse(true,
                    "Successfully",
                    "",
                    ServerResponseStatus.OK);

        } catch (Exception e) {
            return exceptionMessage(e);
        }

    }

    @Override
    public ServerResponse getAllUsers() {
        ServerResponse response = new ServerResponse();
        try {


            Collection<User> users = (Collection<User>) userRepository.findAll();

            if (users.size() < 1) {
                return new ServerResponse(false,
                        "No user found",
                        users,
                        ServerResponseStatus.OK);
            }

            return new ServerResponse(true,
                    "Get data successfully",
                    users,
                    ServerResponseStatus.OK);
        } catch (Exception e) {
            return exceptionMessage(e);
        }

    }
}
