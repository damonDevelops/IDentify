package com.team.identify.IdentifyAPI.controller;

import com.team.identify.IdentifyAPI.database.UserRepository;
import com.team.identify.IdentifyAPI.model.User;
import com.team.identify.IdentifyAPI.payload.request.UserInfoUpdateRequest;
import com.team.identify.IdentifyAPI.payload.response.MessageResponse;
import com.team.identify.IdentifyAPI.payload.response.UserInfoResponse;
import com.team.identify.IdentifyAPI.security.services.UserDetailsImpl;
import com.team.identify.IdentifyAPI.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;

@Tag(name = "User", description = "Management utilities for the currently authenticated principal")
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Operation(
            summary = "Get info about the user's account",
            description = "Returns information about the currently authenticated user principal"
    )
    @GetMapping("/")
    public ResponseEntity<UserInfoResponse> getUserInfo(Authentication auth){

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername()).get(); // user should be logged in or api wouldn't work

        return ResponseEntity.ok(userService.getUserInfo(currentUser));

    }

    @Operation(
            summary = "Update the user's email address",
            description = "Sets the authenticated user's email address, requires newEmail to be set in the request"
    )
    @PostMapping("/email")
    public ResponseEntity<MessageResponse> updateEmail(@RequestBody UserInfoUpdateRequest requestInfo, Authentication auth){

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername()).get();

        // checks if email already exists in DB for another user
        if(userRepository.existsByEmail(requestInfo.getNewEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(requestInfo.getNewEmail() + " is already in use."));
        }

        currentUser.setEmail(requestInfo.getNewEmail());
        userRepository.save(currentUser);

        return ResponseEntity.ok().body(new MessageResponse("Your email has been changed to " + requestInfo.getNewEmail()));
    }

    @Operation(
            summary = "Change the user's password",
            description = "Changes the password of the currently authenticated user, requires oldPassword and newPassword to be set"
    )
    @PostMapping("/password")
    public ResponseEntity<MessageResponse> updatePassword(@RequestBody UserInfoUpdateRequest requestInfo, Authentication auth) throws InvalidKeyException {

        try{
            // checks if new/old passwords match before continuing
            if (requestInfo.getNewPassword().equals(requestInfo.getOldPassword())) return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Old and New passwords match"));

            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
            User currentUser = userRepository.findByUsername(userDetails.getUsername()).get();

            userService.changePassword(currentUser, requestInfo.getOldPassword(), requestInfo.getNewPassword());

        } catch (NullPointerException e){ // if current password is incorrect pKey will be null
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Password change failed due to current password being incorrect"));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse("Password change failed."));
        }


        return ResponseEntity.ok().body(new MessageResponse("Your password has been updated successfully."));
    }
}
