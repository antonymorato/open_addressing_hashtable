/*
 * Copyright (c) 2020.
 * Anton Korol
 */

package org.korol.hashmap;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class OpenAddressingHashMapTest  {

    private final int INITIAL_SIZE=500;
    private OpenAddressingHashMap hashMap;

    @Before
    public void setUp() {
        hashMap=new OpenAddressingHashMap();
    }

    @Test
    public void removeTest(){
        int key=55;
        long firstValue=1333;
        long secondValue=2444;
        hashMap.put(key,firstValue);
        hashMap.put(key,secondValue);
        hashMap.remove(key);
        try {
            assertEquals(secondValue,hashMap.get(key));
        } catch (NoSuchElementExeption noSuchElementExeption) {
            noSuchElementExeption.printStackTrace();
        }
    }

    @Test
    public void getTest(){
        int key=172;
        long value=228;
        hashMap.put(key,value);
        try {
            assertEquals(value,hashMap.get(key));
        } catch (NoSuchElementExeption noSuchElementExeption) {
            noSuchElementExeption.printStackTrace();
        }
    }

    @Test
    public void sizeTest(){
        int expectedSize=99;
        for (int i = 0; i <expectedSize ; i++) {
            hashMap.put(i*10,i+50);
        }
        assertEquals(expectedSize,hashMap.size());
    }

    @Test
    public void amplificationOfCapacityTest() throws NoSuchFieldException, IllegalAccessException {
        Field capacityField=OpenAddressingHashMap
                .class.getDeclaredField("capacity");
        capacityField.setAccessible(true);
        int oldCapacity=capacityField.getInt(hashMap);

        Field loadFactorField=OpenAddressingHashMap
                .class.getDeclaredField("loadFactor");
        loadFactorField.setAccessible(true);
        double loadFactor=loadFactorField.getDouble(hashMap);

        Field amplificationCoefficientField=OpenAddressingHashMap
                .class.getDeclaredField("amplificationCoefficient");
        amplificationCoefficientField.setAccessible(true);
        double amplificationCoeffiecient=amplificationCoefficientField
                .getDouble(hashMap);

        for (int index=0;index<oldCapacity*loadFactor;index++){
            hashMap.put((index+20)*5,index*20+100);
        }
        int newCapacity=capacityField.getInt(hashMap);

        capacityField.setAccessible(false);
        loadFactorField.setAccessible(false);
        amplificationCoefficientField.setAccessible(false);

        assertEquals(oldCapacity,newCapacity);
    }

}