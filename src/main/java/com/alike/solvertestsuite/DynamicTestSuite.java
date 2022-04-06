package com.alike.solvertestsuite;

import com.alike.dynamicgraphsystem.DynamicGraph;
import com.alike.solvers.Solver;
import com.alike.staticgraphsystem.StaticGraph;

public class DynamicTestSuite {
    /**
     * A dgraph object that can be loaded and unloaded with new data for testing.
     */
    private DynamicGraph dgraph;

    /**
     * A reference to the solver that is being used to solve the dynamic graph during the current test.
     */
    private Solver solver;

    public DynamicTestSuite(Solver solver) {
        setDgraph(new DynamicGraph(new StaticGraph(), false, true));
        setSolver(solver);
    }

    public SolverOutput runSolution() {

    }

    public void setDgraph(DynamicGraph dgraph) {
        this.dgraph = dgraph;
    }

    public void setSolver(Solver solver) {
        this.solver = solver;
    }
}
