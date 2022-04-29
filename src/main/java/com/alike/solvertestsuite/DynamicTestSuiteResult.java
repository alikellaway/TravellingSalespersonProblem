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
    private ArrayList<DynamicTestResult> results;

    /**
     * The node speed the tests were run with.
     */
    private int nodeSpeed;

    /**
     * Whether the @code{DynamicGraph} had random movement turned on.
     */
    private boolean randomMovement;

    /**
     * Whether the @code{DynamicGraph} had velocity movement turned on.
     */
    private boolean velocityMovement;

    /**
     * Creates a new @code{DynamicTestSuiteResult} object.
     * @param results The results the test suite created.
     * @param numSolves The number of time the solver had to solve each graph.
     * @param delayPerSolve The forced delay between each solve the solver made.
     * @param randomMovement Whether the test graphs had random movement.
     * @param velocityMovement Whether the test graphs had velocity movement.
     */
    public DynamicTestSuiteResult(ArrayList<DynamicTestResult> results, int numSolves, int delayPerSolve, int nodeSpeed, boolean randomMovement, boolean velocityMovement) {
        setResults(results);
        setNumSolves(numSolves);
        setDelayPerSolve(delayPerSolve);
        setNodeSpeed(nodeSpeed);
        setRandomMovement(randomMovement);
        setVelocityMovement(velocityMovement);
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
    public ArrayList<DynamicTestResult> getResults() {
        return results;
    }

    /**
     * Assigns the @code{results} attribute.
     * @param results The value to assign the @code{results} attribute.
     */
    public void setResults(ArrayList<DynamicTestResult> results) {
        this.results = results;
    }

    /**
     * Returns the value of the @code{nodeSpeed} attribute.
     * @return nodeSpeed The value of the @code{nodeSpeed} attribute.
     */
    public int getNodeSpeed() {
        return nodeSpeed;
    }

    /**
     * Assigns the @code{nodeSpeed} attribute.
     * @param nodeSpeed The value to assign the @code{nodeSpeed} attribute.
     */
    public void setNodeSpeed(int nodeSpeed) {
        this.nodeSpeed = nodeSpeed;
    }

    /**
     * Returns the @code{randomMovement} attribute.
     * @return randomMovement The @code{randomMovement} attribute.
     */
    public boolean isRandomMovement() {
        return randomMovement;
    }

    /**
     * Assigns the @code{randomMovement} attribute.
     * @param randomMovement The value to assign the @code{randomMovement} attribute.
     */
    public void setRandomMovement(boolean randomMovement) {
        this.randomMovement = randomMovement;
    }

    /**
     * Returns the @code{velocityMovement} attribute.
     * @return velocityMovement The @code{velocityMovement} attribute.
     */
    public boolean isVelocityMovement() {
        return velocityMovement;
    }

    /**
     * Assigns the @code{velocityMovement} attribute.
     * @param velocityMovement The value to assign the @code{velocityMovement} attribute.
     */
    public void setVelocityMovement(boolean velocityMovement) {
        this.velocityMovement = velocityMovement;
    }

    /**
     * Returns the average of the average route lengths of the results.
     * @return avg The average of the average route lengths.
     */
    public double getAverageAvgRoute() {
        double total = 0;
        for (DynamicTestResult r : results) {
            total += r.getSol().getAvgLength();
        }
        return total/results.size();
    }

    /**
     * Returns the average of the average solve times output by DynamicSolver tests.
     * @return avg The average of the average time value in the results.
     */
    public long getAverageAvgTime() {
        long total = 0;
        for (DynamicTestResult r : results) {
            total += r.getSol().getAvgSolveTime();
        }
        return total/results.size();
    }
}
