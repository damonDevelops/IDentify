package com.team.identify.IdentifyAPI.controller.company.advice;

import com.team.identify.IdentifyAPI.payload.response.MessageResponse;
import com.team.identify.IdentifyAPI.util.exception.MissingCompanyPermissionError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CompanyPermissionAdvice {
    @ExceptionHandler(MissingCompanyPermissionError.class)
    ResponseEntity<MessageResponse> companyNotFoundMessage(MissingCompanyPermissionError e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new MessageResponse(e.getMessage()));
    }
}
