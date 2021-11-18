package com.alike;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * This class will output permutations of an array list so that we dont have to store all permuatations of an array list
 * in memory.
 * @param <T> The object type in the array list that will be permuted.
 */
public class Permuter<T> {

    private ArrayList<T> currentPerm;
    private int currentIdx = 0;

    public Permuter(ArrayList<T> array) {
        setCurrentPerm(array);
    }

//    public ArrayList<T> getNextPerm() {
//
//    }


    public void printPermutations(ArrayList<T> arr, int currentIdx) {
        if (currentIdx == arr.size() - 1) {
            System.out.println(arr + " : " + currentIdx);
            return;
        }
        for (int i = currentIdx; i < arr.size(); i++) {
            Collections.swap(arr, i, currentIdx);
            printPermutations(arr, currentIdx + 1);
            Collections.swap(arr, i, currentIdx);
        }
    }

    public void printPermutations() {
        printPermutations(currentPerm, 0);
    }



    public ArrayList<T> getCurrentPerm() {
        return currentPerm;
    }

    public void setCurrentPerm(ArrayList<T> currentPerm) {
        this.currentPerm = currentPerm;
    }
}
