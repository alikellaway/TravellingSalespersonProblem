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


    public Fail(Exception e, Graph g) {
        setFailure(e);
    }

    public Fail(Error e, Graph g) {
        setFailure(e);
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

    public Graph getGraph() {
        return this.graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    @Override
    public boolean isFail() {
        return true;
    }
}
