package com.alike.solvertestsuite;

import com.alike.dynamicgraphsystem.DynamicGraph;
import com.alike.solvers.DynamicSolver;
import com.alike.solvers.StaticSolver;
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

    private int timerLengthMs;

    private int delayPerSolve;

    public DynamicTestSuite(DynamicSolver solver) {
        setDgraph(new DynamicGraph(new StaticGraph(), false, true));
        setSolver(solver);
    }



    private SolverOutput runTest(DynamicGraph dgraph, ArrayList<Vector> initVelocities) {
        solver.setGraph(dgraph);
        SolverOutput so = solver.runSolution(timerLengthMs, delayPerSolve);
        return so;
    }

//    private DynamicGraph loadGraph() {
//
//    }

    public void setDgraph(DynamicGraph dgraph) {
        this.dgraph = dgraph;
    }

    public void setSolver(DynamicSolver solver) {
        this.solver = solver;
    }


}
