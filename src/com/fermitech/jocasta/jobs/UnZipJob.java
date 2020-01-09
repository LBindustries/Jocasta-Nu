package com.fermitech.jocasta.jobs;

import java.io.*;
import java.util.zip.InflaterInputStream;
import java.util.zip.InflaterOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnZipJob extends OutJob {
    public UnZipJob(String source, String destination, String ext) throws FileNotFoundException {
        super(source, destination, ext);
    }

    @Override
    public void execute() throws IOException {
        super.execute();
        ZipInputStream zip = new ZipInputStream(new BufferedInputStream(this.stream));
        ZipEntry entry;
        while((entry=zip.getNextEntry())!=null){
            byte[] buffer = new byte[(int)max_size];
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination+"/"+nextFileNameGenerator()), (int) max_size);
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
