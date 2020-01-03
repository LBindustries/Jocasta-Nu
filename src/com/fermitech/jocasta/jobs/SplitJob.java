package com.fermitech.jocasta.jobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public abstract class SplitJob extends InJob {

    int division_value;

    public SplitJob(String source, String destination, int division_value) throws FileNotFoundException {
        super(source, destination);
        this.division_value = division_value;
    }
}
