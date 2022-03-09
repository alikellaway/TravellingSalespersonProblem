package com.alike.solvers;

import com.alike.customexceptions.NoNodeContainerException;
import com.alike.solvertestsuite.Solution;
import com.alike.tspgraphsystem.TSPEdgeContainer;
import com.alike.tspgraphsystem.TSPGraph;

public class LinKernighanSolver implements Solver {

    /**
     * The graph which we are to solve.
     */
    private TSPGraph graph;

    /**
     * A reference to the edge matrix of the graph we are trying to solve.
     */
    private double[][] edgeMatrix;

    /**
     * The set of edges that is the current tour.
     */
    private TSPEdgeContainer tour;

    public LinKernighanSolver(TSPGraph graph) {
        setGraph(graph);
    }

    @Override
    public Solution runSolution(int delayPerStep) {
        try {
            graph.constructEdgeLengthMatrix(); // Construct a matrix containing the complete graph.
            setEdgeMatrix(graph.getEdgeLengthMatrix());
        } catch(NoNodeContainerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sets the @code{graph} attribute to a new value.
     * @param graph The graph to assign the Solver's @code{graph} attribute.
     */
    @Override
    public void setGraph(TSPGraph graph) {
        this.graph = graph;
    }

    /**
     * Sets the @code{edgeMatrix} attribute to a new value.
     * @param edgeMatrix The new value to assign the @code{edgeMatrix} attribute.
     */
    public void setEdgeMatrix(double[][] edgeMatrix) {
        this.edgeMatrix = edgeMatrix;
    }

    /**
     * Sets the value of the @code{tour} attribute to a new value.
     * @param tour The new value to assign the @code{tour} attriubte.
     */
    public void setTour(TSPEdgeContainer tour) {
        this.tour = tour;
    }
}
