package com.alike.solutions;

import com.alike.customexceptions.EdgeSuperimpositionException;
import com.alike.customexceptions.EdgeToSelfException;
import com.alike.tspgraphsystem.TSPEdge;
import com.alike.tspgraphsystem.TSPEdgeContainer;
import com.alike.tspgraphsystem.TSPGraph;
import com.alike.tspgraphsystem.TSPNode;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Used to find a route through a @code{TSPGraph} using Christofide's algorithm.
 * @author alike
 */
public class ChristofidesSolver {
    /**
     * The @code{TSPGraph} we are solving.
     */
    private TSPGraph graph;


    public ChristofidesSolver(TSPGraph graph) {
        setGraph(graph);
    }

    public Pair<TSPGraph, Double> runSolution() {
        completeGraph();
        return new Pair<>(graph, graph.getEdgeContainer().calculateCurrentRouteLength());
    }

    /**
     * Constructs an edge container for this graph that ensures the graph is complete - a complete graph is a graph
     * where all nodes have an edge to all other nodes. NB: The number of edges constructed during this process will be:
     * (n * (n-1))/2 nodes
     */
    public void completeGraph() {
        ArrayList<TSPNode> nodes = new ArrayList<>(graph.getNodeContainer().getNodeSet()); // Copy the node set
        TSPEdgeContainer edgeContainer = graph.getEdgeContainer();
        edgeContainer.clear();

        for (int i = 0; i < nodes.size(); i++) {
            for (TSPNode node : nodes) {
                try {
                    TSPEdge e = new TSPEdge(nodes.get(i), node);
                    edgeContainer.add(e);
                } catch (EdgeSuperimpositionException | EdgeToSelfException ignored) {
                }
            }
            nodes.remove(i); // Removing the element at the front means we will still be at index 0
            i--;
        }
    }

    /**
     * Sets the value of the @code{graph} attribute to a new value.
     * @param graph The new value to become the @code{graph} attribute.
     */
    public void setGraph(TSPGraph graph) {
        this.graph = graph;
    }
}
