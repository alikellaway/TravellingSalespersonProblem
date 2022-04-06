package com.alike.staticgraphsystem;

import com.alike.customexceptions.NodeSuperimpositionException;
import com.alike.customexceptions.NonExistentNodeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Used to manage sets of nodes for use in StaticGraph objects.
 * @author alike
 */
public class NodeContainer {

    /**
     * The set of nodes that this container manages.
     */
    private ArrayList<Node> nodeSet;

    /**
     * Used to initialise a new empty NodeContainer object.
     */
    public NodeContainer() {
        nodeSet = new ArrayList<>();
    }

    /**
     * Used to initialise a new filled NodeContainer.
     * @param nodeSet The set of nodes that will populate the container.
     * @throws NodeSuperimpositionException Thrown if two nodes occupy the same coordinates in the input node set.
     */
    public NodeContainer(ArrayList<Node> nodeSet) throws NodeSuperimpositionException {
        checkNodeSetForSuperimposition(nodeSet);
        setNodeSet(nodeSet);
        nodeSet.trimToSize();
    }

    /**
     * Constructs a new NodeContainer from a Coordinate array.
     * @param cL An array of coordinates.
     * @throws NodeSuperimpositionException Thrown when an attempt is made to superimpose nodes.
     */
    public NodeContainer(Collection<Coordinate> cL) throws NodeSuperimpositionException {
        this.setNodeSet(new ArrayList<>());
        for (Coordinate c : cL) {
            add(new Node(c));
        }
    }

    /**
     * Used to add a new node to container.
     * @param n The new node to add.
     * @throws NodeSuperimpositionException Thrown if the new node's coordinates are already occupied by a node already
     * in this container.
     */
    public void add(Node n) throws NodeSuperimpositionException {
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
    public void remove(Node n) {
        nodeSet.remove(n);
        nodeSet.trimToSize();
    }

    /**
     * Used to check if the input coordinates are occupied by a node in this container.
     * @param c The coordinates to check for occupation.
     * @return boolean true if the coordinates are occupied by a node, false if not.
     */
    private boolean isCoordinateOccupied(Coordinate c) {
        for (Node n : nodeSet) {
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
    public void checkNodeSetForSuperimposition(ArrayList<Node> nSet) throws NodeSuperimpositionException {
        for (Node n : nSet) {
            for (int i = nSet.indexOf(n) + 1; i < nSet.size(); i++) {
                if (n.getCoordinate().equals(nSet.get(i).getCoordinate())) {
                    throw new NodeSuperimpositionException("Tried to initialise NodeContainer with input array " +
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
    public void setNodeSet(ArrayList<Node> nodeSet) throws NodeSuperimpositionException {
        checkNodeSetForSuperimposition(nodeSet);
        this.nodeSet = nodeSet;
    }

    /**
     * Returns the value of the @code{nodeSet} attribute.
     * @return nodeSet The value of the nodeSet attribute of this NodeContainer object.
     */
    public ArrayList<Node> getNodeSet() {
        return nodeSet;
    }

    /**
     * Used to represent this NodeContainer object as a JSON format string.
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
        return "Failed to load NodeContainer object into JSON format.";
    }

    /**
     * Returns an array containing all the node IDs of the nodes in the set of this container.
     * @return int[] An array containing all the node IDs of the nodes in the set of this container.
     */
    public ArrayList<Integer> getNodeIDs() {
        ArrayList<Node> nodes = getNodeSet();
        ArrayList<Integer> nodeIDs = new ArrayList<>(nodes.size());
        for (int i = 0; i <= nodes.size() - 1; i++) {
             nodeIDs.add(nodes.get(i).getNodeID());
        }
        return nodeIDs;
    }

    /**
     * Returns a node object given its unique identified (ID). Knowing that the set is always ordered, we can just
     * retrieve by index.
     * @param id The ID attribute of the node you wish to get.
     * @return Node The node with matching ID to input.
     * @throws NonExistentNodeException Thrown if the node was not found.
     */
    public Node getNodeByID(int id) throws NonExistentNodeException {
        Node n = getNodeSet().get(id);
        if (n.getNodeID() != id) {
            throw new NonExistentNodeException("No node found with ID: " + id + " (" + getNodeSet().toString() + ")");
        }
        return n;
    }

    /**
     * Returns a node object given its ID, if this container is not ordered (should never be the case).
     * @param id The ID to find.
     * @return n The node found with a matching ID.
     * @throws NonExistentNodeException Thrown if no node was found with a matching ID.
     */
    public Node getNodeByIdSearch(int id) throws NonExistentNodeException {
        for (Node n : getNodeSet()) {
            if (n.getNodeID() == id) {
                return n;
            }
        }
        throw new NonExistentNodeException("No node found with ID: " + id + " (" + getNodeSet().toString() + ")");
    }

    /**
     * Returns an array list containing all the nodes that have an equal boolean as their @code{visited} attribute.
     * @param getVisitedOrUnvisited The value of visited you would like the nodes of.
     * @return ouput The array list containing the nodes that had a matching value of visited.
     */
    public ArrayList<Node> getNodesWithVisitedState(boolean getVisitedOrUnvisited) {
        ArrayList<Node> output = new ArrayList<>();
        for (Node n : nodeSet) {
            if (n.isVisited() == getVisitedOrUnvisited) {
                output.add(n);
            }
        }
        return output;
    }

    /**
     * Returns the coordinates of all the nodes in the node container.
     * @return cL An array list containing the coordinates of all the nodes in the container.
     */
    public ArrayList<Coordinate> getNodeCoordinates() {
        ArrayList<Coordinate> cL = new ArrayList<>();
        for (Node n : getNodeSet()) {
            cL.add(n.getCoordinate());
        }
        return cL;
    }


}
