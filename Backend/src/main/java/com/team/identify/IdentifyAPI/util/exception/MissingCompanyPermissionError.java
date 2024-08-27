package com.team.identify.IdentifyAPI.util.exception;

import java.util.UUID;

public class MissingCompanyPermissionError extends RuntimeException {
    public MissingCompanyPermissionError(String username, UUID companyId, String permission) {
        super(username + " missing permission " + permission + " for company " + companyId.toString());
    }
}
