package com.fermitech.jocasta.jobs;

import java.io.*;
import java.util.stream.Stream;

/**
 * This is the main Job abstract class, from wich all jobs inherit the
 * basic attributes and bufferControl method.
 */
public abstract class Job {
    protected String source;
    protected String destination;
    protected File file;
    protected static long max_size = 8 * 1024;
    protected FileInputStream stream;

    /**
     * This is the Job class constructor.
     *
     * @param source      the absolute path to the source file, which may not exist yet.
     * @param destination the absolute path to the destination file.
     */
    public Job(String source, String destination) throws FileNotFoundException {
        this.source = source;
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }

    public String getSource() {
        return source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setSource(String source) {
        this.source = source;
    }

    /**
     * This is the execute method. Since daisy-chained jobs may contain non-existant files when
     * they are created, the file object gets initialized here. Same thing happens for the FileInputStream.
     */
    public void execute() throws FileNotFoundException, IOException {
        this.file = new File(source);
        this.stream = new FileInputStream(this.file);
    }

    /**
     * This is the bufferControl method. Since it would be unhealty to load an entire file into
     * memory, bufferControl loads a small chunk (max_size contains the maximum size of the chunk)
     * and takes care of the leftover bytes that do not fit a single read.
     *
     * @param value the size of the whole file.
     * @param outputStream the OutputStream that will write the new file onto disk.
     */
    protected void bufferControl(long value, OutputStream outputStream) throws IOException { //There might be something very wrong with this. Not everything gets transferred to disk.
        if (value > max_size) {
            long n_reads = value / max_size;
            long leftover_reads = value % max_size;
            for (int j = 0; j < n_reads; j++) {
                writer(outputStream, max_size);
            }
            if (leftover_reads > 0) {
                writer(outputStream, leftover_reads);
            }
        } else {
            writer(outputStream, value);
        }
    }
    /**
     * This is the writer method. It creates a buffer of given size (size<=max_size), reads from the input stream
     * a given quanity of bytes (his size) and if the read operation was successful, it writes onto the final file.
     *
     * @param outputStream the OutputStream that will write the new file onto disk.
     * @param size the size of the next chunk.
     */
    protected void writer(OutputStream outputStream, long size) throws IOException {
        byte[] buffer = new byte[(int) size];
        int value = this.stream.read(buffer);
        if (value != -1) {
            outputStream.write(buffer);
        }
    }

    @Override
    public String toString() {
        return source + " " + destination + " ";
    }
}
