package com.alike.solvertestsuite;

public class TestResult {
    private SolverOutput result;
    private int testNumber;

    public TestResult(SolverOutput result, int testNumber) {
        setResult(result);
        setTestNumber(testNumber);
    }

    public SolverOutput getResult() {
        return result;
    }

    public void setResult(SolverOutput result) {
        this.result = result;
    }

    public int getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(int testNumber) {
        this.testNumber = testNumber;
    }
}
