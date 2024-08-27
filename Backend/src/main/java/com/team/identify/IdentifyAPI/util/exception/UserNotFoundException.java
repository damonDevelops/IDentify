package com.team.identify.IdentifyAPI.util.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Could not find user with id " + id);
    }

    public UserNotFoundException(String identifier) {
        super("Could not find user with identifier " + identifier);
    }

    public UserNotFoundException(String input, String type) {
        super("Could not find user with " + type + " " + input);
    }
}
