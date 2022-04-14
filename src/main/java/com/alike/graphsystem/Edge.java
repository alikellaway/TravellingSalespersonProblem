package com.alike.graphsystem;

import com.alike.customexceptions.EdgeToSelfException;

import java.util.Comparator;

/**
 * Used to link two TSPNodes together.
 * @author alike
 */
public class Edge implements Comparable<Edge>, Comparator<Edge> {
    /**
     * The node at which the edge starts (note: the edge does not start of finish, its just a naming convention).
     */
    private Node startNode;
    /**
     * The node at which the edge ends (note: the edge does not start of finish, its just a naming convention).
     */
    private Node endNode;

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
    public Edge(Node startNode, Node endNode) throws EdgeToSelfException {
        if (startNode == endNode) {
            throw new EdgeToSelfException("Cannot create an edge between 1 node.");
        }
        setStartNode(startNode);
        setEndNode(endNode);
        setEdgeID(generateEdgeID(startNode, endNode));
    }

    /**
     * Constructs a new Edge that can be used as the comparator in list sorting.
     */
    public Edge() {}

    /**
     * Used to check whether this edge links the same two nodes as another edge.
     * @param otherEdge The edge with which to check equality.
     * @return boolean: true if the edges join the same nodes, false if they do not.
     */
    public boolean equals(Edge otherEdge) {
        return this.getEdgeID().equalsIgnoreCase(otherEdge.getEdgeID());
    }

    /**
     * Returns the node this edge starts at.
     * @return startNode The edge this node starts at.
     */
    public Node getStartNode() {
        return startNode;
    }

    /**
     * Sets the @code{startNode} attribute to a new value.
     * @param startNode The new value to assign to the @code{startNode} attribute.
     */
    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    /**
     * Returns the node this edge finishes on.
     * @return @code{endNode} The node this edge ends on.
     */
    public Node getEndNode() {
        return endNode;
    }

    /**
     * Sets the @code{endNode} attribute to a new value.
     * @param endNode The new value to assign the @code{endNode} attribute.
     */
    public void setEndNode(Node endNode) {
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
    public static String generateEdgeID(Node startNode, Node endNode) throws EdgeToSelfException {
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
    public boolean containsNode(Node n) {
        return startNode.equals(n) || endNode.equals(n);
    }

    /**
     * Outputs the length of this edge as a double.
     * @return length The length of this edge as a double.
     */
    public double getLength() {
        return startNode.getVectorTo(endNode).magnitude();
    }

    /**
     * Used to compare the length of this edge to other edge objects.
     * @param otherEdge The edge which we are comparing lengths to.
     * @return int The required outputs as per the @code{Comparable} interface implementation.
     */
    @Override
    public int compareTo(Edge otherEdge) {
        return Double.compare(this.getLength(), otherEdge.getLength());
    }

    /**
     * Used to sort edges into order using their lengths.
     * @param o1 The first edge.
     * @param o2 The second edge.
     * @return Returns the result of Double.compare() of the lengths.
     */
    @Override
    public int compare(Edge o1, Edge o2) {
        double l1 = o1.getLength();
        double l2 = o2.getLength();
        return Double.compare(l1, l2);
    }
}
