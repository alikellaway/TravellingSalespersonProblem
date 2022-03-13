package com.alike.customexceptions;

/**
 * Thrown when a fractal solver was unable to create a dense enough fractal to visit all nodes on a graph.
 * @author alike
 */
public class FractalDensityException extends Exception {
    /**
     * Constructs a new @code{FractalDensityException} object.
     * @param msg The message to be displayed when the stack trace is printed.
     */
    public FractalDensityException(String msg) {
        super(msg);
    }
}
