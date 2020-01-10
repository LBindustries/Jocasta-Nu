package com.fermitech.jocasta.jobs;

import java.io.*;

/**
 * This is the FixedNumberSplitJob class, a specialized class for Jobs that create splitted archives, divided in n files.
 */
public class FixedNumberSplitJob extends SplitJob {
    /**
     * This is the FixedNumberSplitJob class constructor.
     *
     * @param source      the absolute path to the source file, which may not exist yet.
     * @param destination the absolute path to the destination file.
     * @param division_value the number of requested parts.
     */
    public FixedNumberSplitJob(String source, String destination, int division_value) throws FileNotFoundException {
        super(source, destination, division_value);
    }
    /**
     * This is the FixedNumberSplitJob specialized execute method.
     * It uses the inherited execute() to initialize file and stream, and then calculates the size of each file and the
     * leftover bytes. Then it uses a for cycle to create n files of specified size, using the bufferControl method.
     */
    @Override
    public void execute() throws IOException {
        super.execute();
        long chunk_size = this.file.length()/division_value; // Calcolo della dimensione di ogni file
        long leftover = this.file.length()%division_value; // Calcolo dei byte che ne rimangono fuori alla fine
        for(int i=1; i<=this.division_value; i++){ // Ciclo di divisione dei file di dimensione fissa
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination+"/"+file.getName()+".joca."+i));
            super.bufferControl(chunk_size, outputStream);
            if(leftover>0){ // Scrittura dei dati avanzati
                writer(outputStream, leftover);
                leftover=0;
            }
            outputStream.close();
        }
        stream.close();
    }

    @Override
    public String toString(){
        return super.toString()+"Fixed Number";
    }
}
