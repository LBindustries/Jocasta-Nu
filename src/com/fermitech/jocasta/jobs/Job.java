package com.fermitech.jocasta.jobs;
import java.io.*;
import java.util.stream.Stream;

public abstract class Job {
    protected String source;
    protected String destination;
    protected boolean status;
    protected File file;
    protected static long max_size = 8*1024;

    public Job(String source, String destination) throws FileNotFoundException {
        this.source = source;
        this.destination = destination;
        this.status = false;
    }

    public String getDestination() {
        return destination;
    }

    public String getSource() {
        return source;
    }

    public Boolean getStatus(){
        return status;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void execute() throws FileNotFoundException, IOException {
        this.file = new File(source);
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

    }

    @Override
    public String toString(){
        return source + " " + destination+" ";
    }
}
