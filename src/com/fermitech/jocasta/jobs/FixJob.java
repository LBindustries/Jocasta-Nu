package com.fermitech.jocasta.jobs;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * This is the FixJob class, a specialized class for Jobs that need to stitch together files.
 */
public class FixJob extends OutJob {
    private Vector<File> files;
    private long total_size;

    /**
     * This is the FixJob class constructor.
     * It initializes at null the files vector, which will contain all the files associated to the one saved in file,
     * and total_size, which will contain the total size of the final size.
     *
     * @param source      the absolute path to the source file, which may not exist yet.
     * @param destination the absolute path to the destination file.
     */
    public FixJob(String source, String destination) throws FileNotFoundException {
        super(source, destination, "joca");
        files = null;
        total_size = 0;
    }

    /**
     * This is the locateFiles method.
     * It's used to locate the files that need to be stitched together with the file contained in the file variable.
     * It creates a vector that contains all the files in a folder, and checks if all the extensions (minus the last
     * one, which is a number that differs from file to file) and filenames match up with the ones of the file variable.
     * Before returning the vector, it sorts it.
     *
     * @return the list of files that are related to file.
     */
    private File[] locateFiles() {
        Vector<File> correct = new Vector<File>();
        File folder = new File((file.getAbsoluteFile().getParent()));
        File[] all_files = folder.listFiles();
        for (File tmp : all_files) {
            String[] companion_components = tmp.getName().split("\\.");
            if (name_components[0].equals(companion_components[0]) && name_components.length == companion_components.length) {
                boolean flag = true;
                for (int i = 1; i < name_components.length - 1; i++) {
                    if (!name_components[i].equals(companion_components[i])) {
                        flag = false;
                    }
                }
                if (flag) {
                    correct.add(tmp);
                    total_size = total_size + tmp.length();
                }
            }
        }
        File[] result = new File[correct.size()];
        for (File tmp : correct) {
            String[] companion_components = tmp.getName().split("\\.");
            result[Integer.parseInt(companion_components[companion_components.length - 1]) - 1] = tmp;
        }
        return result;
    }

    /**
     * This is the CryptoJob specialized execute method.
     * It uses the inherited execute() to initialize file and stream. The stream gets closed, and the files are
     * localized using the locateFiles method.
     * Inside the for cycle, a new FileInputStream gets open and put inside the stream variable. After the
     * BufferOutputStream initialization, control is given to bufferControl, and then clean-up operations start:
     * outputStream gets closed, stream gets closed.
     */

    @Override
    public void execute() throws IOException {
        super.execute();
        stream.close();
        File[] filearray = locateFiles();
        //long leftover = total_size % filearray.length;
        //long test = leftover;
        //int counter = 0;
        delete(destination + file.separator + nextFileNameGenerator());
        for (File current : filearray) {
            this.stream = new FileInputStream(current);
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination + file.separator + nextFileNameGenerator(), true));
            bufferControl(current.length(), outputStream);
            //if (counter == filearray.length && leftover > 0) {
            //    bufferControl(file.length(), outputStream);
            //    writer(outputStream, leftover);
            //}
            outputStream.close();
            stream.close();
            //counter++;
        }
    }

    @Override
    public String toString() {
        return super.toString() + " Fix";
    }
}
