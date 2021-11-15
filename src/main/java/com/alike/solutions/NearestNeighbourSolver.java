package com.alike.solutions;

import com.alike.customexceptions.InvalidGraphException;
import com.alike.tspgraphsystem.*;
import javafx.util.Pair;

import java.util.ArrayList;

public class NearestNeighbourSolver {

    private TSPGraph graph;

    private TSPEdgeContainer edgeContainer;
    private TSPNodeContainer nodeContainer;
    private int currentNodeID;
    private TSPNode currentNode;
    private boolean[] visited;
    // We do this so we dont have to repeatedly loop through the list to check they've all been visited.
    private int numNodesVisited;

    public NearestNeighbourSolver(TSPGraph graph) throws InvalidGraphException {
        this.graph = graph;
        this.edgeContainer = graph.getEdgeContainer();
        this.nodeContainer = graph.getNodeContainer();
        if (nodeContainer.getNodeSet().size() < 3) {
            throw new InvalidGraphException("Edge set smaller than 3.");
        }
        this.nodeContainer = graph.getNodeContainer();
    }

    public Pair<TSPGraph, Double> runSolution() {
        // Set our current node to be the first node in the list of nodes.
        setCurrentNode(nodeContainer.getNodeSet().get(0));
        currentNode.setVisited(true); // Set it as visited
        numNodesVisited = 1; // Add 1 to the number of nodes visited
        // Exectute the traversal steps
        while (numNodesVisited < nodeContainer.getNodeSet().size() + 1) {
            traverseToNextClosestNode();
        }
        // Output information about solve
        System.out.println("Finished solve!");
        Pair<TSPGraph, Double> out = new Pair<>(this.graph, graph.getEdgeContainer().calculateCurrentRouteLength());
        return out;
    }

    private void setCurrentNode(TSPNode tspNode) {
        this.currentNode = tspNode;
    }

    private Pair<TSPNode, Integer> findClosestUnvistedNode() {
         // Generate our space to put our results
        double distanceToClosestFoundNode = 0.0; // Keep a record of the closest distance
        TSPNode closestFoundNode = null; // Keep a record of the closest found node
        ArrayList<TSPNode> nodeSet = nodeContainer.getNodeSet(); // Get the set
        int idxOfFoundNode = -1; // -1 so we can throw errors later
        for (int i = 0; i < nodeSet.size(); i++) {
            TSPNode nodeBeingChecked = nodeSet.get(i);
            if (!nodeBeingChecked.isVisited()) {
                Vector v = currentNode.getVectorTo(nodeBeingChecked);
                double mag = v.magnitude();
                if (mag > distanceToClosestFoundNode) {
                    distanceToClosestFoundNode = mag;
                    closestFoundNode = nodeBeingChecked;
                    idxOfFoundNode = i;
                }
            }
        }
        if (closestFoundNode == null) { // Means the end of the graphs - loop back to beginning.
            closestFoundNode = nodeSet.get(0);
            idxOfFoundNode = 0;
        }
        return new Pair<>(closestFoundNode, idxOfFoundNode);
    }


    private void traverseToNextClosestNode() {
        // Find the closest node
//        double distanceToClosestFoundNode = 0.0;
//        TSPNode closestFoundNode = null;
//        int closestFoundNodeIdx = -1;
//        for (int i = 1; i < visited.length - 1; i++) { // We always start at the node at idx 0 so no need to check
//            if (!visited[i]) {
//                TSPNode nodeBeingChecked = nodeContainer.getNodeSet().get(i);
//                Vector v = currentNode.getVectorTo(nodeBeingChecked);
//                double mag = v.magnitude();
//                if (mag > distanceToClosestFoundNode) {
//                    distanceToClosestFoundNode = mag;
//                    closestFoundNode = nodeBeingChecked;
//                    closestFoundNodeIdx = i;
//                }
//            }
//        }
//        if (closestFoundNode == null) {
//            edgeContainer.getEdgeSet().add(new TSPEdge(currentNode, nodeContainer.getNodeSet().get(0)));
//
//            setCurrentNode(nodeContainer.getNodeSet().get(0));
//            System.out.println("bo");
//            return;
//        }
        Pair<TSPNode, Integer> nextNodeInfo = findClosestUnvistedNode();
        TSPNode closestFoundNode = nextNodeInfo.getKey();
        Integer closestFoundNodeIdx = nextNodeInfo.getValue();
        edgeContainer.getEdgeSet().add(new TSPEdge(currentNode, closestFoundNode));
        closestFoundNode.setVisited(true);
        setCurrentNode(closestFoundNode);
        numNodesVisited++;
        System.out.println("Travelled to: " + currentNode.toString() + ", " + nextNodeInfo.getValue());
    }
}
