package com.alike.customexceptions;

/**
 * Thrown when a HilbertFractalCurveSolver has constructed a curve that has missed a node during a solve.
 * @author alike
 */
public class NodeMissedException extends Exception {
    /**
     * Constructs a new @code{NodeMissedException) object.
     * @param msg The message to be displayed in the stack trace.
     */
    public NodeMissedException(String msg) {
        super(msg);
    }
}
