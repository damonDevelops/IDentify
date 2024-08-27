package com.team.identify.IdentifyAPI.apps.crypt.pojo;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class UserKeyPair {
    private UserPrivateKey privateKey;

    private PublicKey publicKey;

    public UserKeyPair(UserPrivateKey privateKey, PublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public UserKeyPair(UserPrivateKey privateKey, String pubKey) {
        this.privateKey = privateKey;
        this.publicKey = UserKeyPair.getPublicKeyFromBase64(pubKey);
    }

    public UserPrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(UserPrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public String getPublicBase64() {
        return Base64.getEncoder().encodeToString(this.publicKey.getEncoded());
    }

    public static PublicKey getPublicKeyFromBase64(String b64PublicKey) {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(b64PublicKey)));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ignored) {
            return null;
        }
    }
}
