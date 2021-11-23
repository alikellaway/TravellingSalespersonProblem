package com.alike;

import com.alike.customexceptions.PermutationExhaustionException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to calculate and manage the permutations of a list object.
 * @param <T> The object type in the list that will be permuted.
 */
public class Permuter<T> {
    /**
     * A container for all the permutations of our input list.
     */
    private List<List<T>> perms;
    /**
     * The index of the next output permutation when @code{getNextPerm) is called.
     */
    private int focusIdx = 0;

    public Permuter(List<T> inputArray) {
        perms = generatePermutations(inputArray);
        System.out.println(perms);
    }

    private List<List<T>> generatePermutations(List<T> inputArray) {
        // If an empty array list is entered, we have no permutations so output an empty permutation set.
        if (inputArray.isEmpty()) {
            List<List<T>> output = new ArrayList<>();
            output.add(new ArrayList<>());
            return output;
        }
        T firstElement = inputArray.remove(0);
        List<List<T>> output = new ArrayList<>();
        List<List<T>> permutations = generatePermutations(inputArray);
        for (List<T> smallerPermutated : permutations) {
            for (int idx = 0; idx <= smallerPermutated.size(); idx++) {
                List<T> temp = new ArrayList<>(smallerPermutated);
                temp.add(idx, firstElement);
                output.add(temp);
            }
        }
        return output;
    }


    public List<T> getNextPermutation() throws PermutationExhaustionException {
        if (focusIdx >= perms.size()) {
            throw new PermutationExhaustionException("No more permutations to view in the Permuter object.");
        }
        List<T> nextPerm = perms.get(focusIdx);
        focusIdx++;
        return nextPerm;
    }
}
