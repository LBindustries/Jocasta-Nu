package com.fermitech.jocasta.jobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * This is the InJob class, a specialized class for Jobs that create splitted archives.
 */
public abstract class SplitJob extends InJob {

    int division_value;

    /**
     * This is the SplitJob class constructor.
     *
     * @param source         the absolute path to the source file, which may not exist yet.
     * @param destination    the absolute path to the destination file.
     * @param division_value the value that is used to divide files. If the goal is to divide the file in a certain
     *                       numer of parts, it's the number of parts. If the goal is to divide the file into files of
     *                       a certain size, it's the size in Kb.
     */
    public SplitJob(String source, String destination, int division_value) throws FileNotFoundException {
        super(source, destination);
        this.division_value = division_value;
    }

    @Override
    public String toString() {
        return super.toString() + "Split ";
    }
}
