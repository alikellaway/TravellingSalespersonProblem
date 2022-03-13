package com.alike.customexceptions;

/**
 * Exception used to throw an error when two nodes attempt to occupy the same coordinate.
 * @author alike
 */
public class NodeSuperimpositionException extends SuperimpositionException {
    /**
     * Constructs a new @code{NodeSuperimpositionException} object.
     * @param message The message to be displayed when the stack trace is printed.
     */
    public NodeSuperimpositionException(String message) {
        super(message);
    }
}
