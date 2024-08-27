package com.team.identify.IdentifyAPI.apps.crypt.pojo;

import com.team.identify.IdentifyAPI.model.User;

import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;

public class UserPrivateKey {
    private String b64EncryptedKey;

    private String b64Iv;

    private String sha256;

    public UserPrivateKey(String b64EncryptedKey, String b64Iv) {
        this.b64EncryptedKey = b64EncryptedKey;
        this.b64Iv = b64Iv;
    }

    public UserPrivateKey(byte[] encryptedKey, IvParameterSpec iv, String sha256) {
        this.b64EncryptedKey = Base64.getEncoder().encodeToString(encryptedKey);
        this.b64Iv = Base64.getEncoder().encodeToString(iv.getIV());

        this.sha256 = sha256;
    }

    public UserPrivateKey(User user) {
        this.b64EncryptedKey = user.getEncryptedPrivateKey();
        this.b64Iv = user.getPrivateKeyIv();
        this.sha256 = user.getPrivateKeySha256();
    }

    public String getB64EncryptedKey() {
        return b64EncryptedKey;
    }

    public void setB64EncryptedKey(String b64EncryptedKey) {
        this.b64EncryptedKey = b64EncryptedKey;
    }

    public String getB64Iv() {
        return b64Iv;
    }

    public void setB64Iv(String b64Iv) {
        this.b64Iv = b64Iv;
    }

    public byte[] getEncryptedKeyBytes() {
        return Base64.getDecoder().decode(b64EncryptedKey);
    }

    public IvParameterSpec getIv() {
        byte[] ivBytes = Base64.getDecoder().decode(b64Iv);
        return new IvParameterSpec(ivBytes);
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }
}
