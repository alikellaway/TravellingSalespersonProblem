package com.alike.dtspgraphsystem;

import com.alike.customexceptions.EdgeToSelfException;
import com.alike.tspgraphsystem.TSPEdge;
import com.alike.tspgraphsystem.TSPNode;

import java.util.ArrayList;

/**
 * Used to manage the state of possible edges on a Graph object. An edge can be 'Online' (represented with true) or
 * 'Offline' (represented with false). If an edge is offline, a solution cannot make an edge between those two nodes.
 */
public class EdgeStateManager {
    /**
     * A map containing
     */
    private ArrayList<String> offlineEdges;

    /**
     * Initialises a new @code{EdgeStateManager} object.
     */
    public EdgeStateManager() {
        reassignOfflineEdges(new ArrayList<>());
    }

    /**
     * Takes an edge offline given a start node and and end node.
     * @param startNode One of the nodes the edge is between.
     * @param endNode The node the edge goes to from the other input node.
     * @throws EdgeToSelfException Thrown if the input nodes have the same ID.
     */
    public void takeOffline(TSPNode startNode, TSPNode endNode) throws EdgeToSelfException {
        try {
            String id = TSPEdge.generateEdgeID(startNode, endNode);
            if (!offlineEdges.contains(id)) { // If the edge is not already in there, add it.
                offlineEdges.add(id);
            }
        } catch (EdgeToSelfException e) {
            throw new EdgeToSelfException("Tried to take an edge offline between nodes with the same node IDs.");
        }
    }

    /**
     * Removes an edge from the offlineEdges list.
     * @param startNode One of the nodes the edge is linking to.
     * @param endNode The other node the edge is linking to.
     * @throws EdgeToSelfException Thrown if the input nodes have the same ID.
     */
    public void takeOnline(TSPNode startNode, TSPNode endNode) throws EdgeToSelfException {
        try {
            String id = TSPEdge.generateEdgeID(startNode, endNode);
            offlineEdges.remove(id);
        } catch (EdgeToSelfException e) {
            throw new EdgeToSelfException("Tried to take an edge online between nodes with the same node IDs.");
        }
    }

    /**
     * Used to check if an edge is currently offline.
     * @param startNode One of the nodes the edges is between.
     * @param endNode The other node the edge is between.
     * @return boolean True if the edge is contained within the offlineEdges arraylist.
     * @throws EdgeToSelfException Thrown if an attempt is made to check the status of an edge between nodes with the
     * same node IDs.
     */
    public boolean isOffline(TSPNode startNode, TSPNode endNode) throws EdgeToSelfException {
        try {
            String id = TSPEdge.generateEdgeID(startNode, endNode);
            return offlineEdges.contains(id);
        } catch (EdgeToSelfException e) {
            throw new EdgeToSelfException("Tried to check status of edge between nodes with the same node IDs.");
        }
    }

    /**
     * Returns the value of the @code{offlineEdges} attribute.
     * @return offlineEdges The value of the @code{offlineEdges} attribute.
     */
    public ArrayList<String> getOfflineEdges() {
        return offlineEdges;
    }

    /**
     * Sets the @code{offlineEdges} attribute to a new value.
     * @param offlineEdges The new value to assign the @code{offlineEdges} attribute.
     */
    public void reassignOfflineEdges(ArrayList<String> offlineEdges) {
        this.offlineEdges = offlineEdges;
    }

    /**
     * Used to represent the @code{EdgeStateManager} object in the console.
     * @return string The @code{EdgeStateManager} represented as a string.
     */
    @Override
    public String toString() {
        return getOfflineEdges().toString();
    }

}
