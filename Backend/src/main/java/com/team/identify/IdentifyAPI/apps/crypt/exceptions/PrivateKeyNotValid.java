package com.team.identify.IdentifyAPI.apps.crypt.exceptions;

public class PrivateKeyNotValid extends Exception{
    public PrivateKeyNotValid() {
        super("The given private key is not valid!");
    }
}
