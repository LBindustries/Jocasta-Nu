package com.fermitech.jocasta.jobs;

import java.io.*;

public class OutJob extends Job {
    protected FileInputStream stream;
    protected String ext;
    protected String[] name_components;
    public OutJob(String source, String destination, String ext) throws FileNotFoundException {
        super(source, destination);
        this.ext = ext;
    }

    protected String nextFileNameGenerator() {
        String result = "";
        for (String tmp : name_components) {
            if (tmp.equals(this.ext)) {
                break;
            }
            if (result.equals("")) {
                result = tmp;
            } else {
                result = result + "." + tmp;
            }
        }
        return result;
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
        name_components = file.getName().split("\\.");
        this.stream = new FileInputStream(this.file);
    }

    @Override
    public String toString() {
        return super.toString() + "Output ";
    }
}
