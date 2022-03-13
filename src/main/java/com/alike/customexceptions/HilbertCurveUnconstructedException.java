package com.alike.customexceptions;

/**
 * Used to show that an attempt has been made to use a hilbert fractal solver to solve a graph but a curve has not yet
 * been constructed.
 */
public class HilbertCurveUnconstructedException extends Exception {
    /**
     * Constructs a new @code{HilberCurveUnconstructedException} object.
     * @param msg The message to be displayed when the stack trace is printed.
     */
    public HilbertCurveUnconstructedException(String msg) {
        super(msg);
    }
}
