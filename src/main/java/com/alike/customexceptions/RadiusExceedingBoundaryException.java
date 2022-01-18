package com.alike.customexceptions;

/**
 * Used to show when the input radius of an irregular polygon graph is higher than the possible width or height for
 * that irregular polygon.
 */
public class RadiusExceedingBoundaryException extends Exception {
    public RadiusExceedingBoundaryException(String msg) {
        super(msg);
    }
}
