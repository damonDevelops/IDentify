package com.team.identify.IdentifyAPI.util.exception;

public class CollectionPointNotFoundError extends RuntimeException{
    public CollectionPointNotFoundError(String collectionEndpoint) {
        super("The collection endpoint at /" + collectionEndpoint + " was not found.");
    }
}
