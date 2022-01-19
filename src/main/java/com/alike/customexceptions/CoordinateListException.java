package com.alike.customexceptions;

/**
 * Thrown when the coordinate file reader has viewed all coordinate lists in the file.
 */
public class CoordinateListException extends Exception {
    /**
     * Constructs a new CoordinateListException.
     * @param msg The message to pass to the exception super constructor.
     */
    public CoordinateListException(String msg) {
        super(msg);
    }
}
