package com.alike.customexceptions;

/**
 * Thrown when no closest node can be found to a node.
 */
public class NoClosestNodeException extends Exception {
    public NoClosestNodeException(String msg) {
        super(msg);
    }
}
