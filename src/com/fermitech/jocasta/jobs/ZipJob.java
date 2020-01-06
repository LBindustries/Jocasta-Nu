package com.fermitech.jocasta.jobs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ZipJob extends InJob{

    public ZipJob(String source, String destination) throws FileNotFoundException {
        super(source, destination);
    }

    public ZipJob(String source, String destination, FileInputStream stream) throws FileNotFoundException {
        super(source, destination, stream);
    }
}
