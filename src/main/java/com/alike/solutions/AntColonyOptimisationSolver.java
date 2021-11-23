package com.alike.solutions;

import com.alike.tspgraphsystem.TSPGraph;

/**
 * Class uses the Ant colony optimisation algorithm to solve an optimised route through a TSPGraph object.
 * @author alike
 */
public class AntColonyOptimisationSolver {
    /**
     * The graph object this solver will be solving.
     */
    private TSPGraph graph;

    /**
     * The number of ants that will traverse the graph to be solved by this solver class.
     */
    private final int numberOfAnts = 500;

    public AntColonyOptimisationSolver(TSPGraph graph) {
        setGraph(graph);
    }

    public void setGraph(TSPGraph graph) {
        this.graph = graph;
    }
}
