package com.alike.customexceptions;

/**
 * Exception used to help identify when a solution has encountered a node ID that is not in an initialized Node
 * object.
 * @author alike
 */
public class NonExistentNodeException extends Exception {
    /**
     * Constructs a new @code{NonExistentNodeException} object.
     * @param msg The message to be displayed when the stack trace is printed.
     */
    public NonExistentNodeException(String msg) {
        super(msg);
    }
}
