package com.alike.solution_helpers;

import com.alike.customexceptions.PermutationExhaustionException;
import com.alike.customexceptions.PermutationFocusException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to calculate and manage the permutations of a list object.
 * @author alike
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

    /**
     * Boolean describing whether the focus idx has passed over every permutation.
     */
    boolean unseenPermutations;

    /**
     * Constructs a new Permuter object and calculates all the permutations.
     * @param inputArray The list of which we wish to find the permutations of.
     */
    public Permuter(List<T> inputArray) {
        perms = generatePermutations(inputArray);
        unseenPermutations = !perms.isEmpty();
    }

    /**
     * Generates the permutations of the elements in the input lists and sets the @code{perms} attribute equal to that.
     * @param inputArray The list of which to calculate the permutations of.
     * @return output A list of permutations of the input array.
     */
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

    /**
     * Returns the next permutation in the list of permutations.
     * @return nextPerm The next permutation in the list of permutations.
     * @throws PermutationExhaustionException Thrown if we exhaust the permutations in the list.
     */
    public List<T> getNextPermutation() throws PermutationExhaustionException {
        if (focusIdx >= perms.size()) {
            throw new PermutationExhaustionException("No more permutations to view in the Permuter object.");
        }
        List<T> nextPerm = perms.get(focusIdx);
        incrementFocusIdx();
        return nextPerm;
    }

    /**
     * Returns the permutation that is currently being viewed.
     * @return perm The permutation that is currently being viewed.
     * @throws PermutationFocusException Thrown if the method is called when no permutation is being viewed.
     */
    public List<T> getCurrentPermutation() throws PermutationFocusException {
        if (focusIdx != 0) {
            return perms.get(focusIdx - 1); // Need to subtract
        } else {
            throw new PermutationFocusException("Not looking at a permutation.");
        }
    }

    /**
     * Used to increment the focus index and checks if we have reached the end.
     */
    private void incrementFocusIdx() {
        focusIdx++;
        if (focusIdx == perms.size()) {
            unseenPermutations = false;
        }
    }

    /**
     * Returns the value of the @code{unseenPermutations} attribute.
     * @return unseenPermutations The value of the @code{unseenPermutations} attribute.
     */
    public boolean hasUnseenPermutations() {
        return this.unseenPermutations;
    }


}
