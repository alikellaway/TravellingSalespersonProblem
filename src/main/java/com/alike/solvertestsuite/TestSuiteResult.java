package com.alike.solvertestsuite;

import java.util.ArrayList;

/**
 * Class used to hold information about a test suite's execution.
 */
public class TestSuiteResult {
    /**
     * The list of @code{Solution}s output by the test suite.
     */
    private ArrayList<TestOutput> results;

    /**
     * Constructs a new TestSuiteResult object.
     * @param solutions The array list containing the solution objects output by the solver.

     */
    public TestSuiteResult(ArrayList<TestOutput> solutions) {
        setResults(solutions);

    }

    /**
     * Returns the value of the @code{results} attribute.
     * @return results The value of the @code{results} attribute.
     */
    public ArrayList<TestOutput> getResults() {
        return results;
    }

    /**
     * Sets the @code{solutions} attribute to a new value.
     * @param results The new value to assign the @code{results} attribute.
     */
    public void setResults(ArrayList<TestOutput> results) {
        this.results = results;
    }
}
