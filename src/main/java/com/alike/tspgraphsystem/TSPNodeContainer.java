package com.alike.tspgraphsystem;

import com.alike.customexceptions.NodeSuperimpositionException;
import com.alike.customexceptions.NonExistentNodeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;

/**
 * Used to manage sets of nodes for use in TSPGraph objects.
 * @author alike
 */
public class TSPNodeContainer {

    /**
     * The set of nodes that this container manages.
     */
    private ArrayList<TSPNode> nodeSet;

    /**
     * The name of this node set (if applicable).
     */
    private String name;

    /**
     * Used to initialise a new empty TSPNodeContainer object.
     */
    public TSPNodeContainer() {
        nodeSet = new ArrayList<>();
    }

    /**
     * Used to initialise a new filled TSPNodeContainer.
     * @param nodeSet The set of nodes that will populate the container.
     * @throws NodeSuperimpositionException Thrown if two nodes occupy the same coordinates in the input node set.
     */
    public TSPNodeContainer(ArrayList<TSPNode> nodeSet) throws NodeSuperimpositionException {
        checkNodeSetForSuperimposition(nodeSet);
        nodeSet = new ArrayList<>();
        setNodeSet(nodeSet);
        nodeSet.trimToSize();
    }

    /**
     * Used to add a new node to container.
     * @param n The new node to add.
     * @throws NodeSuperimpositionException Thrown if the new node's coordinates are already occupied by a node already
     * in this container.
     */
    public void add(TSPNode n) throws NodeSuperimpositionException {
        if (!isCoordinateOccupied(n.getCoordinate())) {
            nodeSet.add(n);
            nodeSet.trimToSize();
        }
        else {
            throw new NodeSuperimpositionException("Tried to add a node in an occupied location.");
        }
    }

    /**
     * Used to remove a node from this container.
     * @param n The node to remove.
     */
    public void remove(TSPNode n) {
        nodeSet.remove(n);
        nodeSet.trimToSize();
    }

    /**
     * Used to check if the input coordinates are occupied by a node in this container.
     * @param c The coordinates to check for occupation.
     * @return boolean true if the coordinates are occupied by a node, false if not.
     */
    public boolean isCoordinateOccupied(Coordinate c) {
        for (TSPNode n : nodeSet) {
            if (n.getCoordinate().equals(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Used to check an input node set for nodes occupying the same coordinates.
     * @param nSet The node set to check.
     * @throws NodeSuperimpositionException Thrown if nodes where found to occupy the same coordinates.
     */
    public void checkNodeSetForSuperimposition(ArrayList<TSPNode> nSet) throws NodeSuperimpositionException {
        for (TSPNode n : nSet) {
            for (int i = nSet.indexOf(n) + 1; i < nSet.size(); i++) {
                if (n.getCoordinate().equals(nSet.get(i).getCoordinate())) {
                    throw new NodeSuperimpositionException("Tried to initialise TSPNodeContainer with input array " +
                            "containing superimposed nodes.");
                }
            }
        }
    }

    /**
     * Sets the node set to a new value.
     * @param nodeSet The new value to become the node set.
     * @throws NodeSuperimpositionException Thrown if the new node set contains nodes occupying the same coordinates.
     */
    public void setNodeSet(ArrayList<TSPNode> nodeSet) throws NodeSuperimpositionException {
        checkNodeSetForSuperimposition(nodeSet);
        this.nodeSet = nodeSet;
    }

    /**
     * Returns the value of the @code{nodeSet} attribute.
     * @return @code{nodeSet} The value of the nodeSet attribute of this TSPNodeContainer object.
     */
    public ArrayList<TSPNode> getNodeSet() {
        return nodeSet;
    }

    /**
     * Used to represent this TSPNodeContainer object as a JSON format string.
     * @return String This container as a JSON format string.
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
     * Returns an array containing all the node IDs of the nodes in the set of this container.
     * @return int[] An array containing all the node IDs of the nodes in the set of this container.
     */
    public ArrayList<Integer> getNodeIDs() {
        ArrayList<TSPNode> nodes = getNodeSet();
        ArrayList<Integer> nodeIDs = new ArrayList<>(nodes.size());
        for (int i = 0; i <= nodes.size() - 1; i++) {
             nodeIDs.add(nodes.get(i).getNodeID());
        }
        return nodeIDs;
    }

    /**
     * Returns a node object given its unique identified (ID).
     * @param id The ID attribute of the node you wish to get.
     * @return TSPNode The node with matching ID to input.
     * @throws NonExistentNodeException Thrown if the node was not found.
     */
    public TSPNode getNodeByID(int id) throws NonExistentNodeException {
        for (TSPNode n : getNodeSet()) {
            if (n.getNodeID() == id) {
                return n;
            }
        }
        throw new NonExistentNodeException("No node found with ID: " + id + " (" + getNodeSet().toString() + ")");
    }
}
