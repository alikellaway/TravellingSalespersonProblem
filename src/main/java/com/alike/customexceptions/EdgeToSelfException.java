package com.alike.customexceptions;

/**
 * Exception thrown when an attempt is made to create an edge to and from the same node.
 * @author alike
 */
public class EdgeToSelfException extends Exception {
    /**
     * Constructs a new @code{EdgeToSelfException} object.
     * @param msg The message to be displayed when the stack trace is printed.
     */
    public EdgeToSelfException(String msg) {
        super(msg);
    }
}
