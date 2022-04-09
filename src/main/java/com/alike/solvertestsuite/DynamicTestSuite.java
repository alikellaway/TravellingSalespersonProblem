package com.alike.solvertestsuite;

import com.alike.dynamicgraphsystem.DynamicGraph;
import com.alike.dynamicsolvers.DynamicSolver;
import com.alike.staticgraphsystem.StaticGraph;
import com.alike.staticgraphsystem.Vector;

import java.util.ArrayList;

public class DynamicTestSuite {
    /**
     * A dgraph object that can be loaded and unloaded with new data for testing.
     */
    private DynamicGraph dgraph;

    /**
     * A reference to the solver that is being used to solve the dynamic graph during the current test.
     */
    private DynamicSolver solver;

    /**
     * The number of times a solver should solve the dynamic graph before outputting it's results.
     */
    private int numSolves = 100;

    /**
     * The amount of time a solver should wait (allow the nodes to shift) before solving the graph again.
     */
    private int delayPerSolve;

    /**
     * The stopwatch object used to gather time and test length data.
     */
    private Stopwatch stopwatch;

    public DynamicTestSuite(DynamicSolver solver) {
        setDgraph(new DynamicGraph(new StaticGraph(), false, true));
        setSolver(solver);
        setStopwatch(new Stopwatch());
    }

//    private SolverOutput runTest(DynamicGraph dgraph, ArrayList<Vector> initVelocities) {
//        solver.setGraph(dgraph);
//        stopwatch.start();
//        SolverOutput so = solver.runSolution(numSolves, delayPerSolve);
//        stopwatch.stop();
//        return so;
//    }

//    private DynamicGraph loadGraph() {
//
//    }

    public void setDgraph(DynamicGraph dgraph) {
        this.dgraph = dgraph;
    }

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
