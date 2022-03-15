package com.alike.solvertestsuite;

import com.alike.staticgraphsystem.Graph;

/**
 * Solver classes will output these objects from their runSolution methods to indicate that the solver failed to find
 * a solution.
 * @author alike
 */
public class Fail implements SolverOutput {
     /**
     * A reference to the exception thrown that failed the test if it was failed by an exception.
     */
    private Exception exception;

    /**
     * A reference to an error that may have failed a test.
     */
    private Error error;

    /**
     * A reference to the graph on which the solution failed.
     */
    private Graph graph;

    /**
     * Constructs a new @code{Fail} object given an exception.
     * @param e The exception that caused the solver to fail in finding a solution.
     * @param g A reference to the graph on which the solver failed.
     */
    public Fail(Exception e, Graph g) {
        setFailure(e);
        error = null;
    }

    /**
     * Constructs a new @code{Fail} object given an error.
     * @param e The error that caused the solver to fail in finding a solution.
     * @param g A reference to the graph on which the solver failed.
     */
    public Fail(Error e, Graph g) {
        setFailure(e);
        exception = null;
    }

    /**
     * Used to set the @code{exception} attribute to a new value.
     * @param e The exception object which caused the test to fail.
     */
    public void setFailure(Exception e) {
        this.exception = e;
    }

    /**
     * Used to set the failure when the failure was an error (not an exception).
     * @param e The error that caused the failure.
     */
    public void setFailure(Error e) {
        this.error = e;
    }

    /**
     * Used to retrieve the error associated with the failed test if it was a failed test.
     * @return error The error added to the test object when it was constructed after a solver failed.
     */
    private Error getError() {
        return this.error;
    }

    /**
     * Used to retrieve the exception associated with the failed test if it was a failed test.
     * @return exception The exception added to the test object when it was constructed after a solver failed.
     */
    private Exception getException() {
        return this.exception;
    }

    /**
     * Returns the value of the @code{graph} attribute.
     * @return graph The value of the @code{graph} attribute.
     */
    public Graph getGraph() {
        return this.graph;
    }

    /**
     * Sets the value of the @code{graph} attribute.
     * @param graph The new value to assign the @code{graph} attribute.
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    /**
     * Returns whether this solution output object is a fail object or not; in this case, true.
     * @return true Returns true because this is a fail object.
     */
    @Override
    public boolean isFail() {
        return true;
    }

    /**
     * Returns a string representation of this fail object.
     * @return string This object represented by a string.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Failure: ");
        if (getError() != null) { // Failure was caused by an error.
            sb.append(getError().getMessage());
        } else {
            sb.append(getException().getMessage());
        }
        return sb.toString();
    }
}
