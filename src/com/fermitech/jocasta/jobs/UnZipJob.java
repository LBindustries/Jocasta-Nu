package com.fermitech.jocasta.jobs;

import java.io.*;
import java.util.zip.InflaterInputStream;
import java.util.zip.InflaterOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This is the UnzipJob class, a specialized class for Jobs that need to decrypt files.
 */
public class UnZipJob extends OutJob {
    /**
     * This is the DecryptJob class constructor.
     *
     * @param source      the absolute path to the source file, which may not exist yet.
     * @param destination the absolute path to the destination file.
     */
    public UnZipJob(String source, String destination) throws FileNotFoundException {
        super(source, destination, "zip");
    }
    /**
     * This is the UnZipJob specialized execute method.
     * It uses the inherited execute() to initialize file and stream, and then creates a ZipInputStream. As long as
     * there are entries, it creates a buffer which then gets filled with data form the ZipInputStream and then written
     * on the destination file.
     */
    @Override
    public void execute() throws IOException {
        super.execute();
        ZipInputStream zip = new ZipInputStream(new BufferedInputStream(this.stream));
        ZipEntry entry;
        while ((entry = zip.getNextEntry()) != null) {
            byte[] buffer = new byte[(int) max_size];
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination + "/" + nextFileNameGenerator()), (int) max_size);
            int size;
            while ((size = zip.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, size);
            }
            outputStream.flush();
            outputStream.close();
        }
        zip.close();
        stream.close();
    }

    @Override
    public String toString() {
        return super.toString() + "UnZip ";
    }
}
