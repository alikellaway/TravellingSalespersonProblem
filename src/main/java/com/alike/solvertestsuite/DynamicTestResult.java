package com.alike.solvertestsuite;

public class DynamicTestResult {
    /**
     * The number of solves the solver had to do in the test.
     */
    private int numSolves;

    /**
     * The total time taken to complete all the solves.
     */
    private long totalTime;

    public DynamicTestResult(DynamicSolution sol, int numSolves, long totalTime) {
        setNumSolves(numSolves);
        setTotalTime(totalTime);
    }

    public double getNumSolves() {
        return numSolves;
    }

    public void setNumSolves(int numSolves) {
        this.numSolves = numSolves;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }
}
