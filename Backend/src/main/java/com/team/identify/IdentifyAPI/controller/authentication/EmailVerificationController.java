package com.team.identify.IdentifyAPI.controller.authentication;

import com.team.identify.IdentifyAPI.payload.request.EmailVerificationRequest;
import com.team.identify.IdentifyAPI.service.UserService;
import com.team.identify.IdentifyAPI.util.exception.TokenNotValidException;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailVerificationController {


    private final UserService userService;

    public EmailVerificationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/verify")
    public @ResponseBody ResponseEntity<?> submitVerification(
            @Parameter(description = "Email verification DTO")
            @RequestBody EmailVerificationRequest request
    ) {
        try {
            userService.completeUserVerification(request.getEmail(), request.getPassword(), request.getVerificationToken());
        } catch (TokenNotValidException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
