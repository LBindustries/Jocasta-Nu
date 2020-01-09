package com.fermitech.jocasta.core;

import com.fermitech.jocasta.jobs.Job;

import java.util.ArrayList;
import java.util.List;

public class Coda {
    private List<Job> list;
    public Coda(){
        list = new ArrayList<Job>();
    }

    public void add(Job j){
        list.add(j);
    }

    public Job peek(){
        Job j = list.get(list.size());
        return j;
    }

    public Job pop(){
        Job j = list.get(list.size());
        list.remove(list.size());
        return j;
    }

    public List<Job> getList() {
        return list;
    }
}
