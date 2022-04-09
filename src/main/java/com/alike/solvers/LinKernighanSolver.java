package com.alike.solvers;

import com.alike.solvertestsuite.Solution;
import com.alike.graphsystem.EdgeContainer;
import com.alike.graphsystem.StaticGraph;

public class LinKernighanSolver implements StaticSolver {

    /**
     * The graph which we are to solve.
     */
    private StaticGraph graph;

    /**
     * A reference to the edge matrix of the graph we are trying to solve.
     */
    private double[][] edgeMatrix;

    /**
     * The set of edges that is the current tour.
     */
    private EdgeContainer tour;

    public LinKernighanSolver(StaticGraph graph) {
        setGraph(graph);
    }

    @Override
    public Solution runSolution(int delayPerStep) {

        graph.constructEdgeLengthMatrix(); // Construct a matrix containing the complete graph.
        setEdgeMatrix(graph.getEdgeLengthMatrix());

        return null;
    }

    /**
     * Sets the @code{graph} attribute to a new value.
     * @param graph The graph to assign the StaticSolver's @code{graph} attribute.
     */
    @Override
    public void setGraph(StaticGraph graph) {
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
    public void setTour(EdgeContainer tour) {
        this.tour = tour;
    }
}
