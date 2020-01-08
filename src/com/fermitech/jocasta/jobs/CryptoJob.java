package com.fermitech.jocasta.jobs;

import com.fermitech.jocasta.core.Enigma;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class CryptoJob extends InJob {
    private String password;
    private Cipher cipher;
    public CryptoJob(String source, String destination, String password) throws FileNotFoundException, NoSuchAlgorithmException {
        super(source, destination);
        this.password = password;
    }

    public CryptoJob(String source, String destination, FileInputStream stream, String password) throws FileNotFoundException {
        super(source, destination, stream);
        this.password = password;
    }

    //private static final byte[] salt = {
    //        (byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44,
    //        (byte) 0x45, (byte) 0x46, (byte) 0x47, (byte) 0x48
    //};
//
    //private Cipher makeChipher() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
    //    PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
    //    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
    //    SecretKey key = factory.generateSecret(keySpec);
    //    Cipher c = Cipher.getInstance("PBEWithMD5AndDES");
    //    PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 42);
    //    c.init(Cipher.ENCRYPT_MODE, key, pbeParamSpec);
    //    return c;
    //}

    @Override
    public void execute() throws IOException {
        super.execute();
        //try {
        //    cipher = makeChipher();
        //} catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | InvalidKeySpecException e) {
        //    e.printStackTrace();
        //}
        Enigma enigma = new Enigma(password, true);
        this.cipher = enigma.getChipher();
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination+"/"+file.getName()+".cry"));
        super.bufferControl(file.length(), outputStream);
        outputStream.close();
        stream.close();
    }

    @Override
    protected void writer(OutputStream outputStream, long size) throws IOException {
        byte[] buffer = new byte[(int) size];
        int value = this.stream.read(buffer);
        if (value!=-1) {
            //try {
            //    buffer = cipher.doFinal(buffer);
            //} catch (IllegalBlockSizeException | BadPaddingException e) {
            //    e.printStackTrace();
            //}
            byte[] tmp = null;
            tmp = cipher.update(buffer);
            outputStream.write(tmp);
        }
    }

    @Override
    public String toString() {
        return super.toString() + "Crypto ";
    }
}
