package com.alike.customexceptions;

/**
 * Exception used to help identify when a solution is fed an invalid graph.
 * @author alike
 */
public class InvalidGraphException extends Exception {
    public InvalidGraphException(String msg) {
        super(msg);
    }
}
