package com.fermitech.jocasta.jobs;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * This is the ZipJob class, a specialized class for Jobs that create compressed archives.
 */
public class ZipJob extends InJob {
    /**
     * This is the ZipJob class constructor.
     *
     * @param source      the absolute path to the source file, which may not exist yet.
     * @param destination the absolute path to the destination file.
     */
    public ZipJob(String source, String destination) throws FileNotFoundException {
        super(source, destination);
    }

    /**
     * This is the ZipJob specialized execute method.
     * It uses the inherited execute() to initialize file and stream, and then creates a ZipOutputStream. It then adds
     * the file to the zip entry list and uses bufferControl to write the file.
     */
    @Override
    public void execute() throws IOException {
        super.execute();
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(destination + "/" + file.getName() + ".zip"));
        zip.setMethod(ZipOutputStream.DEFLATED);
        zip.setLevel(9);
        zip.putNextEntry(new ZipEntry(file.getName()));
        bufferControl(file.length(), zip);
        zip.closeEntry();
        zip.finish();
        zip.close();
        stream.close();
    }

    @Override
    public String toString() {
        return super.toString() + "Zip ";
    }
}
