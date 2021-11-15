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
        setCurrentNode(nodeContainer.getNodeSet().get(0));
        setVisited(0);
        numNodesVisited = 1;

        while (numNodesVisited < visited.length) {
            traverseToNextClosestNode();
        }
    }

    private void setCurrentNode(TSPNode tspNode) {
        System.out.println(tspNode);
        this.currentNode = tspNode;
    }

    public void setGraph(TSPGraph graph) {
        this.graph = graph;
    }

    public int getCurrentNodeID() {
        return currentNodeID;
    }


    private void setVisited(int index) {
        visited[index] = true;
        numNodesVisited++;
    }


    private void traverseToNextClosestNode() {
        // Find the closest node
        double distanceToClosestFoundNode = 0.0;
        TSPNode closestFoundNode = null;
        int closestFoundNodeIdx = -1;
        for (int i = 1; i < visited.length - 1; i++) { // We always start at the node at idx 0 so no need to check
            if (!visited[i]) {
                TSPNode nodeBeingChecked = nodeContainer.getNodeSet().get(i);
                Vector v = currentNode.getVectorTo(nodeBeingChecked);
                double mag = v.magnitude();
                if (mag > distanceToClosestFoundNode) {
                    distanceToClosestFoundNode = mag;
                    closestFoundNode = nodeBeingChecked;
                    closestFoundNodeIdx = i;
                }
            }
        }
        if (closestFoundNode == null) {
            edges.getEdgeSet().add(new TSPEdge(currentNode, nodeContainer.getNodeSet().get(0)));

            setCurrentNode(nodeContainer.getNodeSet().get(0));
            System.out.println("bo");
            return;
        }
        edges.getEdgeSet().add(new TSPEdge(currentNode, closestFoundNode));
        setVisited(closestFoundNodeIdx);
        setCurrentNode(closestFoundNode);
        numNodesVisited++;
        System.out.println("hi");
    }
}
