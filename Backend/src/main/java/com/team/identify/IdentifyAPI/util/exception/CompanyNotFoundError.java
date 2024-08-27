package com.team.identify.IdentifyAPI.util.exception;

import java.util.UUID;

public class CompanyNotFoundError extends RuntimeException {
    public CompanyNotFoundError(UUID id) {
        super("Company " + id.toString() + " not found");
    }

    public CompanyNotFoundError(String str, String type) {
        super("Company with " + type + " " + str + " not found.");
    }
}
