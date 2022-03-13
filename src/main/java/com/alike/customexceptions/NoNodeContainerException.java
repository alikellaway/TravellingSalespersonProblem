package com.alike.customexceptions;

/**
 * Thrown when an attempt is made to modify or interact with a graph that has no node container.
 * @author alike
 */
public class NoNodeContainerException extends Exception {
    /**
     * Constructs a new @code{NoNodeContainerException} object.
     * @param msg The message to be displayed when the stack trace is printed.
     */
    public NoNodeContainerException(String msg) {
        super(msg);
    }
}
