package com.alike.solutions;

import com.alike.customexceptions.*;
import com.alike.solution_helpers.Permuter;
import com.alike.tspgraphsystem.*;
import javafx.util.Pair;
import java.util.List;

/**
 * Class contains logic to solve TSPGraphs containing node sets using the brute force method by finding the permutations
 * of node orders and traversing them all to find the shortest route.
 * @author alike
 */
public class BruteForceSolver {

    /**
     * The graph the solution will be running on.
     */
    private TSPGraph graph;

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
     * @param graph The TSPGraph that this solution will run on.
     */
    public BruteForceSolver(TSPGraph graph) {
        setGraph(graph);
        setPermuter(new Permuter<>(graph.getNodeContainer().getNodeIDs()));
    }

    /**
     * Starts the solution running on the @code{graph}
     * @param delayPerStep The time delay between checking different permutations (to slow the progress if required).
     * @return Returns a pair object containing the TSPGraph (which contains the solution edge set) and its route
     * length.
     * @throws PermutationExhaustionException Thrown if @code{Permuter} object is bugged or incorrectly used.
     * @throws EdgeSuperimpositionException Thrown if an edge is imposed onto another.
     * @throws PermutationFocusException Thrown if @code{Permuter} object is bugged or incorrectly used.
     * @throws NonExistentNodeException Thrown if the solution runs into a node ID that doesn't exist.
     * @throws InterruptedException Thrown if the thread is interrupted.
     */
    public Pair<TSPGraph, Double> runSolution(int delayPerStep) throws PermutationExhaustionException,
            EdgeSuperimpositionException,
            PermutationFocusException,
            NonExistentNodeException,
            InterruptedException, EdgeToSelfException {
        // While there are still permutations we haven't checked we wish to continue checking more.
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

    /**
     * Outputs a new @code{edgeContainer} object given a list of node IDs.
     * @param nodeIDs A permutation of the node IDs to give a new order.
     * @return @code{edgeContainer} A new @code{edgeContainer} object containing edges abiding by the node order specified by
     * the nodeIDs parameter.
     * @throws EdgeSuperimpositionException Thrown if two edges are added between the same nodes.
     * @throws NonExistentNodeException Thrown if a node ID is encountered that does not exist.
     */
    private TSPEdgeContainer createEdgeContainerFromNodeSetPermutation(List<Integer> nodeIDs)
            throws EdgeSuperimpositionException, NonExistentNodeException, EdgeToSelfException {
        TSPEdgeContainer edgeContainer = new TSPEdgeContainer();
        TSPNodeContainer nodeContainer = graph.getNodeContainer();
        int numNodes = nodeContainer.getNodeSet().size();

        for (int idx = 0; idx < nodeIDs.size(); idx++) {
            TSPNode startNode = nodeContainer.getNodeByID(nodeIDs.get(idx));
            int idxOfEndNode = (idx + 1) % numNodes;
            TSPNode endNode = nodeContainer.getNodeByID(nodeIDs.get(idxOfEndNode));
            TSPEdge newEdge = new TSPEdge(startNode, endNode);
            edgeContainer.add(newEdge);
        }
        return edgeContainer;
    }

    /**
     * Sets the value @code{graph} attribute.
     * @param graph The new value to assign the @code{graph} attribute.
     */
    private void setGraph(TSPGraph graph) {
        this.graph = graph;
    }

    /**
     * Sets the @code{permuter} attribute to a new @code{Permuter} object.
     * @param permuter The new @code{Permuter} to become the @code{permuter} attribute.
     */
    private void setPermuter(Permuter<Integer> permuter) {
        this.permuter = permuter;
    }

}
