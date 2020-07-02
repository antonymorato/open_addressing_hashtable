/*
 * Copyright (c) 2020.
 * Anton Korol
 */

package org.korol.hashmap;

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
            for (int i = hash + 1; i != hash; i = (i + 1) % table.length) {
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
            }
        }
        table[hash] = new Entry(key, value);
        size++;
        return true;
    }

    public long get(int key) {
        int hash = hash(key);

        if (table[hash] != null){
            if (table[hash].getKey() == key && !table[hash].isDeleted()) {
                return table[hash].getValue();
            }
        }
            for (int i = hash + 1; i != hash; i = (i + 1) % table.length) {
               if (table[i]!=null) {
                   if (table[i].getKey() == key && !table[i].isDeleted()) {
                       return table[i].getValue();
                   }
               }
            }
            return 0;



    }
    @Override
    public String toString() {

        StringBuilder stringBuilder=new StringBuilder();
        for (int i = 0; i <table.length ; i++) {
            if (table[i]!=null) {
                stringBuilder.append(table[i].getKey()
                        + ":" + table[i].getValue() + "\n");
            }
        }




        return String.valueOf(stringBuilder);
    }
    public boolean remove(int key) {
        int hash=hash(key);
        try{
            if (table[hash].getKey() == key) {
                table[hash].deleteEntry();
                return true;
            }
            for (int i = hash + 1; i != hash; i = (i + 1) % table.length) {
                if (table[i].getValue() == key && !table[i].isDeleted()) {
                    table[i].deleteEntry();
                    return true;
                }
            }
            return false;
        } catch (NullPointerException e){
            return false;
        }
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
