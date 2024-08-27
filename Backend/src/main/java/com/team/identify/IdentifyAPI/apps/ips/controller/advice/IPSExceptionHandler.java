package com.team.identify.IdentifyAPI.apps.ips.controller.advice;

import com.team.identify.IdentifyAPI.payload.response.GenericErrorResponse;
import com.team.identify.IdentifyAPI.util.exception.IPSUserIDException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class IPSExceptionHandler {
    @ExceptionHandler(IPSUserIDException.class)
    @ResponseBody
    public ResponseEntity<GenericErrorResponse> genericHandler(HttpServletRequest request, IPSUserIDException ex) {
        return new ResponseEntity<GenericErrorResponse>(
                new GenericErrorResponse(ex.getMessage(), ExceptionUtils.getStackTrace(ex)),
                HttpStatus.BAD_REQUEST);
    }
}
