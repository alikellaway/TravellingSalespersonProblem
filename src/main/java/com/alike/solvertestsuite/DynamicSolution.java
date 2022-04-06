package com.alike.solvertestsuite;

public class DynamicSolution implements SolverOutput {
    /**
     * The averageLength attribute of the DTSP object after the solution has been run for a certain time period.
     */
    private double averageLength;

    /**
     * The time period for which the solver was solving.
     */
    private double timePeriod;

    public DynamicSolution(double averageLength, double timePeriod) {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getAverageLength());
        sb.append(", ");
        sb.append(getTimePeriod());
        return sb.toString();
    }
}
