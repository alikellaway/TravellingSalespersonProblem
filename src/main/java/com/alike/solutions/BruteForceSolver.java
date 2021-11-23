package com.alike.solutions;

import com.alike.Permuter;
import com.alike.customexceptions.EdgeSuperimpositionException;
import com.alike.customexceptions.NonExistentNodeException;
import com.alike.customexceptions.PermutationExhaustionException;
import com.alike.customexceptions.PermutationFocusException;
import com.alike.tspgraphsystem.*;
import javafx.util.Pair;
import java.util.List;

public class BruteForceSolver {

    TSPGraph graph;
    Permuter<Integer> permuter;
    double shortestFoundRoute = Integer.MAX_VALUE;
    List<Integer> shortestFoundPerm;

    public BruteForceSolver(TSPGraph graph) {
        setGraph(graph);
        setPermuter(new Permuter<>(graph.getNodeContainer().getNodeIDs()));
    }

    public Pair<TSPGraph, Double> runSolution(int delayPerStep) throws PermutationExhaustionException, EdgeSuperimpositionException, PermutationFocusException, NonExistentNodeException, InterruptedException {
        while (permuter.hasUnseenPermutations()) {
            // Set the graphs edges to be a new edge container containing the edges constructed from a permutation
            graph.setEdgeContainer(createEdgeContainerFromNodeSetPermutation(permuter.getNextPermutation()));
            // Calculate the length of that route
            double routeLength = graph.getEdgeContainer().calculateCurrentRouteLength();
            // Check if the route is the shortest route, if it is the record it.
            if (routeLength < shortestFoundRoute) {
                shortestFoundRoute = routeLength;
                shortestFoundPerm = permuter.getCurrentPermutation(); // Do it this way to save memory
            }
            Thread.sleep(delayPerStep);
        }
        // Reset the edge container to the best we've found.
        graph.setEdgeContainer(createEdgeContainerFromNodeSetPermutation(shortestFoundPerm));
        return new Pair<>(graph, shortestFoundRoute);
    }

    private void setGraph(TSPGraph graph) {
        this.graph = graph;
    }

    private void setPermuter(Permuter<Integer> permuter) {
        this.permuter = permuter;
    }

    private TSPEdgeContainer createEdgeContainerFromNodeSetPermutation(List<Integer> nodeOrder)
            throws EdgeSuperimpositionException, NonExistentNodeException {
        TSPEdgeContainer edgeContainer = new TSPEdgeContainer();
        TSPNodeContainer nodeContainer = graph.getNodeContainer();
        int numNodes = nodeContainer.getNodeSet().size();

        for (int idx = 0; idx < nodeOrder.size(); idx++) {
            TSPNode startNode = nodeContainer.getNodeByID(nodeOrder.get(idx));
            int idxOfEndNode = (idx + 1) % numNodes;
            TSPNode endNode = nodeContainer.getNodeByID(nodeOrder.get(idxOfEndNode));
            TSPEdge newEdge = new TSPEdge(startNode, endNode);
            edgeContainer.add(newEdge);
        }
        return edgeContainer;
    }

}
