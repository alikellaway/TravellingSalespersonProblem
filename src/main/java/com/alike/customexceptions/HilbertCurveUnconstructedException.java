package com.alike.customexceptions;

/**
 * Used to show that an attempt has been made to use a hilbert fractal solver to solve a graph but a curve has not yet
 * been constructed.
 */
public class HilbertCurveUnconstructedException extends Exception {
    public HilbertCurveUnconstructedException(String msg) {
        super(msg);
    }
}
