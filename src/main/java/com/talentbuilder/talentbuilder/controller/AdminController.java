package com.talentbuilder.talentbuilder.controller;


import com.talentbuilder.talentbuilder.dto.AddUserDto;
import com.talentbuilder.talentbuilder.dto.ServerResponse;
import com.talentbuilder.talentbuilder.enumType.UserRoleType;
import com.talentbuilder.talentbuilder.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.talentbuilder.talentbuilder.dto.ServerResponse.exceptionMessage;


@Controller
@RequestMapping(value = "/admin", produces = "application/json")
@Api(tags = "Admin Management", description = "Endpoint")
@PreAuthorize("hasAuthority('admin') OR hasAuthority('super_admin')")
public class AdminController {

    @Autowired
    AdminService adminService;

    private HttpHeaders responseHeaders = new HttpHeaders();

    @ApiOperation(value = "Add admin user Note: firstName, email and role are required, user role must be either ADMIN", response = ServerResponse.class)
    @RequestMapping( method = RequestMethod.POST)
//    @PreAuthorize("hasAuthority('admin') OR hasAuthority('super_admin')")
    @ResponseBody
    public ResponseEntity<?> addUser(@RequestHeader("Authorization")  String authorization, @RequestBody AddUserDto request){

        ServerResponse response = new ServerResponse();

        try {
            response = adminService.addUser(request);

        }catch (Exception e) {
            response = exceptionMessage(e);

        }

        return new ResponseEntity<ServerResponse>(response, responseHeaders, ServerResponse.getStatus(response.getStatus()));
    }

    @ApiOperation(value = "Delete user", response = ServerResponse.class)
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
//    @PreAuthorize("hasAuthority('admin')")
    @ResponseBody
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization")  String authorization, @PathVariable("userId") String userId){

        ServerResponse response = new ServerResponse();

        try {
            response = adminService.deleteUser(userId);

        } catch (Exception e) {
            response = exceptionMessage(e);

        }

        return new ResponseEntity<ServerResponse>(response, responseHeaders, ServerResponse.getStatus(response.getStatus()));
    }


    @ApiOperation(value = "View user by role", response = ServerResponse.class)
    @RequestMapping(value = "/role/{role}", method = RequestMethod.GET)
//    @PreAuthorize("hasAuthority('admin')")
    @ResponseBody
    public ResponseEntity<?> getUserByRole(@RequestHeader("Authorization")  String authorization, @PathVariable("role") UserRoleType role){

        ServerResponse response = new ServerResponse();

        try {
            response = adminService.getUserByRole(role);

        } catch (Exception e) {
            response = exceptionMessage(e);

        }

        return new ResponseEntity<ServerResponse>(response, responseHeaders, ServerResponse.getStatus(response.getStatus()));
    }


    @ApiOperation(value = "Lock and unlock user account", response = ServerResponse.class)
    @RequestMapping(value = "/{userId}/lock/{lock}", method = RequestMethod.PUT)
//    @PreAuthorize("hasAuthority('admin')")
    @ResponseBody
    public ResponseEntity<?> lockUserAccount(@RequestHeader("Authorization")  String authorization, @PathVariable("userId") String userId, @PathVariable("lock") boolean lock){

        ServerResponse response = new ServerResponse();

        try {
            response = adminService.lockUserAccount(userId, lock);

        } catch (Exception e) {
            response = exceptionMessage(e);

        }

        return new ResponseEntity<ServerResponse>(response, responseHeaders, ServerResponse.getStatus(response.getStatus()));
    }


    @ApiOperation(value = "View all users", response = ServerResponse.class)
    @RequestMapping( method = RequestMethod.GET)
//    @PreAuthorize("hasAuthority('admin')")
    @ResponseBody
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization")  String authorization){

        ServerResponse response = new ServerResponse();

        try {

            response = adminService.getAllUsers();

        } catch (Exception e) {
            response = exceptionMessage(e);
        }

        return new ResponseEntity<ServerResponse>(response, responseHeaders, ServerResponse.getStatus(response.getStatus()));
    }

}
