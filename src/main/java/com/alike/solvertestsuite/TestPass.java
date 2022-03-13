package com.alike.solvertestsuite;

import com.alike.staticgraphsystem.Graph;

public class TestPass implements TestOutput {
    /**
     * The test case number this test output is from.
     */
    private int testNumber;

    private Solution solution;

    public TestPass(Solution solution, int testNumber) {
        setSolution(solution);
        setTestNumber(testNumber);
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    @Override
    public int getTestNumber() {
        return 0;
    }

    @Override
    public void setTestNumber(int testNumber) {

    }

    @Override
    public boolean isFail() {
        return false;
    }
}
