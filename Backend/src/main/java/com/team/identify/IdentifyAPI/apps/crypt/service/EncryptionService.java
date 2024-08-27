package com.team.identify.IdentifyAPI.apps.crypt.service;

import com.team.identify.IdentifyAPI.apps.crypt.exceptions.PrivateKeyNotValid;
import com.team.identify.IdentifyAPI.apps.crypt.pojo.EncryptedRow;
import com.team.identify.IdentifyAPI.apps.crypt.utils.AESUtil;
import com.team.identify.IdentifyAPI.apps.crypt.utils.EncryptionUtils;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class EncryptionService {


    public EncryptedRow encryptData(List<PublicKey> keysWithAccess, byte[] data) {
        try {
            SecretKey rowKey = (SecretKey) AESUtil.getKeyFromKeyGenerator("AES", 128);
            IvParameterSpec iv = AESUtil.generateIv();
            ByteBuffer bb = ByteBuffer.allocate(keysWithAccess.size() * 256);
            for (PublicKey userKey : keysWithAccess) {
                bb.put(EncryptionUtils.encryptSecretKey(userKey, rowKey));
            }

            byte[] encryptedData = AESUtil.encryptByteArray(data, rowKey, iv);

            return new EncryptedRow(bb.array(), encryptedData, iv.getIV());

        } catch (NoSuchAlgorithmException ignored) {
            throw new RuntimeException("Algorithm should be AES!");
        }
    }

    public byte[] decryptData(PrivateKey usersPrivateKey, EncryptedRow encryptedRow) throws PrivateKeyNotValid {
        return AESUtil.decryptByteArray(encryptedRow.getCypherText(),
                getSecretKey(usersPrivateKey, encryptedRow),
                encryptedRow.getIv());
    }

    public ArrayList<byte[]> decryptRowsToByteList(PrivateKey usersPrivateKey, List<EncryptedRow> encryptedRows) throws PrivateKeyNotValid {
        ArrayList<byte[]> output = new ArrayList<>();
        for (EncryptedRow row : encryptedRows) {
            output.add(
                    AESUtil.decryptByteArray(row.getCypherText(),
                            getSecretKey(usersPrivateKey, row),
                            row.getIv())
            );
        }
        return output;
    }

    public ArrayList<String> decryptRowsToStringList(PrivateKey usersPrivateKey, List<EncryptedRow> encryptedRows) throws PrivateKeyNotValid {
        ArrayList<byte[]> decryptedRows = decryptRowsToByteList(usersPrivateKey, encryptedRows);
        ArrayList<String> output = new ArrayList<>();
        for (byte[] row : decryptedRows) {
            output.add(new String(row, StandardCharsets.UTF_8));
        }
        return output;
    }

    public EncryptedRow addUserToRow(PrivateKey keyWithAccess, PublicKey usersPublicKey, EncryptedRow encryptedRow) throws PrivateKeyNotValid {
        encryptedRow.getRowKeychain().add(
                EncryptionUtils.encryptSecretKey(usersPublicKey, getSecretKey(keyWithAccess, encryptedRow)));
        return encryptedRow;
    }

    public EncryptedRow reEncryptRow(PrivateKey keyWithAccess, List<PublicKey> userList, EncryptedRow toReEncrypt) throws PrivateKeyNotValid{
        SecretKey aesKey = getSecretKey(keyWithAccess, toReEncrypt);
        byte[] data = AESUtil.decryptByteArray(toReEncrypt.getCypherText(), aesKey, toReEncrypt.getIv());
        return encryptData(userList, data);
    }

    private SecretKey getSecretKey(PrivateKey key, EncryptedRow encryptedRow) throws PrivateKeyNotValid {
        for (byte[] header : encryptedRow.getRowKeychain()) {
            byte[] decrypted = EncryptionUtils.decryptKey(key, header);
            ByteBuffer bb = ByteBuffer.wrap(decrypted);
            byte[] validationMsg = new byte[3];
            bb.get(validationMsg, 0, validationMsg.length);
            if (Arrays.equals(validationMsg, "KEY".getBytes(StandardCharsets.UTF_8))) {
                byte[] aesKey = new byte[16];
                bb.get(aesKey, 0, aesKey.length);
                return new SecretKeySpec(aesKey, "AES");
            }
        }
        throw new PrivateKeyNotValid();
    }
}
