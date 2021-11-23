package com.alike;



import com.alike.customexceptions.PermutationExhaustionException;
import com.alike.customexceptions.PermutationFocusException;
import com.alike.solution_helpers.Permuter;

import java.util.ArrayList;


public class Main2 {
    public static void main(String[] args) throws PermutationExhaustionException {
        ArrayList<Integer> a = new ArrayList<>();
        a.add(1);
        a.add(2);
        a.add(3);




        Permuter<Integer> p1 = new Permuter<>(a);
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
