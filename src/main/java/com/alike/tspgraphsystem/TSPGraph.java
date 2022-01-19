package com.alike.tspgraphsystem;

import com.alike.customexceptions.EdgeSuperimpositionException;
import com.alike.customexceptions.EdgeToSelfException;
import com.alike.customexceptions.NodeSuperimpositionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to represent and collate data on a travelling salesperson problem graph.
 * TSPGraph.java
 * @author alike
 */
public class TSPGraph implements Graph {

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
     * Constructs a new graph object given a node container and an edge container.
     * @param nodeContainer The value that will become the @code{nodeContainer} attribute.
     * @param edgeContainer The value that will become the @code{edgeContainer} attribute.
     */
    public TSPGraph(TSPNodeContainer nodeContainer, TSPEdgeContainer edgeContainer) {
        setNodeContainer(nodeContainer);
        setEdgeContainer(edgeContainer);
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

    /**
     * Ensures all nodes are set to unvisited.
     */
    public void setAllNodesUnvisited() {
        for (TSPNode n : getNodeContainer().getNodeSet()) {
            n.setVisited(false);
        }
    }

    public TSPGraph copy() throws NodeSuperimpositionException, EdgeSuperimpositionException {
        TSPGraph graphCopy = new TSPGraph();
        ArrayList<TSPNode> nodesCopy = new ArrayList<>(getNodeContainer().getNodeSet());
        graphCopy.setNodeContainer(new TSPNodeContainer(nodesCopy));
        graphCopy.setEdgeContainer(getEdgeContainer().copy());
        return graphCopy;
    }
}
