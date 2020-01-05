package com.fermitech.jocasta.jobs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public abstract class InJob extends Job {
    protected FileInputStream stream;
    public InJob(String source, String destination) throws FileNotFoundException {
        super(source, destination);
        this.stream = new FileInputStream(this.file);
    }
    public InJob(String source, String destination, FileInputStream stream) throws FileNotFoundException {
        super(source, destination);
        this.stream = stream;
    }
    @Override
    public String toString(){
        return super.toString()+"Input ";
    }
}
