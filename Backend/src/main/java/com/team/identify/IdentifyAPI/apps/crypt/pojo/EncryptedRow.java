package com.team.identify.IdentifyAPI.apps.crypt.pojo;

import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;

public class EncryptedRow implements Serializable {
    private ArrayList<byte[]> rowKeychain;
    private byte[] cypherText;

    private byte[] iv;

    @Serial
    private static final long serialVersionUID = 42L;


    // accepts a string in the following format
    /*
    b64EncodedEncryptedKey1;b64EncodedEncryptedKey2
     */
    public EncryptedRow(byte[] chain, byte[] data, byte[] iv) {
        rowKeychain = new ArrayList<>();
        // split chain every 256 bytes (RSA with 2048 bit key = 256 byte size output)
        ByteBuffer chainBuffer = ByteBuffer.wrap(chain);
        for (int i = 1; i <= (chain.length / 256); i++) {
            byte[] key = new byte[256];
            chainBuffer.get(key, 0, key.length);
            rowKeychain.add(key);
        }
        this.iv = iv;
        this.cypherText = data;
    }

    public EncryptedRow(String row) {
        this(Base64.getDecoder().decode(row.split(";")[0]),
                Base64.getDecoder().decode(row.split(";")[1]),
                Base64.getDecoder().decode(row.split(";")[2]));
    }

    public EncryptedRow() {
    }

    public void setFromString(String input) {
        byte[] chain = Base64.getDecoder().decode(input.split(";")[0]);
        byte[] data = Base64.getDecoder().decode(input.split(";")[1]);
        byte[] iv = Base64.getDecoder().decode(input.split(";")[2]);
        rowKeychain = new ArrayList<>();
        // split chain every 256 bytes (RSA with 2048 bit key = 256 byte size output)
        ByteBuffer chainBuffer = ByteBuffer.wrap(chain);
        for (int i = 1; i <= (chain.length / 256); i++) {
            byte[] key = new byte[256];
            chainBuffer.get(key, 0, key.length);
            rowKeychain.add(key);
        }
        this.iv = iv;
        this.cypherText = data;
    }

    public EncryptedRow(EncryptedRow value) {
        this();
        if (!value.isNull()){
            this.iv = value.iv;
            this.rowKeychain = value.rowKeychain;
            this.cypherText = value.cypherText;
        }
    }


    public ArrayList<byte[]> getRowKeychain() {
        return rowKeychain;
    }

    public void setRowKeychain(ArrayList<byte[]> rowKeychain) {
        this.rowKeychain = rowKeychain;
    }

    public byte[] getCypherText() {
        return cypherText;
    }

    public void setCypherText(byte[] cypherText) {
        this.cypherText = cypherText;
    }

    public IvParameterSpec getIv() {
        return new IvParameterSpec(this.iv);
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }

    // returns a string in the following format
    /*
    b64EncodedEncryptedKey1;b64EncodedEncryptedKey2
     */
    public String toString() {
        if (!this.isNull()) {
            ByteBuffer bb = ByteBuffer.allocate(256 * rowKeychain.size());
            for (byte[] array : rowKeychain) {
                bb.put(array);
            }
            String header = Base64.getEncoder().encodeToString(bb.array());
            String data = Base64.getEncoder().encodeToString(cypherText);
            String iv = Base64.getEncoder().encodeToString(this.iv);
            return header.concat(";").concat(data).concat(";").concat(iv);
        } else {
            return null;
        }
    }

    public boolean isNull() {
        return cypherText == null || iv == null || rowKeychain == null;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(this.toString());
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String data = (String) in.readObject();
        this.setFromString(data);
    }

    @Serial
    private Object writeReplace() throws ObjectStreamException {
        return "";
    }

}
