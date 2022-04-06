package com.alike.solvertestsuite;

public class DSolution implements SolverOutput {

    private double averageLength;
    private double timePeriod;

    public DSolution(double averageLength, double timePeriod) {
        setAverageLength(averageLength);
        setTimePeriod(timePeriod);
    }

    public double getAverageLength() {
        return averageLength;
    }

    public void setAverageLength(double averageLength) {
        this.averageLength = averageLength;
    }

    public double getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(double timePeriod) {
        this.timePeriod = timePeriod;
    }

    @Override
    public boolean isFail() {
        return false;
    }
}
