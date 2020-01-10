package com.fermitech.jocasta.jobs;

import java.io.*;

/**
 * This is the InJob class, a specialized class for Jobs that opens archives.
 */
public abstract class OutJob extends Job {
    protected String ext;
    protected String[] name_components;

    /**
     * This is the OutJob class constructor.
     *
     * @param source      the absolute path to the source file, which may not exist yet.
     * @param destination the absolute path to the destination file.
     * @param ext         the extension of the file for this job
     */
    public OutJob(String source, String destination, String ext) throws FileNotFoundException {
        super(source, destination);
        this.ext = ext;
    }

    /**
     * This is the nextFileNameGenerator method. It's used to evaluate the name of the file after processing.
     * It gradually builds a string containing the filename, and stops as soon as it finds the current job's extension.
     */
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

    /**
     * This is the OutJob specialized execute method.
     * It initializes the vector of extensions of the file.
     */
    @Override
    public void execute() throws IOException {
        super.execute();
        name_components = file.getName().split("\\.");
    }

    @Override
    public String toString() {
        return super.toString() + "Output ";
    }
}
