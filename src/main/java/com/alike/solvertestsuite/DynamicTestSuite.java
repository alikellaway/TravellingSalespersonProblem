package com.alike.solvertestsuite;

import com.alike.customexceptions.NodeSuperimpositionException;
import com.alike.graphsystem.DynamicGraph;
import com.alike.dynamicsolvers.DynamicSolver;
import com.alike.graphsystem.StaticGraph;
import com.alike.read_write.GraphReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class DynamicTestSuite {

    /**
     * A reference to the solver that is being used to solve the dynamic graph during the current test.
     */
    private DynamicSolver solver;

    /**
     * The number of times a solver should solve the dynamic graph before outputting it's results.
     */
    private int numSolves;

    /**
     * The amount of time a solver should wait (allow the nodes to shift) before solving the graph again.
     */
    private int delayPerSolve;

    /**
     * The stopwatch object used to gather time and test length data.
     */
    private Stopwatch stopwatch;

    public DynamicTestSuite() {
        setStopwatch(new Stopwatch());
    }

    public ArrayList<DynamicTestResult> testSolver(DynamicSolver solver, int numSolves, int delayPerSolve, int nodeSpeed, boolean randomMovement, boolean velocityMovement) throws IOException, NodeSuperimpositionException {
        ArrayList<DynamicTestResult> results = new ArrayList<>();
        GraphReader gr = new GraphReader();
        while (true) { // While there are still graphs left in the file
            DynamicGraph dg = gr.getNextGraph(nodeSpeed, randomMovement, velocityMovement); // Try to get a graph
            if (dg == null) { // If returns null then reach end of graph file.
                break;
            }
            solver.setGraph(dg); // Set the solver's dynamic graph so it can solve it
            stopwatch.start(); // Start the timer
            DynamicSolution ds = solver.calculateSolutions(numSolves, delayPerSolve);
            results.add(new DynamicTestResult(ds, numSolves, stopwatch.getTimeNs()));
            stopwatch.clear();
        }
        return results;
    }


//    private DynamicGraph loadGraph() {
//
//    }

    public void setSolver(DynamicSolver solver) {
        this.solver = solver;
    }

    /**
     * Assigns the value of the @code{stopwatch} attribute.
     * @param sw The new value to assign the @code{stopwatch} attribute.
     */
    public void setStopwatch(Stopwatch sw) {
        this.stopwatch = sw;
    }
}
