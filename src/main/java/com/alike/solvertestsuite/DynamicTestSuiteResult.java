package com.alike.solvertestsuite;

import java.util.ArrayList;

/**
 * Interface for data output by dynamic test suite.
 * @author alike
 */
public class DynamicTestSuiteResult {
    /**
     * The number of solves each solver had to output in its test.
     */
    private int numSolves;

    /**
     * The delay between each solver's solve that allowed the nodes to move.
     */
    private int delayPerSolve;

    /**
     * The DynamicSolution objects output from each graph the solver solved.
     */
    private ArrayList<DynamicSolution> results;

    /**
     * Creates a new @code{DynamicTestSuiteResult} object.
     * @param results The results the test suite created.
     * @param numSolves The number of time the solver had to solve each graph.
     * @param delayPerSolve The forced delay between each solve the solver made.
     */
    public DynamicTestSuiteResult(ArrayList<DynamicSolution> results, int numSolves, int delayPerSolve) {
        setResults(results);
        setNumSolves(numSolves);
        setDelayPerSolve(delayPerSolve);
    }

    /**
     * Returns the value of the @code{numSolves} attribute.
     * @return numSolves The value of the @code{numSolves} attribute.
     */
    public int getNumSolves() {
        return numSolves;
    }

    /**
     * Assigns the @code{numSolves} attribute.
     * @param numSolves The value to assign the @code{numSolves} attribute.
     */
    public void setNumSolves(int numSolves) {
        this.numSolves = numSolves;
    }

    /**
     * Returns the value of the @code{delayPerSolve} attribute.
     * @return delayPerSolve The value of the @code{delayPerSolve} attribute.
     */
    public int getDelayPerSolve() {
        return delayPerSolve;
    }

    /**
     * Assigns the @code{delayPerSolve} attribute.
     * @param delayPerSolve The value to assign the @code{delayPerSolve} attribute.
     */
    public void setDelayPerSolve(int delayPerSolve) {
        this.delayPerSolve = delayPerSolve;
    }

    /**
     * Returns the value of the @code{results} attribute.
     * @return results The value of the @code{results} attribute.
     */
    public ArrayList<DynamicSolution> getResults() {
        return results;
    }

    /**
     * Assigns the @code{results} attribute.
     * @param results The value to assign the @code{results} attribute.
     */
    public void setResults(ArrayList<DynamicSolution> results) {
        this.results = results;
    }
}
