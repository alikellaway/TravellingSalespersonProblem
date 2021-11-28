package com.alike.customexceptions;

/**
 * Exception used to help identify when a solution has encountered a node ID that is not in an initialized TSPNode
 * object.
 * @author alike
 */
public class NonExistentNodeException extends Exception {
    public NonExistentNodeException(String msg) {
        super(msg);
    }
}
