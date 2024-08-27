package com.team.identify.IdentifyAPI.payload.response;

public class AESKeyResponse {
    private String keyB64;
    private String ivB64;

    public String getKeyB64() {
        return keyB64;
    }

    public void setKeyB64(String keyB64) {
        this.keyB64 = keyB64;
    }

    public String getIvB64() {
        return ivB64;
    }

    public void setIvB64(String ivB64) {
        this.ivB64 = ivB64;
    }

    public AESKeyResponse(String keyB64, String ivB64) {
        this.keyB64 = keyB64;
        this.ivB64 = ivB64;
    }

    public AESKeyResponse() {
    }
}
