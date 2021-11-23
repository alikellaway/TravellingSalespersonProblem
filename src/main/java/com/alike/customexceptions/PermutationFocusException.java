package com.alike.customexceptions;

/**
 * Thrown by a @code{Permuter} object if its @code{focusIdx} attribute encounters list index errors.
 */
public class PermutationFocusException extends Exception {
    public PermutationFocusException(String msg) {
        super(msg);
    }
}
