package com.alike.customexceptions;

/**
 * Exception used to help identify when a @code{Permuter} object viewed all the permutations.
 * @author alike
 */
public class PermutationExhaustionException extends Exception {
    /**
     * Constructs a new @code{PermutationExhaustionException} object.
     * @param msg The message to be displayed when the stack trace is printed.
     */
    public PermutationExhaustionException(String msg) {
        super(msg);
    }
}
