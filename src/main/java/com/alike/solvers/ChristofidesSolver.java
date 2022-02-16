package com.alike.solvers;

import com.alike.customexceptions.*;
import com.alike.solution_helpers.RepeatedFunctions;
import com.alike.solvertestsuite.Solution;
import com.alike.tspgraphsystem.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Used to find a route through a @code{TSPGraph} using Christofide's algorithm.
 * @author alike
 */
public class ChristofidesSolver implements Solver {
    /**
     * The @code{TSPGraph} we are solving.
     */
    private TSPGraph graph;

    public ChristofidesSolver(TSPGraph graph) {
        setGraph(graph);
    }

    /**
     * An empty constructor so that we can set the graph at a later date.
     */
    public ChristofidesSolver() {

    }

    public Solution runSolution(int delayPerStep) {
        long startTime = System.nanoTime();
        // Construct a minimum spanning tree in graph (MST)
        try {
            constructMinimumSpanningTree(delayPerStep);
        } catch (EdgeToSelfException | InterruptedException | EdgeSuperimpositionException e) {
            e.printStackTrace();
        }
        // Create a subgraph out of the nodes with an odd order.
        TSPGraph subgraph = null;
        try {
            subgraph = new TSPGraph(new TSPNodeContainer(getOddOrderedNodes()), new TSPEdgeContainer());
        } catch (NodeSuperimpositionException e) {
            e.printStackTrace();
        }
        // Find the best perfect matching we can
        assert subgraph != null;
        graph.setEdgeContainer(getMinimumWeightMatching(subgraph));

        // Unite the MST and the matching
//        TSPEdgeContainer temp = graph.getEdgeContainer();
//        RepeatedFunctions.sleep(5000);
//        graph.setEdgeContainer(bestPerfectMatching);
//        RepeatedFunctions.sleep(1000);
//        graph.setEdgeContainer(temp);
//        graph.getEdgeContainer().absorb(bestPerfectMatching);
        // Now that every node has an even degree - we can calculate an Euler tour.

        long finishTime = System.nanoTime();
        return new Solution(graph, graph.getEdgeContainer().getTotalLength(), finishTime - startTime);
    }

//    /**
//     * Constructs an edge container for this graph that ensures the graph is complete - a complete graph is a graph
//     * where all nodes have an edge to all other nodes. NB: The number of edges constructed during this process will be:
//     * (n * (n-1))/2 nodes
//     */
//    public void completeGraph() {
//        ArrayList<TSPNode> nodes = new ArrayList<>(graph.getNodeContainer().getNodeSet()); // Copy the node set
//        TSPEdgeContainer edgeContainer = graph.getEdgeContainer();
//        edgeContainer.clear();
//
//        for (int i = 0; i < nodes.size(); i++) {
//            for (TSPNode node : nodes) {
//                try {
//                    TSPEdge e = new TSPEdge(nodes.get(i), node);
//                    edgeContainer.add(e);
//                } catch (EdgeSuperimpositionException | EdgeToSelfException ignored) {
//                }
//            }
//            nodes.remove(i); // Removing the element at the front means we will still be at index 0
//            i--;
//        }
//    }

    /**
     * Constructs a minimum spanning tree of the graph using the prim algorithm.
     * @param delayPerStep The time to wait after adding an edge (so we can see it draw if we need)
     * @throws EdgeToSelfException Thrown if an attempt is made to create an edge to and from the same node.
     * @throws EdgeSuperimpositionException Thrown if an attempt is made to create an edge that already exists.
     * @throws InterruptedException Thrown if the thread is interrupted.
     */
    private void constructMinimumSpanningTree(int delayPerStep) throws EdgeToSelfException, EdgeSuperimpositionException, InterruptedException {
        graph.setAllNodesUnvisited();
        // Create and populate arrays containing which nodes are visited and not visited.
        ArrayList<TSPNode> unvisited = new ArrayList<>(graph.getNodeContainer().getNodeSet());
        ArrayList<TSPNode> visited = new ArrayList<>();
        // Set the starting node
        unvisited.get(0).setVisited(true);
        visited.add(unvisited.get(0));
        unvisited.remove(0);
        // Create a space for us to add edges to.
        TSPEdgeContainer edgeContainer = new TSPEdgeContainer();
        graph.setEdgeContainer(edgeContainer);
        // Run algorithm
        while (!unvisited.isEmpty()) {
            double record = Integer.MAX_VALUE;
            int vIdx = -1;
            int uIdx = -1;
            for (int v = 0; v < visited.size(); v++) {
                for (int u = 0; u < unvisited.size(); u++) {
                    TSPNode n1 = visited.get(v);
                    TSPNode n2 = unvisited.get(u);
                    double distance = n1.getVectorTo(n2).magnitude();
                    if (distance < record) {
                        record = distance;
                        vIdx = v;
                        uIdx = u;
                    }
                }
            }
            if (vIdx != -1) { // Only check one since the are initialised in the same block
                edgeContainer.add(new TSPEdge(visited.get(vIdx), unvisited.get(uIdx)));
                RepeatedFunctions.sleep(delayPerStep);
                unvisited.get(uIdx).setVisited(true);
                visited.add(unvisited.get(uIdx));
                unvisited.remove(uIdx);
            }
        }
    }

    /**
     * Returns the set of nodes that have an odd order (odd number of edges connected to them).
     * @return oddOrderedNodes An ArrayList containing the nodes that were found to have an odd order.
     */
    private ArrayList<TSPNode> getOddOrderedNodes() {
        ArrayList<TSPNode> oddOrderedNodes = new ArrayList<>();
        for (TSPNode n : graph.getNodeContainer().getNodeSet()) { // For each node
            int nodeOrder = 0;
            // Find its order
            for (TSPEdge e : graph.getEdgeContainer().getEdgeSet()) {
                if (e.containsNode(n)) {
                    nodeOrder++;
                }
            }
            // If it's not even then add it.
            if (!(nodeOrder % 2 == 0)) {
                oddOrderedNodes.add(n);
            }
        }
        return oddOrderedNodes;
    }

