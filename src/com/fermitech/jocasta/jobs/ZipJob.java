package com.fermitech.jocasta.jobs;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipJob extends InJob{

    public ZipJob(String source, String destination) throws FileNotFoundException {
        super(source, destination);
    }

    public ZipJob(String source, String destination, FileInputStream stream) throws FileNotFoundException {
        super(source, destination, stream);
    }

    @Override
    public void execute() throws IOException{
        super.execute();
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(destination+"/"+file.getName()+".zip"));
        zip.setMethod(ZipOutputStream.DEFLATED);
        zip.setLevel(9);
        zip.putNextEntry(new ZipEntry(file.getName()));
        bufferControl(file.length(), zip);
        zip.closeEntry();
        zip.close();
        stream.close();
    }

    @Override
    public String toString(){
        return super.toString()+"Zip ";
    }
}
