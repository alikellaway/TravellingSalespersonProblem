package com.alike.customexceptions;

/**
 * Thrown when a canvas input into a @code{HilbertFractalCurveAnimator} or @code{HilbertFractalCurveSolve} is not a
 * perfect square.
 * @author alike
 */
public class NonSquareCanvasException extends Exception {
    public NonSquareCanvasException(String msg) {
        super(msg);
    }
}
