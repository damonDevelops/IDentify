package com.team.identify.IdentifyAPI.controller.company.advice;

import com.team.identify.IdentifyAPI.payload.response.MessageResponse;
import com.team.identify.IdentifyAPI.util.exception.UserAlreadyInCompany;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserAlreadyExistsAdvice {
    @ExceptionHandler(UserAlreadyInCompany.class)
    ResponseEntity<MessageResponse> companyNotFoundMessage(UserAlreadyInCompany e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageResponse(e.getMessage()));
    }
}
