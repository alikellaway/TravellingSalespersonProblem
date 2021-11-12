package com.alike.tspgraphsystem;

import com.alike.customexceptions.NodeSuperimpositionException;
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
}
