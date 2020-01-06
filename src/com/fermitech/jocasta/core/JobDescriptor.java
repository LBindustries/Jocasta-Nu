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
        String src, dst;
        src = this.src_path;
        dst = this.dst_path;
        File tmp = new File(this.src_path);
        if(!crypt && !compress){
            if(cut_parts) {
                queue.add(new FixedNumberSplitJob(src, dst, this.division_value));
            }
            else{
                queue.add(new SizeSplitJob(src, dst, this.division_value));
            }
        }
        else {
            if (crypt) {
                try {
                    queue.add(new CryptoJob(src, dst, this.password));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                src = dst+"/"+tmp.getName()+".cry";
            }
            if (compress) {
                queue.add(new ZipJob(src, dst));
                src = dst+"/"+tmp.getName();
                if(crypt){
                    src = src+".cry";
                }
                src = src+".zip";
            }
            if (cut_parts) {
                queue.add(new FixedNumberSplitJob(src, dst, this.division_value));
            } else {
                queue.add(new SizeSplitJob(src, dst, this.division_value));
            }
            System.out.println(src);
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
            //System.out.println(job);
            job.execute();
        }
        //TODO: Find a way to delete temp files after processing.
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
