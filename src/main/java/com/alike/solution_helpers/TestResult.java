package com.alike.solution_helpers;

import com.alike.tspgraphsystem.Graph;

/**
 * Used to represent solution outputs in a convenient way.
 */
public class TestResult {
    /**
     * The graph output by the solution (i.e. the solutions final form).
     */
    private Graph resultGraph;

    /**
     * The length of the route the graph contains.
     */
    private Double routeLength;

    /**
     * The time taken to find this solution.
     */
    private long executionTime;

    /**
     * Constructs a new test result object.
     * @param g The value to assign the @code{resultGraph} attribute.
     * @param length The value to assign the @code{length} attribute.
     */
    public TestResult(Graph g, Double length, long executionTime) {
        setResultGraph(g);
        setRouteLength(length);
        setExecutionTime(executionTime);
    }

    /**
     * Returns the value of the @code{resultGraph} attribute.
     * @return resultGraph The value of the @code{resultGraph} attribute.
     */
    public Graph getResultGraph() {
        return resultGraph;
    }

    /**
     * Sets the value of the @code{resultGraph} attribute to a new value.
     * @param resultGraph The new value to assign the @code{resultGraph} attribute.
     */
    public void setResultGraph(Graph resultGraph) {
        this.resultGraph = resultGraph;
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
     * Used to represent the TestResult object as a string in console etc.
     * @return string The TestResult as a string.
     */
    @Override
    public String toString() {
        return getRouteLength().toString();
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
}
