package com.alike.tspgraphsystem;

import com.alike.customexceptions.EdgeSuperimpositionException;
import com.alike.customexceptions.NodeOrderException;

/**
 * Used to link two TSPNodes together.
 * @author alike
 */
public class TSPEdge {
    /**
     * The node at which the edge starts.
     */
    private TSPNode startNode;
    /**
     * The node at which the edge ends.
     */
    private TSPNode endNode;

    /**
     * The unique identifier of this edge's ID.
     * Edges between the same two nodes will have the same edge ID value.
     */
    private String edgeID;

    /**
     * Used to initialise a new edge object.
     * @param startNode The node at which the edge starts.
     * @param endNode The node at which the edge ends.
     */
    public TSPEdge(TSPNode startNode, TSPNode endNode) {
        setStartNode(startNode);
        setEndNode(endNode);
        setEdgeID(generateID());
    }

    public boolean equals(TSPEdge otherEdge) {
        return this.getEdgeID().equalsIgnoreCase(otherEdge.getEdgeID());
    }

    /**
     *
     * @return
     */
    public TSPNode getStartNode() {
        return startNode;
    }

    public void setStartNode(TSPNode startNode) {
        this.startNode = startNode;
    }

    public TSPNode getEndNode() {
        return endNode;
    }

    public void setEndNode(TSPNode endNode) {
        this.endNode = endNode;
    }

    public String getEdgeID() {
        return edgeID;
    }

    private void setEdgeID(String edgeID) {
        this.edgeID = edgeID;
    }

    @Override
    public String toString() {
        return getStartNode().toString() + "-" + getEndNode().toString();
    }

    private String generateID() {
        int startID = getStartNode().getNodeID();
        int endID = getEndNode().getNodeID();
        if (startID >= endID) {
            return endID + ":" + startID;
        } else {
            return startID + ":" + endID;
        }
    }
}
