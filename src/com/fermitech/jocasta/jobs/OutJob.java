package com.fermitech.jocasta.jobs;

import java.io.*;

public class OutJob extends Job {
    protected FileInputStream stream;
    public OutJob(String source, String destination) throws FileNotFoundException {
        super(source, destination);
    }

    @Override
    protected void writer(OutputStream outputStream, long size) throws IOException {
        byte[] buffer = new byte[(int) size];
        int value = this.stream.read(buffer);
        if (value != -1) {
            outputStream.write(buffer);
        }
    }

    @Override
    public void execute() throws IOException {
        super.execute();
        this.stream = new FileInputStream(this.file);
    }

    @Override
    public String toString() {
        return super.toString() + "Output ";
    }
}
