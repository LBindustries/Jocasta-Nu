package com.fermitech.jocasta.core;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * This is the Enigma class, a class that takes care of handling the creation of a chipher using a string as key.
 */
public class Enigma {
    private Cipher chipher;
    private byte[] salt1;
    private byte[] salt2;

    /**
     * This is the Enigma constructor.
     * It tries to create the cipher.
     *
     * @param password the password.
     * @param crypt    the mode: if true, it crypts, else it decrypts.
     */
    public Enigma(String password, boolean crypt) {
        try {
            this.chipher = makeChipher(password, crypt);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is the genearateSalt method of Enigma.
     * It uses the password to create the two salts that are needed for encryption and decryption.
     *
     * @param password the password.
     */
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

    /**
     * This is the makeChipher method of Enigma.
     * It uses the password to create the two salts that are needed for encryption and decryption.
     * After generating the salts, it creates a KeySpec object. Then it initializes a SecretKeyFactory providing the
     * string PBKDF2WithHmacSHA512, which implies the use of the Password-Based Key Derivation Function 2 algorithm with
     * HMAC and SHA 512 as the key derivation algorithm. A secret key is then generated, and the chipher gets initialized
     * specifying the Encription Algorithm (AES), the mode of operation (CFB with 8-bit blocks) and the padding (which
     * in this case is disabled, since 8 bit-blocks are used).
     * The initialization value gets initialized using the 16-byte salt and the key gets encrypted using AES.
     * Accordingly to the boolean, the cipher ends the initialization.
     *
     * @param password the password.
     * @param crypt    the mode: if true, it crypts, else it decrypts.
     * @return the cipher
     */
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

    /**
     * This is the getChipher method of Enigma.
     * It returns the cipher.
     *
     * @return the cipher
     */
    public Cipher getChipher() {
        return chipher;
    }
}
