package com.alike.customexceptions;

/**
 * Thrown when a fractal solver was unable to create a dense enough fractal to visit all nodes on a graph.
 * @author alike
 */
public class FractalDensityFailure extends Exception {
    public FractalDensityFailure(String msg) {
        super(msg);
    }
}
