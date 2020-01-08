package com.fermitech.jocasta.jobs;

import com.fermitech.jocasta.core.Enigma;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class DecryptJob extends OutJob {
    private String password;
    private Cipher cipher;

    public DecryptJob(String source, String destination, String password) throws FileNotFoundException {
        super(source, destination, "cry");
        Enigma enigma = new Enigma(password, false);
        this.cipher = enigma.getChipher();
        this.password = password;
        this.max_size = 4*1024;
    }

    @Override
    public void execute() throws IOException {
        super.execute();
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination+"/"+nextFileNameGenerator()));
        super.bufferControl(file.length(), outputStream);
        outputStream.close();
        stream.close();
    }

    @Override //codice ripetuto, ma non so bene come sistemarlo. Forse potrei metterlo dentro la classe enigma.
    protected void writer(OutputStream outputStream, long size) throws IOException {
        byte[] buffer = new byte[(int) size];
        int value = this.stream.read(buffer);
        if (value!=-1) {
            byte[] tmp = null;
            buffer = cipher.update(buffer); // Qui qualcosa non sta funzionando come dovrebbe
            outputStream.write(buffer);
        }
    }

    @Override
    public String toString() {
        return super.toString() + "Crypto ";
    }
}
