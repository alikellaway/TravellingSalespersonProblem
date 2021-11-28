package com.alike.customexceptions;

/**
 * Exception thrown when an attempt is made to create an edge to and from the same node.
 * @author alike
 */
public class EdgeToSelfException extends Exception {
    public EdgeToSelfException(String msg) {
        super(msg);
    }
}
