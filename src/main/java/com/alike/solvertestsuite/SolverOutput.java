package com.alike.solvertestsuite;

/**
 * Interface for solver output - a class created at the end of a solve containing information about a solves success
 * or failure.
 */
public interface SolverOutput {
    /**
     * Returns whether this SolverOutput was output after a solver failed to solve.
     * @return boolean True if the solver failed.
     */
    boolean isFail();
}
