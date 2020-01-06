package com.fermitech.jocasta.jobs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

public abstract class InJob extends Job {
    protected static long max_size = 8*1024;
    protected FileInputStream stream;

    public InJob(String source, String destination) throws FileNotFoundException {
        super(source, destination);
        this.stream = new FileInputStream(this.file);
    }
    public InJob(String source, String destination, FileInputStream stream) throws FileNotFoundException {
        super(source, destination);
        this.stream = stream;
    }

    protected void bufferControl(long value, OutputStream outputStream) throws IOException {
        if(value >= max_size){
            long n_reads = value/max_size;
            long leftover_reads = value%max_size;
            for(int j=0; j<n_reads; j++){
                writer(outputStream, max_size);
            }
            if(leftover_reads>0){
                writer(outputStream, leftover_reads);
            }
        }
        else{
            writer(outputStream, value);
        }
    }

    protected void writer(OutputStream outputStream, long size) throws IOException {
        byte[] buffer = new byte[(int) size];
        int value = this.stream.read(buffer);
        if (value!=-1) {
            outputStream.write(buffer);
        }
    }

    @Override
    public String toString(){
        return super.toString()+"Input ";
    }
}
