package com.talentbuilder.talentbuilder.service;


import com.talentbuilder.talentbuilder.dto.AddUserDto;
import com.talentbuilder.talentbuilder.dto.ServerResponse;
import com.talentbuilder.talentbuilder.enumType.UserRoleType;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {

    ServerResponse addUser(AddUserDto request);

    ServerResponse deleteUser(String userId);

    ServerResponse getUserByRole(UserRoleType role);

    ServerResponse lockUserAccount(String userId, boolean lock);

    ServerResponse getAllUsers();

}
