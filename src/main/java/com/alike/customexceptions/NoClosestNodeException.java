package com.alike.customexceptions;

/**
 * Thrown when no closest node can be found to a node.
 */
public class NoClosestNodeException extends Exception {
    /**
     * Constructs a new @code{NoClosestNodeException} object.
     * @param msg The message to be displayed when the stack trace is printed.
     */
    public NoClosestNodeException(String msg) {
        super(msg);
    }
}
