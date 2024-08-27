package com.team.identify.IdentifyAPI.apps.crypt.utils;

import com.team.identify.IdentifyAPI.apps.crypt.pojo.UserPrivateKey;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

public class KeygenUtils {
    @Value("${identify.encryption.keygen.iterations:650000}")
    private static int iterations = 650000;

    @Value("${identify.encryption.keygen.salt:Identify123!}")
    private static String salt = "Identify123!";

    public static final int AES_KEY_LENGTH = 256;

    public static final int RSA_KEY_LENGTH = 2048;

    public static KeyPair generateRsaKeypair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(RSA_KEY_LENGTH);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException ignored) {
            return null;
        }
    }

    public static String getKeyHash(byte[] key) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] shaHash = digest.digest(key);
            return Base64.getEncoder().encodeToString(shaHash);
        } catch (NoSuchAlgorithmException ignored) {
            return null;
        }
    }

    /**
     * Takes a user's password and unencrypted AES private key and returns the UserPrivateKey
     * @param password plaintext password
     * @param privateKey User's AES private key
     * @return UserPrivateKey
     */
    public static UserPrivateKey encryptPrivateKeyWithPassword(String password, PrivateKey privateKey) {
        try {
            Cipher enc = Cipher.getInstance("PBEWithHmacSHA512AndAES_256");

            // generate random iv
            SecureRandom randomSecureRandom = new SecureRandom();
            byte[] ivBytes = new byte[enc.getBlockSize()];
            randomSecureRandom.nextBytes(ivBytes);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            // setup to encrypt data using java's password based encryption classes
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithHmacSHA512AndAES_256");
            PBEParameterSpec paramSpec = new PBEParameterSpec(salt.getBytes(), iterations, iv);
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
            SecretKey pbeKey = factory.generateSecret(pbeKeySpec);


            // do encryption of private key
            enc.init(Cipher.ENCRYPT_MODE, pbeKey, paramSpec);
            byte[] encryptedAesPrivateKey = enc.doFinal(privateKey.getEncoded());

            // hash key for later verification
            String b64Hash = getKeyHash(privateKey.getEncoded());
            return new UserPrivateKey(encryptedAesPrivateKey, iv, b64Hash);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException |
                 NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException |
                 BadPaddingException ignored) {
            return null;
        }
    }

    public static PrivateKey decryptPrivateKeyWithPassword(String password, UserPrivateKey encryptedKey)
            throws InvalidKeyException {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithHmacSHA512AndAES_256");
            PBEParameterSpec paramSpec = new PBEParameterSpec(salt.getBytes(), iterations);
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
            SecretKey pbeKey = factory.generateSecret(pbeKeySpec);
            Cipher enc = Cipher.getInstance("PBEWithHmacSHA512AndAES_256");

            PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt.getBytes(), iterations, encryptedKey.getIv());

            enc.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

            // decrypt key
            byte[] decrypted = enc.doFinal(encryptedKey.getEncryptedKeyBytes());

            // check signature
            if (!Objects.equals(encryptedKey.getSha256(), getKeyHash(decrypted))) {
                throw new InvalidKeyException(getKeyHash(decrypted));
            }
            KeyFactory kf =  KeyFactory.getInstance("RSA");
            return kf.generatePrivate(new PKCS8EncodedKeySpec(decrypted));

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                InvalidAlgorithmParameterException | IllegalBlockSizeException |
                BadPaddingException ignored) {
            return null;
        }
    }


}
