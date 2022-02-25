package com.alike.tspgraphsystem;

import com.alike.customexceptions.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

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

    private double[][] edgeLengthMatrix;

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
     * Used to create a graph with no edges but with a node container.
     * @param nodeContainer The node container the graph will have as its node container.
     */
    public TSPGraph(TSPNodeContainer nodeContainer) {
        setNodeContainer(nodeContainer);
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
     * Returns the value of the @code{edgeLengthMatrix} attribute.
     * @return edgeLengthMatrix The value of the @code{edgeLengthMatrix} attribute.
     */
    public double[][] getEdgeLengthMatrix() {
        return edgeLengthMatrix;
    }

    /**
     * Sets the value of the @code{edgeLengthMatrix} attribute to a new value.
     * @param edgeLengthMatrix The new value to assign to the @code{edgeLengthMatrix} attribute.
     */
    public void setEdgeLengthMatrix(double[][] edgeLengthMatrix) {
        this.edgeLengthMatrix = edgeLengthMatrix;
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

    /**
     * Call to construct a matrix containing all the edge lengths between each node in the graph. Method is not called
     * automatically, so for a graph to have an edge length matrix value, this MUST be called.
     * @throws NoNodeContainerException Thrown if the graph does not currently have a node container.
     */
    public void constructEdgeLengthMatrix() throws NoNodeContainerException {
        if (nodeContainer == null) { // Check that we have nodes
            throw new NoNodeContainerException("Tried to construct an edge length matrix on a " +
                    "graph with no node container");
        }
        // Construct the edge matrix
        int nN = getNumNodes();
        double[][] edgeLengthMatrix = new double[nN][nN];
        for (int y = 0; y < nN; y++) {
            try {
                Coordinate snPos = getNodeContainer().getNodeByID(y).getCoordinate();
                for (int x = y; x < nN; x++) { // Since the graph is symmetrical, we can fill the matrix in one.
                    if (x == y) { // If they are the same node, then the distance is null.
                        edgeLengthMatrix[y][x] = 0; // Since nodes cannot occupy the same space, their distance cannot be 0
                    } else {
                        Coordinate enPos = getNodeContainer().getNodeByID(x).getCoordinate();
                        edgeLengthMatrix[y][x] = snPos.getVectorTo(enPos).magnitude();
                        edgeLengthMatrix[x][y] = snPos.getVectorTo(enPos).magnitude();
                    }

                }
            } catch (NonExistentNodeException e) {
                e.printStackTrace();
            }
        }
        setEdgeLengthMatrix(edgeLengthMatrix);
    }
}
