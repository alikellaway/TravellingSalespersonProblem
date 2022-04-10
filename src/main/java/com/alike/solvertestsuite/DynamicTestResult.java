package com.alike.solvertestsuite;

/**
 * Holds information output by a test of a dynamic solver.
 * @author alike
 */
public class DynamicTestResult {
    /**
     * The total time taken to complete all the solves.
     */
    private long totalTimeTaken;

    /**
     * The solution object output by a solver.
     */
    private DynamicSolution sol;

    public DynamicTestResult(DynamicSolution sol, long totalTime) {
        setTotalTimeTaken(totalTime);
        setSol(sol);
    }

    /**
     * Returns the value of the @code{totalTimeTaken} attribute.
     * @return totalTimeTaken The value of the @code{totalTimeTaken} attribute.
     */
    public long getTotalTimeTaken() {
        return totalTimeTaken;
    }

    /**
     * Assigns the value of the @code{totalTimeTaken} attribute.
     * @param totalTimeTaken The new value to assign the @code{totalTimeTaken} attribute.
     */
    public void setTotalTimeTaken(long totalTimeTaken) {
        this.totalTimeTaken = totalTimeTaken;
    }

    /**
     * Returns the value of the @code{sol} attribute.
     * @return sol The value of the @code{sol} attribute.
     */
    public DynamicSolution getSol() {
        return sol;
    }

    /**
     * Assigns the value of the @code{sol} attribute.
     * @param sol The new value to assign the @code{sol} attribute.
     */
    public void setSol(DynamicSolution sol) {
        this.sol = sol;
    }
}
