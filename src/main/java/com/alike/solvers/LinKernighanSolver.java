package com.alike.solvers;

import com.alike.solvertestsuite.Solution;
import com.alike.graphsystem.EdgeContainer;
import com.alike.graphsystem.StaticGraph;

/**
 * A solver employing the LinKernighan algorithm (incomplete).
 */
public class LinKernighanSolver implements StaticSolver {

    /**
     * The graph which we are to solve.
     */
    private StaticGraph graph;

    /**
     * A reference to the edge matrix of the graph we are trying to solve.
     */
    private Double[][] edgeMatrix;

    /**
     * The set of edges that is the current tour.
     */
    private EdgeContainer tour;

    /**
     * Constructs a new @code{LinKernighanSolver} object.
     * @param graph The graph the sovler will solve when instructed to do so.
     */
    public LinKernighanSolver(StaticGraph graph) {
        setGraph(graph);
    }

    /**
     * Invokes the solver to solve the graph stored in the graph attribute.
     * @param delayPerStep The delay between algorithmic decisions in the solution - used to slow a solver down.
     * @return null - Incomplete.
     */
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
    public void setEdgeMatrix(Double[][] edgeMatrix) {
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
