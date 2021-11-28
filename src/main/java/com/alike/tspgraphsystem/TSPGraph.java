package com.alike.tspgraphsystem;

import com.alike.customexceptions.EdgeSuperimpositionException;
import com.alike.customexceptions.EdgeToSelfException;
import com.alike.customexceptions.NodeSuperimpositionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to represent and collate data on a travelling salesperson problem graph.
 * TSPGraph.java
 * @author alike
 */
public class TSPGraph {

    /**
     * The nodes of this graph object (stored in a @code{nodeContainer} object for easier management).
     */
    private TSPNodeContainer nodeContainer;

    /**
     * The edges of this graph object (stored in an @code{edgeContainer} object for easier management).
     */
    private TSPEdgeContainer edgeContainer;

    /**
     * Constructs a new graph with empty node and edge containers.
     */
    public TSPGraph() {
        setNodeContainer(new TSPNodeContainer());
        setEdgeContainer(new TSPEdgeContainer());
    }

    /**
     * Returns the @code{nodeContainer} attribute of the TSPGraph object.
     * @return @code{nodeContainer} The @code{nodeContainer} attribute of the TSPGraph object.
     */
    public TSPNodeContainer getNodeContainer() {
        return nodeContainer;
    }

    /**
     * Sets the @code{nodeContainer} attribute to a new value.
     * @param nodeContainer The new value to become the @code{nodeContainer} attribute.
     */
    public void setNodeContainer(TSPNodeContainer nodeContainer) {
        this.nodeContainer = nodeContainer;
    }

    /**
     * Returns the @code{edgeContainer} attribute of the TSPGraph object.
     * @return @code{edgeContainer} The value of the @code{edgeContainer} attribute.
     */
    public TSPEdgeContainer getEdgeContainer() {
        return edgeContainer;
    }

    /**
     * Sets the @code{edgeContainer} attribute to a new value.
     * @param edgeContainer The new value to assign to the @code{edgeContainer} attribute.
     */
    public void setEdgeContainer(TSPEdgeContainer edgeContainer) {
        this.edgeContainer = edgeContainer;
    }

    /**
     * Used to generate a random TSPGraph object with randomized node positions and randomized edges (if needed).
     * @param numNodes The number of nodes the random graph must have.
     * @param addEdges Whether or not the method should output a graph with randomized edges already assigned.
     * @return @code{g} A new randomized TSPGraph object.
     */
    public static TSPGraph generateRandomGraph(int numNodes, boolean addEdges) {
        TSPNode.restartNodeCounter();
        // Create some random nodes
        TSPNodeContainer nSet = new TSPNodeContainer();
        for (int i = 0; i < numNodes; i++) {
            try {
                nSet.add(TSPNode.generateRandomTSPNode());
            } catch (NodeSuperimpositionException e) {
                i--;
            }
        }
        // Create an empty edge set.
        TSPEdgeContainer eSet = new TSPEdgeContainer();
        if (addEdges) { // Fill with random edges using trial and error if edges are required.
            for (int x = 0; x < numNodes; x++) {
                try {
                    eSet.add(new TSPEdge(nSet.getNodeSet().get(x), nSet.getNodeSet().get((x + 1) % numNodes)));
                } catch (EdgeSuperimpositionException | EdgeToSelfException e) {
                    x--;
                }
            }
        }
        // Construct a new TSPGraph object and return it.
        TSPGraph g = new TSPGraph();
        g.setNodeContainer(nSet);
        g.setEdgeContainer(eSet);
        return g;
    }

    /**
     * Used to represent a TSPGraph object as a string - output the object in JSON format.
     * @return String The TSPGraph represented as a JSON format string.
     */
    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "Failed to load TSPNodeContainer object into JSON format.";
    }

    /**
     * Returns the number of nodes in the graph object's TSPNodeContainer object.
     * @return int The number of TSPNode objects managed by the TSPGraph's TSPNodeContainer object.
     */
    public int getNumNodes() {
        return getNodeContainer().getNodeSet().size();
    }

    public void makeComplete() {
        ArrayList<TSPNode> nodes = getNodeContainer().getNodeSet();
        TSPEdgeContainer edgeContainer = getEdgeContainer();
        for (TSPNode n : nodes) {
            for (TSPNode o : nodes) {
               try {
                   edgeContainer.add(new TSPEdge(n, o));
               } catch (EdgeSuperimpositionException | EdgeToSelfException ignored) {
               }
            }
        }
    }
}
