package org.korol.hashmap;

/*
 * Copyright (c) 2020.
 * Anton Korol
 *
 */


/**
 * class Entry
 * used to store information
 * about elements in hashmap array
 */
public class Entry {
    private final int key;
    private final long value;
    private boolean deleted;

    public Entry(int key, long value) {
        this.key = key;
        this.value = value;
        this.deleted=false;
    }

    public int getKey() {
        return key;
    }

    public long getValue() {
        return value;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public boolean deleteEntry(){
        if (!this.deleted){
            this.deleted=true;
            return true;
        }
        return false;
    }

}
