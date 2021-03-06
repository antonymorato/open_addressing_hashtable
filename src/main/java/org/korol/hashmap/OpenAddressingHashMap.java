/*
 * Copyright (c) 2020.
 * Anton Korol
 */

package org.korol.hashmap;
/**
 * Simple implementation
 * of open addressing hashtable
 */

public class OpenAddressingHashMap {

    private Entry[] table;
    private int capacity;
    private int size;
    private double loadFactor;
    private double amplificationCoefficient;

    {
        capacity=16;
        loadFactor=0.75;
        amplificationCoefficient=1.5;
        size=0;
    }
    private class Entry {
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

    private int hash (int key){
        return key % capacity;
    }

    public OpenAddressingHashMap(){
        table=new Entry[capacity];
    }
    public OpenAddressingHashMap(int capacity){
        this.capacity=capacity;
        table=new Entry[capacity];
    }

    public int size(){
        return size;
    }

    public boolean put(int key,long value){
        if (needsToAmplify())
            amplifyTableWithNewSize();

        int hash=hash(key);
        if (table[hash]!=null){

            if (table[hash].isDeleted()) {
                table[hash] = new Entry(key, value);
                size++;
                return true;
            }
            int step=1;
            for (int i = hash + 1; i != hash; i = (i + step*step) % table.length) {
                if (table[i]!=null) {
                    if (table[i].isDeleted()) {
                        table[i] = new Entry(key, value);
                        size++;
                        return true;
                    }
                }
                else{
                    table[i]=new Entry(key, value);
                    size++;
                    return true;
                }
                step++;
            }
        }
        table[hash] = new Entry(key, value);
        size++;
        return true;
    }


    /**
     * @param key
     * @return value if exists
     * also can throw unchecked NullPointerException,
     * so you need to use
     * @method containsKey to check if the key is present
     */
    public long get(int key){
        int hash = hash(key);

        if (table[hash] != null){
            if (table[hash].getKey() == key && !table[hash].isDeleted()) {
                return table[hash].getValue();
            }
        }
        int step=1;
        for (int i = hash + 1; i != hash; i = (i + step*step) % table.length) {
            if (table[i]!=null) {
                if (table[i].getKey() == key && !table[i].isDeleted()) {
                    return table[i].getValue();
                }
            }
            step++;
        }
        throw new NullPointerException("No such element");
    }

    public boolean remove(int key) {
        int hash=hash(key);
        try{
            if (table[hash].getKey() == key) {
                table[hash].deleteEntry();
                return true;
            }
            int step=1;
            for (int i = hash + 1; i != hash; i = (i + step*step) % table.length) {
                if (table[i].getValue() == key && !table[i].isDeleted()) {
                    table[i].deleteEntry();
                    return true;
                }
                step++;
            }
            return false;
        } catch (NullPointerException e){
            return false;
        }
    }

    public boolean containsKey(int key){
        for (Entry el:table) {
            if (el != null) {
                if (el.getKey() == key)
                    return true;
            }
        }
        return false;
    }

    private boolean needsToAmplify(){
        return size > capacity * loadFactor?true:false;
    }

    private void amplifyTableWithNewSize(){
        size=0;
        capacity = (int) (capacity*amplificationCoefficient);
        Entry[] oldTable=table;
        table=new Entry[capacity];
        Entry temporary;
        for (int i = 0; i <oldTable.length; i++) {
            if (oldTable[i]!=null && !oldTable[i].isDeleted())
            put(oldTable[i].getKey(),oldTable[i].getValue());
        }
    }
}
