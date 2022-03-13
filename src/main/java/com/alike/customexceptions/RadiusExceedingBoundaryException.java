package com.alike.customexceptions;

/**
 * Used to show when the input radius of an irregular polygon graph is higher than the possible width or height for
 * that irregular polygon.
 */
public class RadiusExceedingBoundaryException extends Exception {
    /**
     * Creates a new @code{RadiusExccedingBoundaryException} object.
     * @param msg The message to be displayed when the stack trace is printed.
     */
    public RadiusExceedingBoundaryException(String msg) {
        super(msg);
    }
}
