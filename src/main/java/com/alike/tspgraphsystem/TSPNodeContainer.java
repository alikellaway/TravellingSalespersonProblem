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

    private ArrayList<TSPNode> nodeSet;

    private String name;

    public TSPNodeContainer() {
        nodeSet = new ArrayList<>();
    }

    public TSPNodeContainer(ArrayList<TSPNode> nodeSet) throws NodeSuperimpositionException {
        checkNodeSetForSuperimposition(nodeSet);
        nodeSet = new ArrayList<>();
        setNodeSet(nodeSet);
        nodeSet.trimToSize();
    }

    public void add(TSPNode n) throws NodeSuperimpositionException {
        if (!isCoordinateOccupied(n.getCoordinate())) {
            nodeSet.add(n);
            nodeSet.trimToSize();
        }
        else {
            throw new NodeSuperimpositionException("Tried to add a node in an occupied location.");
        }
    }

    public void remove(TSPNode n) {
        nodeSet.remove(n);
        nodeSet.trimToSize();
    }

    public boolean isCoordinateOccupied(Coordinate c) {
        for (TSPNode n : nodeSet) {
            if (n.getCoordinate().equals(c)) {
                return true;
            }
        }
        return false;
    }

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

    public void setNodeSet(ArrayList<TSPNode> nodeSet) throws NodeSuperimpositionException {
        checkNodeSetForSuperimposition(nodeSet);
        this.nodeSet = nodeSet;
    }

    public ArrayList<TSPNode> getNodeSet() {
        return nodeSet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
        for (int i = 0; i < nodes.size() - 1; i++) {
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
        throw new NonExistentNodeException("No node found with ID: " + id);
    }
}
