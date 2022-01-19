package com.alike.solvertestsuite;

import com.alike.tspgraphsystem.Graph;

/**
 * Used to represent solution outputs in a convenient way.
 */
public class Solution {
    /**
     * The graph output by the solution (i.e. the solutions final form).
     */
    private Graph solutionGraph;

    /**
     * The length of the route the graph contains.
     */
    private Double routeLength;

    /**
     * The time taken to find this solution.
     */
    private Long executionTime;

    /**
     * A boolean that will be set to true if the test was failed.
     */
    private Boolean testFailed = false;

    /**
     * A reference to the exception thrown that failed the test.
     */
    private Exception failure;

    /**
     * Constructs a new test result object.
     * @param g The value to assign the @code{solutionGraph} attribute.
     * @param length The value to assign the @code{length} attribute.
     */
    public Solution(Graph g, Double length, Long executionTime) {
        setSolutionGraph(g);
        setRouteLength(length);
        setExecutionTime(executionTime);
    }

    /**
     * Used to construct a failed test result.
     */
    public static Solution createFailedTest(Graph g, Exception e) {
        Solution nR = new Solution(g, null, 0L);
        nR.setFailed();
        nR.setFailure(e);
        return nR;
    }

    /**
     * Returns the value of the @code{solutionGraph} attribute.
     * @return solutionGraph The value of the @code{solutionGraph} attribute.
     */
    public Graph getSolutionGraph() {
        return solutionGraph;
    }

    /**
     * Sets the value of the @code{solutionGraph} attribute to a new value.
     * @param solutionGraph The new value to assign the @code{solutionGraph} attribute.
     */
    public void setSolutionGraph(Graph solutionGraph) {
        this.solutionGraph = solutionGraph;
    }

    /**
     * Returns the value of the @code{routeLength} attribute.
     * @return routeLength The value of the @code{routeLength} attribute.
     */
    public Double getRouteLength() {
        return routeLength;
    }

    /**
     * Sets the value of the @code{routeLength} attribute to a new value.
     * @param routeLength The new value to assign to the @code{routeLength} attribute.
     */
    public void setRouteLength(Double routeLength) {
        this.routeLength = routeLength;
    }

    /**
     * Used to represent the Solution object as a string in console etc.
     * @return string The Solution as a string.
     */
    @Override
    public String toString() {
        return getRouteLength() + " in " + getExecutionTime() + "ns";
    }

    /**
     * Returns the value of the @code{executionTime} attribute.
     * @return executionTime The value of the @code{executionTime} attribute.
     */
    public long getExecutionTime() {
        return executionTime;
    }

    /**
     * Sets the value of the @code{executionTime} attribute to a new value.
     * @param executionTime The new value to assign to the @code{executionTime} attribute.
     */
    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    /**
     * Used to determine whether this test result is a result of a failed test.
     * @return testFailed The value of the @code{testFailed} attribute.
     */
    public boolean testFailed() {
        return this.testFailed;
    }

    /**
     * Sets the value of the @code{testFailed} attribute to true.
     */
    public void setFailed() {
        this.testFailed = true;
    }

    /**
     * Used to set the @code{failure} attribute to a new value.
     * @param e The exception object which caused the test to fail.
     */
    public void setFailure(Exception e) {
        this.failure = e;
    }
}
