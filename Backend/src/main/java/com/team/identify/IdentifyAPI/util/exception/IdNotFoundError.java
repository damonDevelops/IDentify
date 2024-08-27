package com.team.identify.IdentifyAPI.util.exception;

public class IdNotFoundError extends Exception{
    public IdNotFoundError(String id) {
        super(id + " not found in the database");
    }
}
