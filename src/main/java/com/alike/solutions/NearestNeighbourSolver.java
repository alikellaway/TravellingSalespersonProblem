package com.alike.solutions;

import com.alike.tspgraphsystem.*;

public class NearestNeighbourSolver {

    private TSPGraph graph;

    private TSPEdgeContainer edges;
    private TSPNodeContainer nodeContainer;
    private int currentNodeID;
    private TSPNode currentNode;
    private boolean[] visited;
    // We do this so we dont have to repeatedly loop through the list to check they've all been visited.
    private int numNodesVisited;

    public NearestNeighbourSolver(TSPGraph graph) {
        this.graph = graph;
        this.edges = graph.getEdgeContainer();
        this.nodeContainer = graph.getNodeContainer();
        // Default initialization is false for booleans
        visited = new boolean[nodeContainer.getNodeSet().size()];
    }

    public void runSolution() {
        // Set our current node to be the first node in the list of nodes.
        setCurrentNodeID(nodeContainer.getNodeSet().get(0).getNodeID());
        setCurrentNode(nodeContainer.getNodeSet().get(0));
        setVisited(0);
        numNodesVisited = 1;

        while (numNodesVisited < visited.length) {
            traverseToNextClosestNode();
        }

    }

    private void setCurrentNode(TSPNode tspNode) {
        this.currentNode = tspNode;
    }

    public void setGraph(TSPGraph graph) {
        this.graph = graph;
    }

    public int getCurrentNodeID() {
        return currentNodeID;
    }

    public void setCurrentNodeID(int currentNodeID) {
        this.currentNodeID = currentNodeID;
        setVisited(currentNodeID);
    }

    private void setVisited(int index) {
        visited[index] = true;
        numNodesVisited++;
    }

    private TSPNode findClosestUnvisitedNode() {
        double distanceToClosestFoundNode = 0.0;
        TSPNode closestFoundNode = null;
        for (int i = 1; i < visited.length - 1; i++) {
            if (!visited[i]) {
                TSPNode nodeBeingChecked = nodeContainer.getNodeSet().get(i);
                Vector v = currentNode.getVectorTo(nodeBeingChecked);
                double mag = v.magnitude();
                if (mag > distanceToClosestFoundNode) {
                    distanceToClosestFoundNode = mag;
                    closestFoundNode = nodeBeingChecked;
                }
            }
        }
        return closestFoundNode;
    }

    private void traverseToNextClosestNode() {
        TSPNode nextNode = findClosestUnvisitedNode();
        if (nextNode == null) {
            System.out.println("Unable to find a next closest node.");
            return;
        }
        edges.getEdgeSet().add(new TSPEdge(currentNode, nextNode));
        setCurrentNode(nextNode); // Also sets next node as visited
    }
}
