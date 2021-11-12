package com.alike.customexceptions;

/**
 * Exception used to throw an error when two edges attempt to join the same nodes.
 * @author alike
 */
public class EdgeSuperimpositionException extends SuperimpositionException {
    public EdgeSuperimpositionException(String message) {
        super(message);
    }
}
