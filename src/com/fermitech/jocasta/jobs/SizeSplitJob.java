package com.fermitech.jocasta.jobs;

import java.io.*;

public class SizeSplitJob extends SplitJob {
    public SizeSplitJob(String source, String destination, int division_value) throws FileNotFoundException {
        super(source, destination, division_value*1024);
    }

    public SizeSplitJob(String source, String destination, FileInputStream stream, int division_value) throws FileNotFoundException {
        super(source, destination, stream, division_value*1024);
    }

    @Override
    public void execute() throws IOException{
        super.execute();
        long parts = this.file.length()/(division_value); //Numero di parti da dividere data una certa dimensione in Kb
        long leftover = this.file.length()%(division_value); //Numero di Byte che avanzano
        int i;
        for(i=1; i<=parts; i++){
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination+"/"+file.getName()+".joca."+i));
            super.bufferControl(division_value, outputStream);
            outputStream.close();
        }
        if(leftover>0){
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination+"/"+file.getName()+".joca."+(i)));
            writer(outputStream, leftover);
            outputStream.close();
        }
        stream.close();
    }

    @Override
    public String toString(){
        return super.toString()+"Fixed Size";
    }
}
