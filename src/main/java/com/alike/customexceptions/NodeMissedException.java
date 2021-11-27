package com.alike.customexceptions;

/**
 * Thrown when a HilbertFractalCurveSolver has constructed a curve that has missed a node during a solve.
 */
public class NodeMissedException extends Exception {

    private int numNodesMissed;

    public NodeMissedException(String msg, int numNodesMissed) {
        super(msg);
        setNumNodesMissed(numNodesMissed);
    }

    public int getNumNodesMissed() {
        return numNodesMissed;
    }

    public void setNumNodesMissed(int numNodesMissed) {
        this.numNodesMissed = numNodesMissed;
    }
}
