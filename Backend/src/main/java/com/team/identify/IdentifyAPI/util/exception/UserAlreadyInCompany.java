package com.team.identify.IdentifyAPI.util.exception;

import com.team.identify.IdentifyAPI.model.User;

public class UserAlreadyInCompany extends RuntimeException {
    public UserAlreadyInCompany(User user) {
        super("User " + user.getUsername() + " is already a member of the company!");
    }
}
