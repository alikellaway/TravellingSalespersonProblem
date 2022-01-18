package com.alike.customexceptions;

/**
 * Thrown when a HilbertFractalCurveSolver has constructed a curve that has missed a node during a solve.
 * @author alike
 */
public class NodeMissedException extends Exception {

    public NodeMissedException(String msg, int numNodesMissed) {
        super(msg);
    }
}
