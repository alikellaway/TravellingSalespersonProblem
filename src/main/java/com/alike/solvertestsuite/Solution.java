package com.alike.solvertestsuite;

import com.alike.staticgraphsystem.Graph;

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
    private Exception failureException;

    /**
     * A reference to an error that may have failed a test.
     */
    private VirtualMachineError failureError;

    /**
     * The ID or number of the test that produced this solution.
     */
    private int testNumber;

    /**
     * Constructs a new test result object.
     * @param g The value to assign the @code{solutionGraph} attribute.
     * @param length The value to assign the @code{length} attribute.
     */
    public Solution(Graph g, Double length, Long executionTime) {
        setSolutionGraph(g);
        setRouteLength(length);
        setExecutionTime(executionTime);
        setTestNumber(testNumber);
    }



    public static Solution createFailedSolution(Graph g, Exception e) {
        Solution nS = new Solution(g, null, 0L);
        nS.setFailed();
        nS.setFailure(e);
        return nS;
    }

    /**
     * Used to create a failed test result when the failure was a virtual machine error and not an exception.
     * @param g The graph that the error was thrown on.
     * @param e The error that was thrown.
     * @return nS The new solution that stores the failure result.
     */
    public static Solution createFailedSolution(Graph g, VirtualMachineError e) {
        Solution nS = new Solution(g, null, 0L);
        nS.setFailed();
        nS.setFailure(e);
        return nS;
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
        this.failureException = e;
    }

    /**
     * Used to set the failure when the failure was an error (not an exception).
     * @param e The error that caused the failure.
     */
    public void setFailure(VirtualMachineError e) {
        this.failureError = e;
    }

    /**
     * Used to retrieve the error associated with the failed test if it was a failed test.
     * @return failureError The error added to the test object when it was constructed after a solver failed.
     */
    private Error getFailureError() {
        return failureError;
    }

    /**
     * Used to retrieve the exception associated with the failed test if it was a failed test.
     * @return failureException The exception added to the test object when it was constructed after a solver failed.
     */
    private Exception getFailureException() {
        return failureException;
    }

    /**
     * Returns the cause of failure whether it's an error or an exception.
     * @return failure The exception or error object.
     */
    public Object getFailure() {
        if (getFailureException() != null) {
            return getFailureException();
        }
        if (getFailureError() != null) {
            return getFailureError();
        }
        return null;
    }

    /**
     * Returns the value of the @code{testNumber} attribute.
     * @return testNumber The value of the @code{testNumber} attribute.
     */
    public int getTestNumber() {
        return testNumber;
    }

    /**
     * Sets the value of the @code{testNumber} attribute to a new value.
     * @param testNumber The new value to assign the @code{testNumber} attribute.
     */
    public void setTestNumber(int testNumber) {
        this.testNumber = testNumber;
    }

    /**
     * Used to turn this solution object into a string to be represented in console.
     * @return string The solution object as a string.
     */
    @Override
    public String toString() {
        if (testFailed) {
            return "#" + testNumber + " failed " + getFailure().toString();
        }
        else {
            return "#" + testNumber + " passed " + getRouteLength() + " in " + getExecutionTime() + "ns";
        } // Convert from ns to s is divide by 1bil
    }
}
