package com.alike.solvertestsuite;

import com.alike.staticgraphsystem.Graph;

/**
 * Class used to represent a solver has failed to produce a solution on a graph within a test suite.
 */
public class TestFail implements TestOutput {
    /**
     * The test case number this test output is from.
     */
    private int testNumber;



    /**
     * Returns the value of the @code{testNumber} attribute.
     * @return tesNumber The value of the @code{testNumber} attribute.
     */
    @Override
    public int getTestNumber() {
        return this.testNumber;
    }

    /**
     * Sets the @code{testNumber} attribute to a new value.
     * @param testNumber The new value to assign the @code{testNumber} attribute.
     */
    @Override
    public void setTestNumber(int testNumber) {
        this.testNumber = testNumber;
    }

    /**
     * Returns true, since this is output by a failed test.
     * @return true This test was failed.
     */
    @Override
    public boolean isFail() {
        return true;
    }

}
