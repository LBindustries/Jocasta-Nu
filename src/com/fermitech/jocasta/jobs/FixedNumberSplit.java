package com.fermitech.jocasta.jobs;

import java.io.*;

public class FixedNumberSplit extends SplitJob {
    public FixedNumberSplit(String source, String destination, int division_value) throws FileNotFoundException {
        super(source, destination, division_value);
    }

    @Override
    public void Execute() throws IOException {
        long chunk_size = (this.file.getTotalSpace()/division_value);
        long leftover = this.file.getTotalSpace()%division_value;
        for(int i=1; i<=this.division_value; i++){
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination+file.getName()+".joca."+i));
            if(chunk_size > 1024){
                long n_reads = chunk_size/1024;
                long leftover_reads = chunk_size%1024;
                for(int j=0; j<n_reads; j++){
                    writer(outputStream, 1024);
                }
                if(leftover_reads>0){
                    writer(outputStream, leftover_reads);
                }
            }
            else{
                writer(outputStream, chunk_size);
            }
        }
        if(leftover>0){
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination+file.getName()+".joca."+division_value+1));
        }
    }

    public void writer(BufferedOutputStream outputStream, long size) throws IOException{
        byte[] buffer = new byte[(int) size];
        int value = this.stream.read(buffer);
        outputStream.write(value);
    }
}
