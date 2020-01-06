package com.fermitech.jocasta.core;

import com.fermitech.jocasta.gui.FileInputOptions;
import com.fermitech.jocasta.jobs.FixedNumberSplitJob;
import com.fermitech.jocasta.jobs.Job;
import com.fermitech.jocasta.jobs.SizeSplitJob;
import com.fermitech.jocasta.jobs.ZipJob;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class JobDescriptor {
    private boolean crypt, compress, cut_size, cut_parts ,decompress, decrypt, fix;
    private String src_path, dst_path, password;
    private int division_value;
    private boolean error;
    Queue<Job> queue;

    public JobDescriptor(FileInputOptions options, String src_path, String dst_path){
        this.crypt = options.getCrypt();
        this.compress = options.getComp();
        this.cut_size = options.getDim();
        this.cut_parts = options.getPart();
        this.password = options.getPassword();
        try {
            this.division_value = Integer.parseInt(options.get_value());
            this.error = false;
        }
        catch(NumberFormatException e){
            this.error = true;
        }
        this.src_path = src_path;
        this.dst_path = dst_path;
        this.queue = new LinkedList<Job>();

    }

    private void BuildOutJobs() throws FileNotFoundException{
        if(!crypt && !compress){
            if(cut_parts) {
                queue.add(new FixedNumberSplitJob(this.src_path, this.dst_path, this.division_value));
            }
            else{
                queue.add(new SizeSplitJob(this.src_path, this.dst_path, this.division_value));
            }
        }
        else {
            if (crypt) {
                queue.add(null);
            }
            if (compress) {
                queue.add(new ZipJob(this.src_path, this.dst_path));
            }
            if (cut_parts) {
                queue.add(new FixedNumberSplitJob(this.src_path, this.dst_path, this.division_value));
            } else {
                queue.add(new SizeSplitJob(this.src_path, this.dst_path, this.division_value));
            }
        }
    }

    private void BuildInJobs() throws FileNotFoundException {
        return;
    }

    public void BuildJobs() throws FileNotFoundException {
       if(cut_size || cut_parts){
           BuildOutJobs();
       }
       else{
           BuildInJobs();
       }
    }

    public void RunJobs() throws IOException {
        Iterator iterator = this.queue.iterator();
        while(iterator.hasNext()){
            Job job = (Job) iterator.next();
            job.execute();
        }

    }

    public Queue<Job> getQueue() {
        return queue;
    }

    public boolean getFlag(){
        return error;
    }


    @Override
    public String toString(){
        return "Cr: "+this.crypt+" Co: "+this.compress+" Cp: "+this.cut_parts+" Cs: "+this.cut_size+ " Pw: " + this.password + " Va: " + this.division_value +"\nIn: "+this.src_path+" Ou: "+this.dst_path;
    }
}
