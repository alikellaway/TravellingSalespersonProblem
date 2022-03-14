package com.alike.solvertestsuite;

/**
 * Inteface is used to allow us to manage and interact with solver outputs, whether they failed or passed.
 */
public interface TestOutput {
    int getTestNumber();
    void setTestNumber(int testNumber);
    boolean isFail();
}
