package com.team.identify.IdentifyAPI.util.exception;

public class CardDataNotFound extends RuntimeException{

    public CardDataNotFound(String id) {
        super("Card data not found with id " + id);
    }
}
