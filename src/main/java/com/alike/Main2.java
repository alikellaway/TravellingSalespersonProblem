package com.alike;



import com.alike.customexceptions.PermutationExhaustionException;
import com.alike.customexceptions.PermutationFocusException;

import java.util.ArrayList;


public class Main2 {
    public static void main(String[] args) throws PermutationExhaustionException {
        ArrayList<Integer> a = new ArrayList<>();
        a.add(1);
        a.add(2);
        a.add(3);




        Permuter<Integer> p1 = new Permuter<Integer>(a);
        try {
//        System.out.println(p1.getCurrentPermutation());
//        System.out.println(p1.getCurrentPermutation());
            System.out.println(p1.getNextPermutation());
            System.out.println(p1.getCurrentPermutation());
            System.out.println(p1.getNextPermutation());
            System.out.println(p1.getCurrentPermutation());
        } catch (PermutationFocusException e) {
            e.printStackTrace();
        }

    }

}
