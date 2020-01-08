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
    public Enigma(String password, boolean crypt) {
        try {
            this.chipher = makeChipher(password, crypt);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }
    // Salt1 and Salt2 are here just for a brief moment.
    private static final byte[] salt1 = {
            (byte) 0x43, (byte) 0x76, (byte) 0x95, (byte) 0xc7,
            (byte) 0x5b, (byte) 0xd7, (byte) 0x45, (byte) 0x17
    };

    private static final byte[] salt2 = {
            (byte) 0x43, (byte) 0x76, (byte) 0x95, (byte) 0xc7,
            (byte) 0x5b, (byte) 0xd7, (byte) 0x45, (byte) 0x17,
            (byte) 0x43, (byte) 0x76, (byte) 0x95, (byte) 0xc7,
            (byte) 0x5b, (byte) 0xd7, (byte) 0x45, (byte) 0x17
    };

    protected byte[] generateSalt(int size) {
        byte[] salt = new byte[size];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
        return salt;
    }

    private Cipher makeChipher(String password, boolean crypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
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
