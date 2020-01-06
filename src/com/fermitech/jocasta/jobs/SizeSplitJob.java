package com.fermitech.jocasta.jobs;

import java.io.*;

public class SizeSplitJob extends SplitJob {
    private static long max_size = 8*1024;
    public SizeSplitJob(String source, String destination, int division_value) throws FileNotFoundException {
        super(source, destination, division_value*1000);
    }

    public SizeSplitJob(String source, String destination, FileInputStream stream, int division_value) throws FileNotFoundException {
        super(source, destination, stream, division_value*1000);
    }

    @Override
    public void execute() throws IOException{
        long parts = this.file.length()/(division_value); //Numero di parti da dividere data una certa dimensione in Kb
        long leftover = this.file.length()%(division_value); //Numero di Byte che avanzano
        int i;
        for(i=1; i<=parts; i++){
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination+"/"+file.getName()+".joca."+i));
            if(division_value > max_size){
                long n_reads = division_value/max_size;
                long leftover_reads = division_value%max_size;
                for(int j=0; j<n_reads; j++){
                    writer(outputStream, max_size);
                }
                if(leftover_reads>0){
                    writer(outputStream, leftover_reads);
                }
            }
            else{
                writer(outputStream, division_value);
            }
            outputStream.close();
        }
        if(leftover>0){
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination+"/"+file.getName()+".joca."+(i)));
            writer(outputStream, leftover);
            outputStream.close();
        }
        stream.close();
    }

    public void writer(BufferedOutputStream outputStream, long size) throws IOException{
        byte[] buffer = new byte[(int) size];
        int value = this.stream.read(buffer);
        if (value!=-1) {
            outputStream.write(buffer);
        }
    }

    @Override
    public String toString(){
        return super.toString()+"Fixed Size";
    }
}
