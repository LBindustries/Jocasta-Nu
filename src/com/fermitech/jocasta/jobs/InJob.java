package com.fermitech.jocasta.jobs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

/**
 * This is the InJob class, a specialized class for Jobs that create archives.
 */
public abstract class InJob extends Job {
    /**
     * This is the InJob class constructor.
     *
     * @param source      the absolute path to the source file, which may not exist yet.
     * @param destination the absolute path to the destination file.
     */
    public InJob(String source, String destination) throws FileNotFoundException {
        super(source, destination);
    }

    @Override
    public String toString() {
        return super.toString() + "Input ";
    }
}
