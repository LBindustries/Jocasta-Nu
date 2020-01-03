package com.fermitech.jocasta.core;

import com.fermitech.jocasta.gui.FileInputOptions;
import com.fermitech.jocasta.jobs.Job;

import java.util.Vector;

public class JobDescriptor {
    private boolean crypt, compress, cut_size, cut_parts ,decompress, decrypt, fix;
    private String src_path, dst_path, password;
    private int division_value;
    private boolean error;

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
    }

    public Vector<Job> BuildJobs(){
        Vector<Job> newJobs = new Vector<Job>();
        return newJobs;
    }

    public boolean getFlag(){
        return error;
    }

    @Override
    public String toString(){
        return "Cr: "+this.crypt+" Co: "+this.compress+" Cp: "+this.cut_parts+" Cs: "+this.cut_size+ " Pw: " + this.password + " Va: " + this.division_value +"\nIn: "+this.src_path+" Ou: "+this.dst_path;
    }
}
