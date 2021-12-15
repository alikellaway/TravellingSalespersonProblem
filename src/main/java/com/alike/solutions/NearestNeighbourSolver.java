package com.alike.solutions;

import com.alike.Main;
import com.alike.customexceptions.EdgeSuperimpositionException;
import com.alike.customexceptions.EdgeToSelfException;
import com.alike.customexceptions.InvalidGraphException;
import com.alike.tspgraphsystem.*;
import javafx.util.Pair;
import java.util.ArrayList;

/**
 * This class takes a graph object as a parameter, and will create a TSP route using the 'Nearest Neighbour Algorithm'.
 * @author alike
 * @version 1.0
 */
public class NearestNeighbourSolver {
    /**
     * A reference to the graph we are solving.
     */
    private final TSPGraph graph;
    /**
     * A reference to the edge container we are operating within.
     */
    private final TSPEdgeContainer edgeContainer;
    /**
     * A reference to the node container we are operating on.
     */
    private final TSPNodeContainer nodeContainer;
    /**
     * The node the algorithm is currently sitting on.
     */
    private TSPNode currentNode;
    /**
     * This records the number of nodes we have already visited. We do this so we don't have to repeatedly loop
     * through the list to check they've all been visited.
     */
    private int numNodesVisited;

    /**
     * Constructor used to load a graph into the object, so that a solution can be run.
     * @param graph The graph to load into the NNS object.
     * @throws InvalidGraphException Thrown if the input graph has less than 3 nodes (illegal graph).
     */
    public NearestNeighbourSolver(TSPGraph graph) throws InvalidGraphException {
        this.graph = graph;
        this.edgeContainer = graph.getEdgeContainer();
        this.nodeContainer = graph.getNodeContainer();
        if (nodeContainer.getNodeSet().size() < 3) {
            throw new InvalidGraphException("Edge set smaller than 3.");
        }
    }

    /**
     * Method starts the algorithm that solves the TSP through the graph.
     * @return Returns the graph (with the solution in the edgeContainer) and a double describing how long the found
     * route was.
     */
    public Pair<TSPGraph, Double> runSolution(int delayPerStep) throws InterruptedException,
            EdgeSuperimpositionException, EdgeToSelfException {
        // Set our current node to be the first node in the list of nodes.
        setCurrentNode(nodeContainer.getNodeSet().get(0));
        currentNode.setVisited(true); // Set it as visited
        numNodesVisited = 1; // Nodes visited is not 1 as we account for the last node when we visit it at the end.
        // Execute the traversal steps
        while (numNodesVisited < nodeContainer.getNodeSet().size() + 1) {
            Thread.sleep(delayPerStep);
            traverseToNextClosestNode();
        }
        // Output information about solve
        return new Pair<>(this.graph, graph.getEdgeContainer().getTotalLength());
    }

    /**
     * Method used to set the current node to a new node.
     * @param tspNode The new node to become the current node.
     */
    private void setCurrentNode(TSPNode tspNode) {
        this.currentNode = tspNode;
    }

    /**
     * This method is used to find the closest unvisited node to the current node.
     * @return TSPNode The closest univisited node (or the starting node if no others are found).
     */
    private TSPNode findClosestUnvistedNode() {
         // Generate our space to put our results
        double distanceToClosestFoundNode =  // We set the distance to be the maximum distance two nodes could away
                Math.ceil(Math.sqrt(Math.pow(Main.COORDINATE_MAX_WIDTH, 2) + Math.pow(Main.COORDINATE_MAX_HEIGHT, 2)));
        TSPNode closestFoundNode = null; // Keep a record of the closest found node
        ArrayList<TSPNode> nodeSet = nodeContainer.getNodeSet(); // Get the set
        for (TSPNode nodeBeingChecked : nodeSet) {
            if (!nodeBeingChecked.isVisited() && nodeBeingChecked != currentNode) {
                Vector v = currentNode.getVectorTo(nodeBeingChecked);
                double mag = v.magnitude();
                if (mag < distanceToClosestFoundNode) {
                    distanceToClosestFoundNode = mag;
                    closestFoundNode = nodeBeingChecked;
                }
            }
        }
        if (closestFoundNode == null) { // Means the end of the graphs - loop back to beginning.
            closestFoundNode = nodeSet.get(0);
        }
        return closestFoundNode;
    }

    /**
     * Traverse the algorithm to the next closes node.
     */
    private void traverseToNextClosestNode() throws EdgeSuperimpositionException, EdgeToSelfException {
        // Find the next closest unvisited node.
        TSPNode nextNode = findClosestUnvistedNode();
        // Add an edge between the current node and the next closest node.
        edgeContainer.add(new TSPEdge(currentNode, nextNode));
        nextNode.setVisited(true); // Set that node to visited.
        // System.out.println(nextNode.isVisited());
        setCurrentNode(nextNode); // Change the current node.
        numNodesVisited++;
        // Debug verbosity
        // System.out.println("Travelled to: " + currentNode.toString() + ", " + nextNode.getNodeID() + ", " + numNodesVisited);
    }
}
