package com.alike.solvers;

import com.alike.customexceptions.EdgeSuperimpositionException;
import com.alike.customexceptions.EdgeToSelfException;
import com.alike.customexceptions.NoClosestNodeException;
import com.alike.customexceptions.NonExistentNodeException;
import com.alike.solution_helpers.RepeatedFunctions;
import com.alike.solvertestsuite.Fail;
import com.alike.solvertestsuite.Solution;
import com.alike.solvertestsuite.SolverOutput;
import com.alike.graphsystem.*;
import com.alike.time.Stopwatch;

import java.util.ArrayList;

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
     * An array storing all the nodes the algorithm hasen't yet visited.
     */
    private ArrayList<Node> unvisitedNodes;

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
            unvisitedNodes = new ArrayList<>(graph.getNodeContainer().getNodeSet());
            graph.getEdgeContainer().clear(); // Make sure edge set is empty
            Stopwatch watch = new Stopwatch(true);
            // Set our current node to be the first node in the list of nodes (could choose any).
            setCurrentNode(graph.getNodeContainer().getNodeSet().get(0)); // Current node is 0th
            unvisitedNodes.remove(0);
            // Execute the traversal steps
            while (!unvisitedNodes.isEmpty()) {
                try {
//                    traverseToNextClosestNode();
                    traverse();
                    RepeatedFunctions.sleep(delayPerStep);
                } catch (EdgeSuperimpositionException | EdgeToSelfException e) {
                    e.printStackTrace();
                }
            }
            // Join the end back together
            graph.getEdgeContainer().add(new Edge(graph.getNodeContainer().getNodeSet().get(0), currentNode));
            // Output information about solve
            return new Solution(this.graph, graph.getEdgeContainer().getTotalLength(), watch.getTimeNs());
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
     * Traverses the algorithm to the next closest node.
     * @throws EdgeToSelfException Thrown if an attempt is made to create an edge to and from the same node.
     * @throws EdgeSuperimpositionException Thrown if an attempt is made to create an edge that already exists.
     * @throws NonExistentNodeException Thrown if a node is searched for but does not exist.
     */
    private void traverse() throws EdgeToSelfException, EdgeSuperimpositionException, NonExistentNodeException {
        // Find the next closest node
        double shortestDist = Double.MAX_VALUE;
        int closestID = -1;
        int idxOfClosest = -1; // Set to -1 to keep compiler happy
        for (int i = 0; i < unvisitedNodes.size(); i++) {
            Node n = unvisitedNodes.get(i);
            double dist = currentNode.getVectorTo(n).magnitude();
            if (dist < shortestDist) {
                shortestDist = dist;
                closestID = n.getNodeID();
                idxOfClosest = i;
            }
        }
        Node closest = graph.getNodeContainer().getNodeByID(closestID);
        // Add an edge from the current node to the closest node
        unvisitedNodes.remove(idxOfClosest);
        graph.getEdgeContainer().add(new Edge(currentNode, closest));
        setCurrentNode(closest);
    }

    /**
     * Sets the value of the @code{graph} attribute to a new value.
     * @param newGraph The new value to assign to the @code{graph} attribute.
     */
    public void setGraph(StaticGraph newGraph) {
        RepeatedFunctions.validateGraph(newGraph);
        this.graph = newGraph;
    }
}
