package com.alike.customexceptions;

/**
 * Thrown when the coordinate file reader has viewed all coordinate lists in the file.
 */
public class CoordinateListExhaustionException extends Exception {
    public CoordinateListExhaustionException(String msg) {
        super(msg);
    }
}
