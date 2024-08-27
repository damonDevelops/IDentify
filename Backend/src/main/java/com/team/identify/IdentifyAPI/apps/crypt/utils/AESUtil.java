package com.team.identify.IdentifyAPI.apps.crypt.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public enum AESUtil {
    ;

    public static Key getKeyFromKeyGenerator(String cipher, int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(cipher);
        keyGenerator.init(keySize);
        return keyGenerator.generateKey();
    }

    public static String getBase64FromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static SecretKey getAesKeyFromBase64(String b64) {
        byte[] decodedKey = Base64.getDecoder().decode(b64);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public static byte[] encryptByteArray(byte[] bytes, Key aes256Key, IvParameterSpec iv) {
        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, aes256Key, iv);
            return c.doFinal(bytes);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    public static byte[] decryptByteArray(byte[] cypherText, Key aesKey, IvParameterSpec iv) {
        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, aesKey, iv);
            return c.doFinal(cypherText);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    public static String byteArrayToEncryptedBase64(byte[] bytes, Key aes256Key, IvParameterSpec iv) {
        byte[] enc = AESUtil.encryptByteArray(bytes, aes256Key, iv);
        return Base64.getEncoder().encodeToString(enc);
    }

    public static byte[] decryptBase64(String base64, Key aes256Key, IvParameterSpec iv) {
        try {
            byte[] decode = Base64.getDecoder().decode(base64);
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, aes256Key, iv);
            return c.doFinal(decode);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
}
