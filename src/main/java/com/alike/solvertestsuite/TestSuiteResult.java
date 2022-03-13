package com.alike.solvertestsuite;

import java.util.ArrayList;

/**
 * Class used to hold information about a test suite's execution.
 */
public class TestSuiteResult {
    /**
     * The list of @code{Solution}s output by the test suite.
     */
    private ArrayList<Solution> solutions;

    /**
     * The number of tests that were failed in the test suite.
     */
    private int testsFailed;

    /**
     * The number of tests that were passed in the test suite.
     */
    private int testsPassed;

    /**
     * Constructs a new TestSuiteResult object.
     * @param solutions The array list containing the solution objects output by the solver.
     * @param testsPassed The number of tests passed in this test.
     * @param testsFailed The number of tests failed in this test.
     */
    public TestSuiteResult(ArrayList<Solution> solutions, int testsPassed, int testsFailed) {
        setSolutions(solutions);
        setTestsPassed(testsPassed);
        setTestsFailed(testsFailed);
    }

    /**
     * Returns the value of the @code{testFailed} attribute.
     * @return testsFailed The value of the @code{testsFailed} attribute.
     */
    public int getTestsFailed() {
        return testsFailed;
    }

    /**
     * Sets the @code{testsFailed} attribute to a new value.
     * @param testsFailed The new value to assign to the @code{testsFailed} attribute.
     */
    public void setTestsFailed(int testsFailed) {
        this.testsFailed = testsFailed;
    }

    /**
     * Returns the value of the @code{testsPassed} attribute.
     * @return testsPassed The value of the @code{testsPassed} attribute.
     */
    public int getTestsPassed() {
        return testsPassed;
    }

    /**
     * Sets the @code{testsPassed} attribute to a new value.
     * @param testsPassed The new value to assign to the @code{testsPassed} attribute.
     */
    public void setTestsPassed(int testsPassed) {
        this.testsPassed = testsPassed;
    }

    /**
     * Returns the value of the @code{solutions} attribute.
     * @return solutions The value of the @code{solutions} attribute.
     */
    public ArrayList<Solution> getSolutions() {
        return solutions;
    }

    /**
     * Sets the @code{solutions} attribute to a new value.
     * @param solutions The new value to assign the @code{solutions} attribute.
     */
    public void setSolutions(ArrayList<Solution> solutions) {
        this.solutions = solutions;
    }
}
