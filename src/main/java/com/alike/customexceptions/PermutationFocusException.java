package com.alike.customexceptions;

/**
 * Thrown by a @code{Permuter} object if its @code{focusIdx} attribute encounters list index errors.
 * @author alike
 */
public class PermutationFocusException extends Exception {
    /**
     * Constructs a new @code{PermutationFocusException} object.
     * @param msg The message to be displayed when stack trace is printed.
     */
    public PermutationFocusException(String msg) {
        super(msg);
    }
}