    public TSPEdgeContainer findBestPerfectMatching(TSPGraph graph) throws EdgeSuperimpositionException, EdgeToSelfException, NoClosestNodeException {
        double bestLength = Double.MAX_VALUE;
        TSPEdgeContainer bestMatching = null;
        // Iterate forwards
        for (int i = 0; i < graph.getNumNodes(); i++) {
            TSPEdgeContainer currMatching = findPerfectMatching(graph, i);
            double currLength = currMatching.getTotalLength();
            if (currLength < bestLength) {
                bestLength = currLength;
                bestMatching = currMatching;
            }
        }
        return bestMatching;
    }

    /**
     * Finds a perfect matching using a greedy algorithm given a starting node (only finds perfect matching if input
     * node set has an even number of nodes). Calculates starting from the node at the startingIdx in the graph's node
     * set.
     * @param graph The graph on which to find a perfect matching.
     * @param startingIdx The index at which
     * @return matching A TSPEdgeContianer containing the matching that arose from this starting index.
     */
    public TSPEdgeContainer findPerfectMatching(TSPGraph graph, int startingIdx) throws EdgeToSelfException, NoClosestNodeException, EdgeSuperimpositionException {
        TSPEdgeContainer matching = new TSPEdgeContainer();
        ArrayList<TSPNode> unmatched = new ArrayList<>(graph.getNodeContainer().getNodeSet());
        // For each node, find its closest node and create a pair out of them, then remove them from the unmatched
        for (int i = 0; i < unmatched.size(); i++) {
            TSPNode currNode = unmatched.get((i + startingIdx) % unmatched.size());
            TSPNode closestNode = currNode.getClosestNode(unmatched);
            matching.add(new TSPEdge(currNode, closestNode));
            unmatched.remove(currNode);
            unmatched.remove(closestNode);
        }
        return matching;
    }

    /**
     * Sets the value of the @code{graph} attribute to a new value.
     * @param graph The new value to become the @code{graph} attribute.
     */
    public void setGraph(TSPGraph graph) {
        RepeatedFunctions.validateGraph(graph);
        this.graph = graph;
    }

    /**
     * Returns a minimum weight perfect matching (I think) of a graph with an even number of vertexes by:
     * 1. Calculate all edge lengths by creating a matrix of node IDs.
     * 2. Ignore all the values to the left of and including the diagonal of the matrix starting in the top left corner
     * and ending in the bottom right. We can do this because nodes don't map to themselves and edges are symmetrical.
     * 3. Take the edges we haven't ignored and sort them by length.
     * 4. Record which nodes we have included in the matching and search through our sorted list lowest to highest.
     * 5. When we see an edge that has neither nodes visited we can add it to the matching.
     * @param graph The graph of which we need a matching.
     * @return matching The minimum weight perfect matching of the graph.
     */
    private TSPEdgeContainer getMinimumWeightMatching(TSPGraph graph) {
        int nN = graph.getNumNodes(); // We need to know how many nodes there are.
        // Check that our graph is legal for this method.
        if (nN % 2 != 0) {
            try {
                throw new InvalidGraphException("Graph had " + nN + " nodes which is not even.");
            } catch (InvalidGraphException e) {
                e.printStackTrace();
            }
        }
        ArrayList<TSPEdge> edges = new ArrayList<>();
        // For each row,column in a square matrix, find the edge length between the nodes with ids row and column.
        for (int row = 0; row < nN; row++) {
            for (int col = 0; col < nN; col++) {
                // Nodes can't edge to themselves, so don't check calculate where col == row.
                // Edges are symmetrical, so don't calculate where col < row since we already calculated it earlier.
                if (col > row) {
                    try { // Try and get the nodes to construct an edge between them.
                        TSPEdge e = new TSPEdge(
                                graph.getNodeContainer().getNodeByID(row),
                                graph.getNodeContainer().getNodeByID(col) // Doesn't matter which way round they are.
                        );
                        edges.add(e); // Put this edge in the matrix.
                    } catch (NonExistentNodeException | EdgeToSelfException ignored) {
                    }
                }
            }
        }
        // Now we have all the important edge lengths we can sort them lowest to highest
        Collections.sort(edges); // Sorted using comparable implementation in TSPEdge class.
        /* We now need to start constructing the matching from this sorted array. To get the minimum matching we can
           choose the edges in order when node pairs first appear. Afer the nodes have been
           visited, we can set them to visited and not select edges including either again. */
        TSPEdgeContainer matching = new TSPEdgeContainer(); // This is where we will keep the edges in the matching.
        HashMap<Integer, Boolean> visited = new HashMap<>(); // Map records which nodes are in matching
        for (TSPEdge edge : edges) {
            visited.put(edge.getStartNode().getNodeID(), false);
            visited.put(edge.getEndNode().getNodeID(), false);
        } // We now have one entry of nodeID:false for each node in the graph.
        for (TSPEdge e : edges) {
            int nodeIDa = e.getStartNode().getNodeID();
            int nodeIDb = e.getEndNode().getNodeID();
            if (!visited.get(nodeIDa) && !visited.get(nodeIDb)) { // If neither node is visited.
                try {
                    matching.add(e);
                    visited.put(nodeIDa, true);
                    visited.put(nodeIDb, true);
                } catch (EdgeSuperimpositionException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return matching;
    }
}
