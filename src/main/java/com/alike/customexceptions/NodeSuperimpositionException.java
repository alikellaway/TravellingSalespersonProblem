package com.alike.customexceptions;

/**
 * Exception used to throw an error when two nodes attempt to occupy the same coordinate.
 * @author alike
 */
public class NodeSuperimpositionException extends SuperimpositionException {
    public NodeSuperimpositionException(String message) {
        super(message);
    }
}
