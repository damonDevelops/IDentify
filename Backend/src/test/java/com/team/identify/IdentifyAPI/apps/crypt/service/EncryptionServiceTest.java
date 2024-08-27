package com.team.identify.IdentifyAPI.apps.crypt.service;

import com.team.identify.IdentifyAPI.apps.crypt.exceptions.PrivateKeyNotValid;
import com.team.identify.IdentifyAPI.apps.crypt.pojo.EncryptedRow;
import com.team.identify.IdentifyAPI.apps.crypt.utils.KeygenUtils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;

class EncryptionServiceTest {

    @Test
    void oneUserEncryption() throws PrivateKeyNotValid {
        EncryptionService es = new EncryptionService();
        KeyPair kp = KeygenUtils.generateRsaKeypair();
        ArrayList<PublicKey> keyList = new ArrayList<>();
        byte[] data = "CHEESE".getBytes(StandardCharsets.UTF_8);
        keyList.add(kp.getPublic());
        EncryptedRow row = es.encryptData(keyList, data);
        System.out.println(row.toString());
        byte[] decrypted = es.decryptData(kp.getPrivate(), row);
        System.out.println(new String(decrypted, StandardCharsets.UTF_8));

    }

    @Test
    void twoUserEncryption() throws PrivateKeyNotValid {
        EncryptionService es = new EncryptionService();
        KeyPair kp = KeygenUtils.generateRsaKeypair();
        KeyPair kp2 = KeygenUtils.generateRsaKeypair();
        ArrayList<PublicKey> keyList = new ArrayList<>();
        byte[] data = "CHEESE".getBytes(StandardCharsets.UTF_8);
        keyList.add(kp.getPublic());
        keyList.add(kp2.getPublic());
        EncryptedRow row = es.encryptData(keyList, data);
        System.out.println(row.toString());
        byte[] decrypted = es.decryptData(kp2.getPrivate(), row);
        System.out.println(new String(decrypted, StandardCharsets.UTF_8));
    }

    @Test
    void serialisationOfEncryptedRow() throws PrivateKeyNotValid {
        EncryptionService es = new EncryptionService();
        KeyPair kp = KeygenUtils.generateRsaKeypair();
        KeyPair kp2 = KeygenUtils.generateRsaKeypair();
        ArrayList<PublicKey> keyList = new ArrayList<>();
        byte[] data = "CHEESE".getBytes(StandardCharsets.UTF_8);
        keyList.add(kp.getPublic());
        keyList.add(kp2.getPublic());
        EncryptedRow row = es.encryptData(keyList, data);

        String rowString = row.toString();
        EncryptedRow cereal = new EncryptedRow(rowString);

        byte[] decrypted = es.decryptData(kp2.getPrivate(), cereal);
        System.out.println(new String(decrypted, StandardCharsets.UTF_8));
    }

    @Test
    void encryptData() {
    }

    @Test
    void decryptData() {
    }

    @Test
    void addUserToRow() throws PrivateKeyNotValid {
        EncryptionService es = new EncryptionService();
        KeyPair kp = KeygenUtils.generateRsaKeypair();
        ArrayList<PublicKey> keyList = new ArrayList<>();
        byte[] data = "CHEESE".getBytes(StandardCharsets.UTF_8);
        keyList.add(kp.getPublic());
        EncryptedRow row = es.encryptData(keyList, data);
        System.out.println(row.toString());

        KeyPair newUser = KeygenUtils.generateRsaKeypair();
        es.addUserToRow(kp.getPrivate(), newUser.getPublic(), row);

        byte[] decrypted = es.decryptData(newUser.getPrivate(), row);
        System.out.println(new String(decrypted, StandardCharsets.UTF_8));
    }

}