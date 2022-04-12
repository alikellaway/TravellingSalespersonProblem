package com.alike.solvertestsuite;

import com.alike.graphsystem.Graph;

/**
 * Used to represent solution outputs in a convenient way.
 */
public class Solution implements SolverOutput {
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
     * Constructs a new test result object.
     * @param g The graph solved.
     * @param length The length of the route on the graph.
     * @param executionTime The time taken for the solution to be found.
     */
    public Solution(Graph g, Double length, Long executionTime) {
        setSolutionGraph(g);
        setRouteLength(length);
        setExecutionTime(executionTime);
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
     * Used to turn this solution object into a string to be represented in console.
     * @return string The solution object as a string.
     */
    @Override
    public String toString() {
        // Convert from ns to s is divide by 1bil
        return getRouteLength() + " units long. Found in " + getExecutionTime() + "ns";
    }

    /**
     * Returns whether this solution output object is a fail object or not; in this case, false.
     * @return false Returns false because this is a solution object.
     */
    @Override
    public boolean isFail() {
        return false;
    }

}
