package com.alike.solvers;

import com.alike.customexceptions.NoNodeContainerException;
import com.alike.solvertestsuite.Solution;
import com.alike.tspgraphsystem.TSPGraph;

public class LinKernighanSolver implements Solver {

    private Double[][] edgeMatrix;
    private TSPGraph graph;

    public LinKernighanSolver(TSPGraph graph) {
        setGraph(graph);
    }

    @Override
    public Solution runSolution(int delayPerStep) {
        try {
            graph.constructEdgeLengthMatrix(); // Construct a matrix containing the complete graph.
        } catch(NoNodeContainerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setGraph(TSPGraph graph) {
        this.graph = graph;
    }



}
