package com.team.identify.IdentifyAPI.controller.advice;

import com.team.identify.IdentifyAPI.payload.response.InvalidArgumentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ValidationErrorAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ValidationErrorAdvice.class);

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> bodyNotReadableHandler(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(e.toString());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<InvalidArgumentResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, List<String>> details = new HashMap<>();

        BindingResult results = ex.getBindingResult();
        for (FieldError e : results.getFieldErrors()) {
            if (details.containsKey(e.getField()))
                details.get(e.getField()).add(e.getDefaultMessage());
            else
                details.put(e.getField(), Collections.singletonList(e.getDefaultMessage()));
        }


        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new InvalidArgumentResponse(ex.getDetailMessageCode(), details));
    }
}
