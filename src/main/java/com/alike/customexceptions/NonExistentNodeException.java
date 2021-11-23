package com.alike.customexceptions;

public class NonExistentNodeException extends Exception {
    public NonExistentNodeException(String msg) {
        super(msg);
    }
}
