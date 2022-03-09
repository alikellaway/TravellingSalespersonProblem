package com.alike.solvers;

import com.alike.Main;
import com.alike.customexceptions.EdgeSuperimpositionException;
import com.alike.customexceptions.EdgeToSelfException;
import com.alike.customexceptions.NoClosestNodeException;
import com.alike.solution_helpers.RepeatedFunctions;
import com.alike.solvertestsuite.Solution;
import com.alike.staticgraphsystem.*;
import java.util.ArrayList;

/**
 * This class takes a graph object as a parameter, and will create a TSP route using the 'Nearest Neighbour Algorithm'.
 * @author alike
 * @version 1.0
 */
public class NearestNeighbourSolver implements Solver {
    /**
     * A reference to the graph we are solving.
     */
    private StaticGraph graph;
    /**
     * A reference to the edge container we are operating within.
     */
    private EdgeContainer edgeContainer;
    /**
     * A reference to the node container we are operating on.
     */
    private NodeContainer nodeContainer;
    /**
     * The node the algorithm is currently sitting on.
     */
    private Node currentNode;
    /**
     * This records the number of nodes we have already visited. We do this so we don't have to repeatedly loop
     * through the list to check they've all been visited.
     */
    private int numNodesVisited;

    /**
     * Constructor used to load a graph into the object, so that a solution can be run.
     * @param graph The graph to load into the NNS object.
     */
    public NearestNeighbourSolver(StaticGraph graph) {
        RepeatedFunctions.validateGraph(graph);
        setGraph(graph);
    }

    public NearestNeighbourSolver() {
    }

    /**
     * Method starts the algorithm that solves the TSP through the graph.
     * @return Returns the graph (with the solution in the edgeContainer) and a double describing how long the found
     * route was.
     */
    public Solution runSolution(int delayPerStep) {
        long startTime = System.nanoTime();
        // Set our current node to be the first node in the list of nodes.
        setCurrentNode(nodeContainer.getNodeSet().get(0));
        currentNode.setVisited(true); // Set it as visited
        numNodesVisited = 1; // Nodes visited is not 1 as we account for the last node when we visit it at the end.
        // Execute the traversal steps
        while (numNodesVisited < nodeContainer.getNodeSet().size() + 1) {
            RepeatedFunctions.sleep(delayPerStep);
            try {
                traverseToNextClosestNode();
            } catch (EdgeSuperimpositionException | EdgeToSelfException e) {
                e.printStackTrace();
            }
        }
        // Output information about solve
        long finishTime = System.nanoTime();
        return new Solution(this.graph, graph.getEdgeContainer().getTotalLength(), finishTime - startTime);
    }

    /**
     * Method used to set the current node to a new node.
     * @param tspNode The new node to become the current node.
     */
    private void setCurrentNode(Node tspNode) {
        this.currentNode = tspNode;
    }

    /**
     * This method is used to find the closest unvisited node to the current node.
     * @return Node The closest univisited node (or the starting node if no others are found).
     */
    private Node findClosestUnvistedNode() {
         // Generate our space to put our results
        double distanceToClosestFoundNode =  // We set the distance to be the maximum distance two nodes could away
                Math.ceil(Math.sqrt(Math.pow(Main.COORDINATE_MAX_WIDTH, 2) + Math.pow(Main.COORDINATE_MAX_HEIGHT, 2)));
        Node closestFoundNode = null; // Keep a record of the closest found node
        ArrayList<Node> nodeSet = nodeContainer.getNodeSet(); // Get the set
        for (Node nodeBeingChecked : nodeSet) {
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
        Node nextNode;
        try {
            nextNode = currentNode.getClosestNode(nodeContainer.getNodeSet(), true);
        } catch (NoClosestNodeException e) {
            nextNode = nodeContainer.getNodeSet().get(0);
        }
        // Add an edge between the current node and the next closest node.
        edgeContainer.add(new Edge(currentNode, nextNode));
        nextNode.setVisited(true); // Set that node to visited.
        // System.out.println(nextNode.isVisited());
        setCurrentNode(nextNode); // Change the current node.
        numNodesVisited++;
        // Debug verbosity
        // System.out.println("Travelled to: " + currentNode.toString() + ", " + nextNode.getNodeID() + ", " + numNodesVisited);
    }

    /**
     * Sets the value of the @code{graph} attribute to a new value.
     * @param newGraph The new value to assign to the @code{graph} attribute.
     */
    public void setGraph(StaticGraph newGraph) {
        RepeatedFunctions.validateGraph(newGraph);
        this.graph = newGraph;
        this.nodeContainer = newGraph.getNodeContainer();
        this.edgeContainer = newGraph.getEdgeContainer();
    }
}
