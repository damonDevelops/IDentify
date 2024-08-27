package com.team.identify.IdentifyAPI.util.exception;

import com.team.identify.IdentifyAPI.model.User;

public class IPSUserIDException extends RuntimeException{
    public IPSUserIDException(Long userId) {
        super("User ID " + userId + " is already associated with an IPS");
    }

    public IPSUserIDException(User user) {
        super("User " + user.getUsername() + " is not associated with an IPS");
    }
}
