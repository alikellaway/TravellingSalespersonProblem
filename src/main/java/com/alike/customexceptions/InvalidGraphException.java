package com.alike.customexceptions;

/**
 * Exception used to help identify when a solution is fed an invalid graph.
 * @author alike
 */
public class InvalidGraphException extends Exception {
    /**
     * Constructs a new @code{InvalidGraphException} object.
     * @param msg The message to be displayed when the stack trace is printed.
     */
    public InvalidGraphException(String msg) {
        super(msg);
    }
}
