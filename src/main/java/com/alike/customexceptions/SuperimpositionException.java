package com.alike.customexceptions;

/**
 * Exception thrown when an object is superimposed on another within the TSP space.
 * @author alike
 */
public class SuperimpositionException extends Exception {
    /**
     * Constructs a new @code{SuperimpositionException}
     * @param message The message to be displayed in the stack trace when the error is thrown.
     */
    public SuperimpositionException(String message) {
        super(message);
    }
}
