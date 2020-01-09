package com.fermitech.jocasta.jobs;

import java.io.*;
/**
 * This is the SizeSplitJob class, a specialized class for Jobs that create splitted archives, divided in files with
 * size lower than n° Kb.
 */
public class SizeSplitJob extends SplitJob {
    /**
     * This is the SizeSplitJob class constructor.
     *
     * @param source      the absolute path to the source file, which may not exist yet.
     * @param destination the absolute path to the destination file.
     * @param division_value the size in Kb.
     */
    public SizeSplitJob(String source, String destination, int division_value) throws FileNotFoundException {
        super(source, destination, division_value*1024);
    }
    /**
     * This is the SizeSplitJob specialized execute method.
     * It uses the inherited execute() to initialize file and stream, and then calculates the number of files and
     * leftover bytes. It then uses a for cycle to write down the files using the bufferControl method, and if there are
     * leftover bytes they get written on a new file.
     */
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
