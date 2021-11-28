package com.alike.customexceptions;

/**
 * Exception used to help identify when a @code{Permuter} object viewed all the permutations.
 * @author alike
 */
public class PermutationExhaustionException extends Exception {
    public PermutationExhaustionException(String msg) {
        super(msg);
    }
}
