package com.alike.tspgraphsystem;

import com.alike.customexceptions.EdgeToSelfException;

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
    public TSPEdge(TSPNode startNode, TSPNode endNode) throws EdgeToSelfException {
        if (startNode == endNode) {
            throw new EdgeToSelfException("Cannot create an edge between 1 node.");
        }
        setStartNode(startNode);
        setEndNode(endNode);
        setEdgeID(generateEdgeID(startNode, endNode));
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

    /**
     * Sets the @code{edgeID} attribute to a new value.
     * @param edgeID The new value to assign to the @code{edgeID} attribute.
     */
    private void setEdgeID(String edgeID) {
        this.edgeID = edgeID;
    }

    /**
     * Used to output the edge as a string for viewing in text form.
     * @return edgeString The edge represented as a string.
     */
    @Override
    public String toString() {
        return getStartNode().toString() + " -- " + getEndNode().toString();
    }

    /**
     * Used to generate a non-unique edge ID between two nodes (non-unique as its the same output for switched inputs).
     * Its static because it needs to be called in the edge manager class to calculate edge IDs efficiently.
     * @param startNode One of the nodes the edge is linking.
     * @param endNode The other node the edge is linking.
     * @return edgeID The ID of this edge.
     */
    public static String generateEdgeID(TSPNode startNode, TSPNode endNode) throws EdgeToSelfException {
        int startID = startNode.getNodeID();
        int endID = endNode.getNodeID();
        if (startID == endID) {
            throw new EdgeToSelfException("Attempt made to create edge between nodes with equal ID's.");
        }
        if (startID >= endID) {
            return endID + ":" + startID;
        } else {
            return startID + ":" + endID;
        }
    }

    /**
     * Returns a boolean describing whether an edge starts or ends on the parameter node.
     * @param n The node to check the edge for.
     * @return boolean true if the edge does touch the paramet node.
     */
    public boolean containsNode(TSPNode n) {
        return startNode.equals(n) || endNode.equals(n);
    }
}
