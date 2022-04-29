package com.alike.customexceptions;

/**
 * Exception used to throw an error when two edges attempt to join the same nodes.
 * @author alike
 */
public class EdgeSuperimpositionException extends SuperimpositionException {
    /**
     * Creates a new @code{EdgeSuperimpositionException} object.
     * @param message The message to be displayed when stacktrace is printed.
     */
    public EdgeSuperimpositionException(String message) {
        super(message);
    }
}
