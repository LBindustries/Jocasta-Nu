package com.fermitech.jocasta.jobs;

import com.fermitech.jocasta.core.Enigma;

import javax.crypto.*;
import java.io.*;
import java.security.NoSuchAlgorithmException;
/**
 * This is the ZipJob class, a specialized class for Jobs that create encrypted archives.
 */
public class CryptoJob extends InJob {
    private String password;
    private Cipher cipher;
    /**
     * This is the CryptoJob class constructor.
     *
     * @param source      the absolute path to the source file, which may not exist yet.
     * @param destination the absolute path to the destination file.
     * @param password the string that is used to encrypt the file.
     */
    public CryptoJob(String source, String destination, String password) throws FileNotFoundException, NoSuchAlgorithmException {
        super(source, destination);
        this.password = password;
        if(this.password==null){
            this.password=" ";
        }
    }
    /**
     * This is the CryptoJob specialized execute method.
     * It uses the inherited execute() to initialize file and stream, and obtains a cipher using an Enigma object.
     * It then uses bufferControl to create the new file.
     */
    @Override
    public void execute() throws IOException {
        super.execute();
        Enigma enigma = new Enigma(password, true);
        this.cipher = enigma.getChipher();
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination+"/"+file.getName()+".cry"));
        super.bufferControl(file.length(), outputStream);
        outputStream.close();
        stream.close();
    }
    /**
     * This is the CryptoJob specialized writer method.
     * It creates a buffer of a given size, in which the contents of the file are written.
     * It then uses the cipher object to encrypt the buffer, which is then written on the disk.
     *
     * @param outputStream the OutputStream that will write the new file onto disk.
     * @param size the size of the next chunk.
     */
    @Override
    protected void writer(OutputStream outputStream, long size) throws IOException {
        byte[] buffer = new byte[(int) size];
        int value = this.stream.read(buffer);
        if (value!=-1) {
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
