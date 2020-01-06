package com.fermitech.jocasta.jobs;
import java.io.*;
import java.util.stream.Stream;

public abstract class Job {
    protected String source;
    protected String destination;
    protected boolean status;
    protected File file;

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

    @Override
    public String toString(){
        return source + " " + destination+" ";
    }
}
