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

    /**
     * When this test took place chronologically amongst other tests (allows us to differentiate tests if needed).
     */
    private int testNumber;

    public DynamicTestResult(DynamicSolution sol, long totalTime, int testNumber) {
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

    /**
     * Returns the @code{testNumber} attribute.
     * @return testNumber The @code{testNumber} attribute.
     */
    public int getTestNumber() {
        return testNumber;
    }

    /**
     * Assigns the @code{testNumber} attribute.
     * @param testNumber The value to assign the @code{testNumber} attribute.
     */
    public void setTestNumber(int testNumber) {
        this.testNumber = testNumber;
    }

    /**
     * Returns the information in this test result as a string to be represented in console etc.
     * @return string The information in the test result as a string.
     */
    @Override
    public String toString() {
        return sol.toString() + ", totalling " + getTotalTimeTaken() + "ns";
    }
}
