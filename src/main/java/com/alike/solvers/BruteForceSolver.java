package com.alike.solvers;

import com.alike.customexceptions.*;
import com.alike.solution_helpers.Permuter;
import com.alike.solution_helpers.RepeatedFunctions;
import com.alike.solvertestsuite.Fail;
import com.alike.solvertestsuite.Solution;
import com.alike.solvertestsuite.SolverOutput;
import com.alike.graphsystem.*;
import java.util.List;

/**
 * Class contains logic to solve TSPGraphs containing node sets using the brute force method by finding the permutations
 * of node orders and traversing them all to find the shortest route.
 * @author alike
 */
public class BruteForceSolver implements StaticSolver {

    /**
     * The graph the solution will be running on.
     */
    private StaticGraph graph;

    /**
     * The @code{Permuter} object that will handle the permutation calculations of nodes for the solution to run.
     */
    private Permuter<Integer> permuter;

    /**
     * The current shortest found route during the solution run (initialised to max value).
     */
    private double shortestFoundRoute = Integer.MAX_VALUE;

    /**
     * The permutation which led to the shortest found route so far.
     */
    private List<Integer> shortestFoundPerm;

    /**
     * Used to initialise a new @code{BruteForceSolver} object.
     * @param graph The StaticGraph that this solution will run on.
     */
    public BruteForceSolver(StaticGraph graph) {
        setGraph(graph);
    }

    /**
     * Constructs an empty @code{BruteForceSolver} object.
     */
    public BruteForceSolver() {
    }

    /**
     * Starts the solution running on the @code{graph}
     * @param delayPerStep The time delay between checking different permutations (to slow the progress if required).
     * @return Returns a pair object containing the StaticGraph (which contains the solution edge set) and its route
     * length.
     */
    public SolverOutput runSolution(int delayPerStep) {
        try { // Try to create a solution.
            Runtime.getRuntime().gc(); // Reclaim as much memory as possible.
            long startTime = System.nanoTime();
            // While there are still permutations we haven't checked we wish to continue checking more.
            while (permuter.hasUnseenPermutations()) {
                // Set the graphs edges to be a new edge container containing the edges constructed from a permutation
                try {
                    graph.setEdgeContainer(createEdgeContainerFromNodeSetPermutation(permuter.getNextPermutation()));
                } catch (PermutationExhaustionException | EdgeSuperimpositionException |
                        NonExistentNodeException | EdgeToSelfException e) {
                    e.printStackTrace();
                    continue;
                }
                // Calculate the length of that route
                double routeLength = graph.getEdgeContainer().getTotalLength();
                // Check if the route is the shortest route, if it is the record it.
                if (routeLength < shortestFoundRoute) {
                    shortestFoundRoute = routeLength;
                    try {
                        shortestFoundPerm = permuter.getCurrentPermutation(); // Do it this way to save memory
                    } catch (PermutationFocusException e) {
                        e.printStackTrace();
                        continue;
                    }
                }
                RepeatedFunctions.sleep(delayPerStep);
            }
            // Reset the edge container to the best we've found.
            try {
                graph.setEdgeContainer(createEdgeContainerFromNodeSetPermutation(shortestFoundPerm));
            } catch (EdgeSuperimpositionException | NonExistentNodeException | EdgeToSelfException e) {
                e.printStackTrace();
            }
            long finishTime = System.nanoTime();
            return new Solution(graph, shortestFoundRoute, finishTime - startTime);
        } catch (Exception e) { // Failed to create solution because of an un caught exception.
            return new Fail(e, graph);
        } catch (Error e) { // Failed to create solution due to an error.
            return new Fail(e, graph);
        }
    }

    /**
     * Outputs a new @code{edgeContainer} object given a list of node IDs.
     * @param nodeIDs A permutation of the node IDs to give a new order.
     * @return @code{edgeContainer} A new @code{edgeContainer} object containing edges abiding by the node order specified by
     * the nodeIDs parameter.
     * @throws EdgeSuperimpositionException Thrown if two edges are added between the same nodes.
     * @throws NonExistentNodeException Thrown if a node ID is encountered that does not exist.
     */
    private EdgeContainer createEdgeContainerFromNodeSetPermutation(List<Integer> nodeIDs)
            throws EdgeSuperimpositionException, NonExistentNodeException, EdgeToSelfException {
        EdgeContainer edgeContainer = new EdgeContainer();
        NodeContainer nodeContainer = graph.getNodeContainer();
        int numNodes = nodeContainer.getNodeSet().size();
        for (int idx = 0; idx < nodeIDs.size(); idx++) {
            Node startNode = nodeContainer.getNodeByID(nodeIDs.get(idx));
            int idxOfEndNode = (idx + 1) % numNodes;
            Node endNode = nodeContainer.getNodeByID(nodeIDs.get(idxOfEndNode));
            Edge newEdge = new Edge(startNode, endNode);
            edgeContainer.add(newEdge);
        }
        return edgeContainer;
    }

    /**
     * Sets the value @code{graph} attribute.
     * @param graph The new value to assign the @code{graph} attribute.
     */
    public void setGraph(StaticGraph graph) {
        flushAttributes();
        RepeatedFunctions.validateGraph(graph);
        this.graph = graph;
        setPermuter(new Permuter<>(graph.getNodeContainer().getNodeIDs()));
    }

    /**
     * Sets the @code{permuter} attribute to a new @code{Permuter} object.
     * @param permuter The new @code{Permuter} to become the @code{permuter} attribute.
     */
    private void setPermuter(Permuter<Integer> permuter) {
        this.permuter = permuter;
    }

    /**
     * Used to reset the attributes so that if we use this class on multiple solutions, the values are reset to their
     * originial values.
     */
    private void flushAttributes() {
        permuter = null;
        shortestFoundPerm = null;
        shortestFoundRoute = Integer.MAX_VALUE;
        graph = null;
    }
}
