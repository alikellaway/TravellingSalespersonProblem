package com.alike.customexceptions;

/**
 * Thrown when the coordinate file reader has viewed all coordinate lists in the file.
 */
public class CoordinateListExhaustionException extends Exception {
    /**
     * Constructs a new CoordinateListExhaustionException.
     * @param msg The message to pass to the exception super constructor.
     */
    public CoordinateListExhaustionException(String msg) {
        super(msg);
    }
}
