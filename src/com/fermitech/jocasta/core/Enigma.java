package com.fermitech.jocasta.core;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class Enigma {
    private Cipher chipher;
    private byte[] salt1;
    private byte[] salt2;
    public Enigma(String password, boolean crypt) {
        try {
            this.chipher = makeChipher(password, crypt);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    protected void generateSalt(String password) {
        byte[] p_bytes = password.getBytes();
        salt1 = new byte[8];
        for (int i = 0; i < p_bytes.length; i++) {
            salt1[i] = p_bytes[i];
        }
        if (p_bytes.length < 8) {
            for (int i = p_bytes.length; i < 8; i++) {
                salt1[i] = (byte) 0x00;
            }
        }
        salt2 = new byte[16];
        for (int i = 0; i < p_bytes.length; i++) {
            salt2[i] = p_bytes[i];
        }
        if (p_bytes.length < 16) {
            for (int i = p_bytes.length; i < 16; i++) {
                salt2[i] = (byte) 0x00;
            }
        }
    }

    private Cipher makeChipher(String password, boolean crypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        generateSalt(password);
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt1, 65535, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        SecretKey key = factory.generateSecret(keySpec);
        Cipher c = Cipher.getInstance("AES/CFB8/NoPadding");
        IvParameterSpec iv = new IvParameterSpec(salt2);
        SecretKey aes_key = new SecretKeySpec(key.getEncoded(), "AES");
        if (crypt) {
            c.init(Cipher.ENCRYPT_MODE, aes_key, iv);
        } else {
            c.init(Cipher.DECRYPT_MODE, aes_key, iv);
        }
        return c;
    }

    public Cipher getChipher() {
        return chipher;
    }
}
