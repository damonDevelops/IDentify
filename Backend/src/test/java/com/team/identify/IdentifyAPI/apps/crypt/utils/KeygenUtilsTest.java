package com.team.identify.IdentifyAPI.apps.crypt.utils;

import com.team.identify.IdentifyAPI.apps.crypt.pojo.UserPrivateKey;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

class KeygenUtilsTest {

    @Test
    void generateRsaKeypair() throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        KeyPair kp = KeygenUtils.generateRsaKeypair();
        assert kp != null;
        byte[] toEncrypt = AESUtil.getKeyFromKeyGenerator("AES", 128).getEncoded();
        // encryption
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, kp.getPublic());

        byte[] encryptedMessageBytes = encryptCipher.doFinal(toEncrypt);
        String encodedEncryptedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);

        // decryption
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, kp.getPrivate());

        byte[] decodedCypherText = Base64.getDecoder().decode(encodedEncryptedMessage);
        byte[] decryptedBytes = decryptCipher.doFinal(decodedCypherText);

        assert Arrays.equals(toEncrypt, decryptedBytes);
    }


    @Test
    void encryptionAndDecryptionOfPrivateKeyWithPassword() throws InvalidKeyException {
        String generatedString = RandomStringUtils.randomAlphanumeric(12);

        KeyPair kp = KeygenUtils.generateRsaKeypair();
        assert kp != null;
        UserPrivateKey userPriv = KeygenUtils.encryptPrivateKeyWithPassword(generatedString, kp.getPrivate());
        assert userPriv != null;
        PrivateKey decryptPrivKey = KeygenUtils.decryptPrivateKeyWithPassword(generatedString, userPriv);
        assert Objects.equals(kp.getPrivate(), decryptPrivKey);


    }

    @Test
    void generateCompanyKeypair() {

    }

}