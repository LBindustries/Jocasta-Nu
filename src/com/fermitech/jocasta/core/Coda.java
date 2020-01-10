package com.fermitech.jocasta.core;

import com.fermitech.jocasta.jobs.Job;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the Coda class, which implements basic queue logic using a list.
 */
public class Coda {
    private List<Job> list;

    /**
     * This is the constructor of Coda.
     * It initializes the list.
     */
    public Coda() {
        list = new ArrayList<Job>();
    }

    /**
     * This is the add method of Coda.
     * It adds an element at the end of the queue.
     */
    public void add(Job j) {
        list.add(j);
    }

    /**
     * This is the peek method of Coda.
     * It returns the last element of the list.
     *
     * @return the last element of the list, the one in top of the queue.
     */
    public Job peek() {
        Job j = list.get(list.size());
        return j;
    }

    /**
     * This is the pop method of Coda.
     * It returns the last element of the list, and deletes it from list.
     *
     * @return the last element of the list, the one in top of the queue.
     */
    public Job pop() {
        Job j = list.get(list.size());
        list.remove(list.size());
        return j;
    }

    /**
     * This is the getList method of Coda.
     * It returns the list.
     *
     * @return the list.
     */

    public List<Job> getList() {
        return list;
    }
}
