package com.team.identify.IdentifyAPI.apps.crypt.utils;

import javax.crypto.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;

public class EncryptionUtils {
    public static byte[] encryptSecretKey(PublicKey pubKey, SecretKey aesKey) {
        return encryptKey(pubKey, aesKey);
    }

    private static byte[] encryptKey(PublicKey companyOwnerUserPublicKey, Key companySecretKey) {
        try {
            Cipher enc = Cipher.getInstance("RSA");
            enc.init(Cipher.ENCRYPT_MODE, companyOwnerUserPublicKey);
            ByteBuffer bb = ByteBuffer.allocate(3 + companySecretKey.getEncoded().length);
            bb.put("KEY".getBytes(StandardCharsets.UTF_8));
            bb.put(companySecretKey.getEncoded());
            return enc.doFinal(bb.array());
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException ignored) {
            return null;
        }
    }

    public static byte[] decryptKey(PrivateKey pKey, byte[] data) {
        try {
            Cipher dec = Cipher.getInstance("RSA");
            dec.init(Cipher.DECRYPT_MODE, pKey);
            return dec.doFinal(data);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            // we must have the wrong key
            return "BADKEY".getBytes(StandardCharsets.UTF_8);
        }
    }

}
