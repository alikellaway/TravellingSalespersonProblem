package com.alike.tspgraphsystem;

/**
 * Used to link two TSPNodes together.
 * @author alike
 */
public class TSPEdge {
    /**
     * The node at which the edge starts (note: the edge does not start of finish, its just a naming convention).
     */
    private TSPNode startNode;
    /**
     * The node at which the edge ends (note: the edge does not start of finish, its just a naming convention).
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

    /**
     * Used to check whether this edge links the same two nodes as another edge.
     * @param otherEdge The edge with which to check equality.
     * @return boolean: true if the edges join the same nodes, false if they do not.
     */
    public boolean equals(TSPEdge otherEdge) {
        return this.getEdgeID().equalsIgnoreCase(otherEdge.getEdgeID());
    }

    /**
     * Returns the node this edge starts at.
     * @return startNode The edge this node starts at.
     */
    public TSPNode getStartNode() {
        return startNode;
    }

    /**
     * Sets the @code{startNode} attribute to a new value.
     * @param startNode The new value to assign to the @code{startNode} attribute.
     */
    public void setStartNode(TSPNode startNode) {
        this.startNode = startNode;
    }

    /**
     * Returns the node this edge finishes on.
     * @return @code{endNode} The node this edge ends on.
     */
    public TSPNode getEndNode() {
        return endNode;
    }

    /**
     * Sets the @code{endNode} attribute to a new value.
     * @param endNode The new value to assign the @code{endNode} attribute.
     */
    public void setEndNode(TSPNode endNode) {
        this.endNode = endNode;
    }

    /**
     * Returns the ID of the edge (this may not be unique, since it will
     * @return edgeID The ID of the edge (non-unique).
     */
    public String getEdgeID() {
        return edgeID;
    }

    private void setEdgeID(String edgeID) {
        this.edgeID = edgeID;
    }

    @Override
    public String toString() {
        return getStartNode().toString() + " -- " + getEndNode().toString();
    }

    /**
     * Used to generate a non-unique ID for this edge so that we can check if two edges are joining the same two nodes.
     * @return String a string in the format "lowerNodeID:higherNodeID".
     */
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
