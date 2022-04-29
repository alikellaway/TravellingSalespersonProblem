package com.alike.solvertestsuite;

/**
 * Used as a wrapper for test outputs.
 * @author alike
 */
public class TestResult {
    /**
     * The result of the test.
     */
    private SolverOutput result;

    /**
     * The test number.
     */
    private int testNumber;

    /**
     * Constructs a new test result object.
     * @param result The result of the test. The output of the solver.
     * @param testNumber The test number of this test in it's batch.
     */
    public TestResult(SolverOutput result, int testNumber) {
        setResult(result);
        setTestNumber(testNumber);
    }

    /**
     * Returns the value of the @code{result} attribute.
     * @return result The value of the @code{result} attribute.
     */
    public SolverOutput getResult() {
        return result;
    }

    /**
     * Sets the value of the @code{result} attribute.
     * @param result The new value to assign the @code{result} attribute.
     */
    public void setResult(SolverOutput result) {
        this.result = result;
    }

    /**
     * Sets the value of the @code{testNumber} attribute.
     * @param testNumber The new value to assign the @code{testNumber} attribute.
     */
    public void setTestNumber(int testNumber) {
        this.testNumber = testNumber;
    }
}
