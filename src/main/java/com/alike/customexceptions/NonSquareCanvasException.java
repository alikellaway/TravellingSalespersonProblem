package com.alike.customexceptions;

/**
 * Thrown when a canvas input into a @code{HilbertFractalCurveAnimator} or @code{HilbertFractalCurveSolve} is not a
 * perfect square.
 * @author alike
 */
public class NonSquareCanvasException extends Exception {
    /**
     * Constructs a new @code{NonSquareCanvasException} object.
     * @param msg The message to be displayed when the stack trace is printed.
     */
    public NonSquareCanvasException(String msg) {
        super(msg);
    }
}
