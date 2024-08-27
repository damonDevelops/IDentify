package com.team.identify.IdentifyAPI.controller.company.advice;

import com.team.identify.IdentifyAPI.payload.response.MessageResponse;
import com.team.identify.IdentifyAPI.util.exception.CompanyNotFoundError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CompanyNotFoundAdvice {

    @ExceptionHandler(CompanyNotFoundError.class)
    ResponseEntity<MessageResponse> companyNotFoundMessage(CompanyNotFoundError e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponse(e.getMessage()));
    }
}
