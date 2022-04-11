package com.alike.solvertestsuite;

import com.alike.customexceptions.NodeSuperimpositionException;
import com.alike.graphsystem.DynamicGraph;
import com.alike.solvers.DynamicSolver;
import com.alike.read_write.GraphReader;
import com.alike.time.Stopwatch;

import java.io.IOException;
import java.util.ArrayList;

public class DynamicTestSuite {
    /**
     * The stopwatch object used to gather time and test length data.
     */
    private Stopwatch stopwatch;

    /**
     * Initialises a new @code{DynamicTestSuite} object.
     */
    public DynamicTestSuite() {
        setStopwatch(new Stopwatch());
    }

    /**
     * Method tests a solver against the graph's written in file and outputs the test results.
     * @param solver The instance of a solver to test.
     * @param numSolves The number of solves the solver should compute before terminating.
     * @param delayPerSolve The delay between each solve during which the graph will move.
     * @param nodeSpeed The speed at which nodes should move.
     * @param randomMovement Whether nodes should move with a random component.
     * @param velocityMovement Whetner nodes should move with velocity.
     * @return results
     * @throws IOException Thrown if an I/O error occurs.
     * @throws NodeSuperimpositionException Thrown if an attempt is made to superimpose nodes.
     */
    public DynamicTestSuiteResult testSolver(DynamicSolver solver, int numSolves, int delayPerSolve, int nodeSpeed, boolean randomMovement, boolean velocityMovement) throws IOException, NodeSuperimpositionException {
        ArrayList<DynamicTestResult> results = new ArrayList<>();
        GraphReader gr = new GraphReader();
        int testNumber = 1;
        while (true) { // While there are still graphs left in the file
            DynamicGraph testGraph = gr.getNextGraph(nodeSpeed, randomMovement, velocityMovement); // Try to get a graph
            if (testGraph == null) { // If returns null then reach end of graph file.
                break;
            }
            System.out.println(testNumber + ": " + testGraph.toStorageFormat(';'));
            solver.setGraph(testGraph); // Set the solver's dynamic graph, so it can solve it
            stopwatch.start(); // Start the watch
            DynamicSolution ds = solver.calculateSolutions(numSolves, delayPerSolve);
            results.add(new DynamicTestResult(ds, stopwatch.getTimeNs(), testNumber));
            System.out.println("Solver finished.");
            stopwatch.clear();
            testNumber++;
            System.out.println("Completed.");
            System.out.println(testGraph.getAverageRouteLength());
            testGraph.kill(); // Kill the graph so it is no longer running in the background.
        }
        return new DynamicTestSuiteResult(results, numSolves, delayPerSolve, nodeSpeed, randomMovement, velocityMovement);
    }

    /**
     * Assigns the value of the @code{stopwatch} attribute.
     * @param sw The new value to assign the @code{stopwatch} attribute.
     */
    private void setStopwatch(Stopwatch sw) {
        this.stopwatch = sw;
    }
}
