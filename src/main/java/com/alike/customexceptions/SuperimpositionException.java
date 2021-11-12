package com.alike.customexceptions;

/**
 * Exception thrown when an object is superimposed on another within the TSP space.
 * @author alike
 */
public class SuperimpositionException extends Exception {
    public SuperimpositionException(String message) {
        super(message);
    }
}
