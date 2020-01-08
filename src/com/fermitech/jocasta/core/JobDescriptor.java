package com.fermitech.jocasta.core;

import com.fermitech.jocasta.gui.FileInputOptions;
import com.fermitech.jocasta.jobs.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class JobDescriptor {
    private boolean crypt, compress, cut_size, cut_parts, decompress, decrypt, fix;
    private String src_path, dst_path, password;
    private int division_value;
    private boolean error;
    private int tot_jobs, curr_jobs;
    Queue<Job> queue;
    String[] ext = null;

    public JobDescriptor(FileInputOptions options, String src_path, String dst_path) {
        this.crypt = options.getCrypt();
        this.compress = options.getComp();
        this.cut_size = options.getDim();
        this.cut_parts = options.getPart();
        this.password = options.getPassword();
        try {
            this.division_value = Integer.parseInt(options.get_value());
            this.error = false;
        } catch (NumberFormatException e) {
            this.error = true;
        }
        this.src_path = src_path;
        this.dst_path = dst_path;
        this.queue = new LinkedList<Job>();
        tot_jobs = 0;
        curr_jobs = 0;
    }

    public JobDescriptor(String src_path, String dst_path, String password) {
        File file = new File(src_path);
        this.ext = file.getName().split("\\.");
        for (String tmp : ext) {
            if (tmp.equals("cry")) {
                decrypt = true;
            } else if (tmp.equals("zip")) {
                decompress = true;
            } else if (tmp.equals("joca")) {
                fix = true;
            }
        }
        this.password = password;
        this.src_path=src_path;
        this.dst_path=dst_path;
        this.queue = new LinkedList<Job>();
    }

    private void BuildOutJobs() throws FileNotFoundException {
        String src, dst;
        src = this.src_path;
        dst = this.dst_path;
        File tmp = new File(this.src_path);
        if (!crypt && !compress) {
            if (cut_parts) {
                queue.add(new FixedNumberSplitJob(src, dst, this.division_value));
            } else {
                queue.add(new SizeSplitJob(src, dst, this.division_value));
            }
            tot_jobs++;
        } else {
            if (crypt) {
                try {
                    queue.add(new CryptoJob(src, dst, this.password));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                src = dst + "/" + tmp.getName() + ".cry";
                tot_jobs++;
            }
            if (compress) {
                queue.add(new ZipJob(src, dst));
                src = dst + "/" + tmp.getName();
                if (crypt) {
                    src = src + ".cry";
                }
                src = src + ".zip";
                tot_jobs++;
            }
            if (cut_parts) {
                queue.add(new FixedNumberSplitJob(src, dst, this.division_value));
            } else {
                queue.add(new SizeSplitJob(src, dst, this.division_value));
            }
            tot_jobs++;
            System.out.println(src);
        }
    }

    private void BuildInJobs() throws FileNotFoundException {
        String src, dst;
        src = this.src_path;
        dst = this.dst_path;
        if (fix) {
            queue.add(new FixJob(src, dst));
            src = nextFileNameGenerator("joca");
        }
        if(decrypt){
            queue.add(new DecryptJob(src, dst, password));
            src = nextFileNameGenerator("cry");
        }
        if(decompress){
            //queue.add(null);
        }
    }

    public void BuildJobs() throws FileNotFoundException {
        if (cut_size || cut_parts) {
            BuildOutJobs();
        } else {
            BuildInJobs();
        }
    }

    public void RunJobs() throws IOException {
        Iterator iterator = this.queue.iterator();

        while (iterator.hasNext()) {
            Job job = (Job) iterator.next();
            //System.out.println(job);
            job.execute();
            if (curr_jobs >= 1) {
                File file = new File(job.getSource());
                file.delete();
            }
            curr_jobs++;
        }
    }

    public Queue<Job> getQueue() {
        return queue;
    }

    public boolean getFlag() {
        return error;
    }

    protected String nextFileNameGenerator(String exte) {
        String result = "";
        for (String tmp : this.ext) {
            if (tmp.equals(exte)) {
                break;
            }
            if (result.equals("")) {
                result = tmp;
            } else {
                result = result + "." + tmp;
            }
        }
        return dst_path+"/"+result;
    }

    @Override
    public String toString() {
        return "Cr: " + this.crypt + " Co: " + this.compress + " Cp: " + this.cut_parts + " Cs: " + this.cut_size + " Pw: " + this.password + " Va: " + this.division_value + "\nIn: " + this.src_path + " Ou: " + this.dst_path;
    }
}
