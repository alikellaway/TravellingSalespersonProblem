package com.alike.solvertestsuite;

/**
 * DynamicSolution objects can be used to represent the output of a DynamicSolver objects solutions.
 * @author alike
 */
public class DynamicSolution implements SolverOutput {
    /**
     * The average length of the route yielded by a solver.
     */
    private double avgLength;

    /**
     * Average solve time.
     */
    private long avgSolveTime;

    /**
     * Initialises a new @code{DynamicSolution} object.
     * @param averageLength The average route length of all the solutions yielded by a @code{DynamicSolver}.
     * @param avgSolveTime The average time taken for a @code{DynamicSolver} to yield each solution.
     */
    public DynamicSolution(double averageLength, long avgSolveTime) {
        setAvgLength(averageLength);
        setAvgSolveTime(avgSolveTime);
    }

    /**
     * Returns the value of the @code{avgLength} attribute.
     * @return avgLength The value of the @code{avgLength} attribute.
     */
    public double getAvgLength() {
        return avgLength;
    }

    /**
     * Assigns the value of the @code{avgLength} attribute.
     * @param avgLength The new value to assign the @code{avgLength} attribute.
     */
    public void setAvgLength(double avgLength) {
        this.avgLength = avgLength;
    }

    /**
     * Returns the value of the @code{avgSolveTime} attribute.
     * @return avgSoleTime The value of the @code{avgSolveTime} attribute.
     */
    public long getAvgSolveTime() {
        return avgSolveTime;
    }

    /**
     * Assigns the value of the @code{avgSolveTime} attribute.
     * @param avgSolveTime The new value to assign the @code{avgSoleTime} attribute.
     */
    public void setAvgSolveTime(long avgSolveTime) {
        this.avgSolveTime = avgSolveTime;
    }

    /**
     * Returns whether this @code{SolverOutput} object is of type @code{Fail}.
     * @return false This object is not a @code{Fail}.
     */
    @Override
    public boolean isFail() {
        return false;
    }

    /**
     * Returns a string representation of this object and the information in it.
     * @return string This object as a string.
     */
    @Override
    public String toString() {
        return "Averaged " + getAvgLength() + " in " + getAvgSolveTime() + "ns";
    }

    /**
     * Returns a string representing this object's information in a format that can be stored and later read back into
     * memory.
     * @return string The object written as a string.
     */
    public String toStorageFormat() {
        return (float) getAvgLength() + "," + getAvgSolveTime();
    }
}
