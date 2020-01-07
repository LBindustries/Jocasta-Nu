package com.fermitech.jocasta.jobs;

import java.io.*;
import java.util.List;
import java.util.Vector;

public class FixJob extends OutJob {
    private Vector<File> files;
    private String[] name_components;

    public FixJob(String source, String destination) throws FileNotFoundException {
        super(source, destination);
        files = null;
        name_components = null;
    }

    private Vector<File> locateFiles() {
        Vector<File> result = new Vector<File>();
        File folder = new File((file.getAbsoluteFile().getParent()));
        File[] all_files = folder.listFiles();
        name_components = file.getName().split("\\.");
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
                    result.add(tmp);
                }
            }
        }
        return result;
    }

    private String nextFileNameGenerator() {
        String result = "";
        for (String tmp : name_components) {
            if (tmp.equals("joca")) {
                break;
            }
            if (result == "") {
                result = tmp;
            } else {
                result = result + "." + tmp;
            }
        }
        return result;
    }

    @Override
    public void execute() throws IOException {
        super.execute();
        this.files = locateFiles();
        for (File current : files) {
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination + "/" + nextFileNameGenerator()));
            super.bufferControl(file.length(), outputStream);
            outputStream.close();
        }
        stream.close();
    }

    @Override
    public String toString() {
        return super.toString() + " Fix";
    }
}
