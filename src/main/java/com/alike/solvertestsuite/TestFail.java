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
     * A reference to the exception thrown that failed the test if it was failed by an exception.
     */
    private Exception exception;

    /**
     * A reference to an error that may have failed a test.
     */
    private Error error;

    /**
     * A reference to the graph on which the solution failed.
     */
    private Graph graph;


    public TestFail(Exception e, int testNumber) {
        setFailure(e);
        setTestNumber(testNumber);
    }

    public TestFail(Error e, int testNumber) {
        setFailure(e);
        setTestNumber(testNumber);
    }

    /**
     * Used to set the @code{exception} attribute to a new value.
     * @param e The exception object which caused the test to fail.
     */
    public void setFailure(Exception e) {
        this.exception = e;
    }

    /**
     * Used to set the failure when the failure was an error (not an exception).
     * @param e The error that caused the failure.
     */
    public void setFailure(Error e) {
        this.error = e;
    }

    /**
     * Used to retrieve the error associated with the failed test if it was a failed test.
     * @return error The error added to the test object when it was constructed after a solver failed.
     */
    private Error getError() {
        return error;
    }

    /**
     * Used to retrieve the exception associated with the failed test if it was a failed test.
     * @return exception The exception added to the test object when it was constructed after a solver failed.
     */
    private Exception getException() {
        return exception;
    }

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

    /**
     * Returns the value of the @code{graph} attribute.
     * @return graph The value of the @code{graph} attribute.
     */
    public Graph getGraph() {
        return this.graph;
    }

    /**
     * Sets the value of the @code{graph} attribute to a new value.
     * @param graph The new value to assign the @code{graph} attribute.
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
    }
}
