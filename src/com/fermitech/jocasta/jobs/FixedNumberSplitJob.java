package com.fermitech.jocasta.jobs;

import java.io.*;

public class FixedNumberSplitJob extends SplitJob {
    private static long max_size = 8*1024;
    public FixedNumberSplitJob(String source, String destination, int division_value) throws FileNotFoundException {
        super(source, destination, division_value);
    }

    public FixedNumberSplitJob(String source, String destination, FileInputStream stream,int division_value) throws FileNotFoundException {
        super(source, destination, stream,division_value);
    }

    @Override
    public void execute() throws IOException {
        super.execute();
        long chunk_size = this.file.length()/division_value; // Calcolo della dimensione di ogni file
        long leftover = this.file.length()%division_value; // Calcolo dei byte che ne rimangono fuori alla fine
        for(int i=1; i<=this.division_value; i++){ // Ciclo di divisione dei file di dimensione fissa
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination+"/"+file.getName()+".joca."+i));
            super.bufferControl(chunk_size, outputStream);
            outputStream.close();
        }
        if(leftover>0){ // Scrittura dei dati avanzati
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination+"/"+file.getName()+".joca."+(division_value)));
            writer(outputStream, leftover);
            outputStream.close();
        }
        stream.close();
    }

    @Override
    public String toString(){
        return super.toString()+"Fixed Number";
    }
}
