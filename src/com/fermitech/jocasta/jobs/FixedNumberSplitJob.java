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
        long chunk_size = this.file.length()/division_value; // Calcolo della dimensione di ogni file
        long leftover = this.file.length()%division_value; // Calcolo dei byte che ne rimangono fuori alla fine
        for(int i=1; i<=this.division_value; i++){ // Ciclo di divisione dei file di dimensione fissa
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination+"/"+file.getName()+".joca."+i));
            if(chunk_size > max_size){ // Per evitare di caricare in ram tutto il file, lo divido in segmenti di 8kb
                long n_reads = chunk_size/max_size;
                long leftover_reads = chunk_size%max_size;
                for(int j=0; j<n_reads; j++){
                    writer(outputStream, max_size);
                }
                if(leftover_reads>0){
                    writer(outputStream, leftover_reads);
                }
            } // Se l'elemento da copiare ha dimensione inferiore di 8kb, allora siamo a cavallo
            else{
                writer(outputStream, chunk_size);
            }
            outputStream.close();
        }
        if(leftover>0){ // Scrittura dei dati avanzati
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination+"/"+file.getName()+".joca."+(division_value)));
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
        return super.toString()+"Fixed Number";
    }
}
