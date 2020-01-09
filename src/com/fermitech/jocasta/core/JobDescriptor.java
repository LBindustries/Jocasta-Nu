package com.fermitech.jocasta.core;

import com.fermitech.jocasta.gui.FileInputOptions;
import com.fermitech.jocasta.jobs.*;
import com.fermitech.jocasta.core.Coda;

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
    Coda queue;
    String[] ext = null;
    String descrizione;
    private int id;
    private boolean valid;

    public JobDescriptor(FileInputOptions options, String src_path, String dst_path, int id) {
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
        this.queue = new Coda();
        this.tot_jobs = 0;
        this.curr_jobs = 0;
        this.descrizione = "";
        this.id = id;
        valid = true;
    }

    public JobDescriptor(String src_path, String dst_path, String password, int id) {
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
        this.queue = new Coda();
        this.descrizione = "";
        this.id = id;
        valid = true;
    }

    private void BuildOutJobs() throws FileNotFoundException {
        String src, dst;
        src = this.src_path;
        dst = this.dst_path;
        File tmp = new File(this.src_path);
        descrizione+="Crea ";
        if (!crypt && !compress) {
            if (cut_parts) {
                queue.add(new FixedNumberSplitJob(src, dst, this.division_value));
                descrizione+="Dividi in "+division_value+" file";
            } else {
                queue.add(new SizeSplitJob(src, dst, this.division_value));
                descrizione+="Dividi in file da "+division_value+" kb";
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
                descrizione+=" Cifra";
            }
            if (compress) {
                queue.add(new ZipJob(src, dst));
                src = dst + "/" + tmp.getName();
                if (crypt) {
                    src = src + ".cry";
                }
                src = src + ".zip";
                tot_jobs++;
                descrizione+=" Comprimi ";
            }
            if (cut_parts) {
                queue.add(new FixedNumberSplitJob(src, dst, this.division_value));
                descrizione+="Dividi in "+division_value+" file";
            } else {
                queue.add(new SizeSplitJob(src, dst, this.division_value));
                descrizione+="Dividi in file da "+division_value+" kb";
            }
            tot_jobs++;
            System.out.println(src);
        }
    }

    private void BuildInJobs() throws FileNotFoundException {
        String src, dst;
        src = this.src_path;
        dst = this.dst_path;
        descrizione += "Apri ";
        if (fix) {
            queue.add(new FixJob(src, dst));
            src = nextFileNameGenerator("joca");
            descrizione += "Ricompatta ";
        }
        if(decompress){
            queue.add(new UnZipJob(src, dst));
            src = nextFileNameGenerator("zip");
            descrizione += "Decomprimi ";
        }
        if(decrypt){
            queue.add(new DecryptJob(src, dst, password));
            src = nextFileNameGenerator("cry");
            descrizione += "Decifra ";
        }
    }

    public void BuildJobs() throws FileNotFoundException {
        if (cut_size || cut_parts) {
            BuildOutJobs();
        } else {
            BuildInJobs();
        }
    }

    //public void RunJobs() throws IOException {
    //    Iterator iterator = this.queue.iterator();
//
    //    while (iterator.hasNext()) {
    //        Job job = (Job) iterator.next();
    //        //System.out.println(job);
    //        job.execute();
    //        if (curr_jobs >= 1) {
    //            File file = new File(job.getSource());
    //            file.delete();
    //        }
    //        curr_jobs++;
    //    }
    //}

    public Job RunNextJob(){
        return queue.pop();
    }

    public Coda getQueue() {
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

    public String getDst_path() {
        return dst_path;
    }

    public String getSrc_path() {
        return src_path;
    }

    public int getDivision_value() {
        return division_value;
    }

    public boolean isCrypt() {
        return crypt;
    }

    public boolean isCompress() {
        return compress;
    }

    public boolean isCut_parts() {
        return cut_parts;
    }

    public boolean isCut_size() {
        return cut_size;
    }

    public boolean isDecompress() {
        return decompress;
    }

    public boolean isDecrypt() {
        return decrypt;
    }

    public boolean isError() {
        return error;
    }

    public boolean isFix() {
        return fix;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public int getCurr_jobs() {
        return curr_jobs;
    }

    public void setCurr_jobs(int curr_jobs) {
        this.curr_jobs = curr_jobs;
    }

    public int getId() {
        return id;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "Cr: " + this.crypt + " Co: " + this.compress + " Cp: " + this.cut_parts + " Cs: " + this.cut_size + " Pw: " + this.password + " Va: " + this.division_value + "\nIn: " + this.src_path + " Ou: " + this.dst_path;
    }
}
