package com.alike.solvers;

import com.alike.customexceptions.EdgeSuperimpositionException;
import com.alike.customexceptions.EdgeToSelfException;
import com.alike.customexceptions.NoClosestNodeException;
import com.alike.solution_helpers.RepeatedFunctions;
import com.alike.solvertestsuite.Fail;
import com.alike.solvertestsuite.Solution;
import com.alike.solvertestsuite.SolverOutput;
import com.alike.graphsystem.*;

/**
 * This class takes a graph object as a parameter, and will create a TSP route using the 'Nearest Neighbour Algorithm'.
 * @author alike
 * @version 1.0
 */
public class NearestNeighbourSolver implements StaticSolver {
    /**
     * A reference to the graph we are solving.
     */
    private StaticGraph graph;

    /**
     * The node the algorithm is currently focussed on.
     */
    private Node currentNode;

    /**
     * This records the number of nodes we have already visited. We do this, so we don't have to repeatedly loop
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

    /**
     * Constructs a new empty @code{NearestNeighbourSolver} object.
     */
    public NearestNeighbourSolver() {}

    /**
     * Method starts the algorithm that solves the TSP through the graph.
     * @return Returns the graph (with the solution in the edgeContainer) and a double describing how long the found
     * route was.
     */
    public SolverOutput runSolution(int delayPerStep) {
        try { // Try to create a solution.
            graph.setAllNodesUnvisited(); // Make sure all nodes are in unvisited state
            graph.getEdgeContainer().clear(); // Make sure edge set is empty
            long startTime = System.nanoTime();
            // Set our current node to be the first node in the list of nodes (could choose any).
            setCurrentNode(graph.getNodeContainer().getNodeSet().get(0)); // Current node is 0th
            currentNode.setVisited(true); // Set it as visited
            numNodesVisited = 1;
            // Execute the traversal steps
            while (numNodesVisited <= graph.getNodeContainer().getNodeSet().size()) {
                try {
                    traverseToNextClosestNode();
                    RepeatedFunctions.sleep(delayPerStep);
                } catch (EdgeSuperimpositionException | EdgeToSelfException e) {
                    e.printStackTrace();
                }
            }
            // Output information about solve
            long finishTime = System.nanoTime();
            return new Solution(this.graph, graph.getEdgeContainer().getTotalLength(), finishTime - startTime);
        } catch (Exception e) { // Failed to create a solution due to an uncaught exception.
            return new Fail(e, graph);
        } catch (Error e) {
            return new Fail(e, graph);
        }
    }

    /**
     * Method used to set the current node to a new node.
     * @param tspNode The new node to become the current node.
     */
    private void setCurrentNode(Node tspNode) {
        this.currentNode = tspNode;
    }

    /**
     * Extends the route by moving to the next closest node (by distance) or to the start node if all nodes are visited.
     */
    private void traverseToNextClosestNode() throws EdgeSuperimpositionException, EdgeToSelfException {
        // Find the next closest unvisited node.
        Node nextNode;
        try {
            nextNode = currentNode.getClosestNode(graph.getNodeContainer().getNodeSet(), true);
        } catch (NoClosestNodeException e) { // If we didn't find one, we have exhausted nodes, loop back to beginning
            nextNode = graph.getNodeContainer().getNodeSet().get(0);
        }
        // Add an edge between the current node and the next closest node.
        graph.getEdgeContainer().add(new Edge(currentNode, nextNode));
        nextNode.setVisited(true); // Set that node to visited.
        // System.out.println(nextNode.isVisited());
        setCurrentNode(nextNode); // Change the current node.
        numNodesVisited++;
    } // TODO this is not the most efficient way of doing this, better would be to remove nodes as we've visited them

    /**
     * Sets the value of the @code{graph} attribute to a new value.
     * @param newGraph The new value to assign to the @code{graph} attribute.
     */
    public void setGraph(StaticGraph newGraph) {
        RepeatedFunctions.validateGraph(newGraph);
        this.graph = newGraph;
    }
}
