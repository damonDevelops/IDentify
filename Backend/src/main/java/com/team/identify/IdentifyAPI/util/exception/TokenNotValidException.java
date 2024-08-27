package com.team.identify.IdentifyAPI.util.exception;

public class TokenNotValidException extends Exception{
    public TokenNotValidException(String token) {
        super("The given token is not valid: " + token);
    }
}
