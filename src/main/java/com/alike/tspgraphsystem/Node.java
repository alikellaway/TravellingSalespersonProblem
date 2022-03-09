package com.alike.tspgraphsystem;

import com.alike.Main;
import com.alike.customexceptions.NoClosestNodeException;

import java.util.ArrayList;

/**
 * Used to represent nodes (cities) in the TSP.
 * @author alike
 */
public class Node {
    /**
     * A record of the number of nodes initialised used to assign each node a unique ID.
     */
    private static int numNodes = 0;

    /**
     * A record of whether a solution has visited the node.
     */
    private boolean visited = false;

    /**
     * The coordinate of the node.
     */
    private Coordinate coordinate;

    /**
     * The unique ID of this node object.
     */
    private int nodeID;

    /**
     * Initialises a node given an x and y coordinate value.
     * @param x The x positional value of the node.
     * @param y The y positional valye of the node.
     */
    public Node(int x, int y) {
        Coordinate c = new Coordinate(x, y);
        setCoordinate(c);
        assignID();
    }

    /**
     * Initialises a node given a GraphSystem.Coordinate object.
     * @param c The GraphSystem.Coordinate to give the node object.
     */
    public Node(Coordinate c) {
        setCoordinate(c);
        assignID();
    }

    /**
     * Used to get the vector from this node to another parameter node.
     * @param otherNode The node to find a vector to.
     * @return Vector A vector describing the distance between this node and the parameter node.
     */
    public Vector getVectorTo(Node otherNode) {
        return coordinate.getVectorTo(otherNode.getCoordinate());
    }

    /**
     * Returns the coordinate attribute of the node.
     * @return GraphSystem.Coordinate The coordinate attribute of the node.
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Sets the coordinate attribute to a new coordinate value.
     * @param coordinate The new coordinate to set the node's coordinate attribute to.
     */
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * Used to represent the Node as a string (in console or terminal).
     * @return String The Node object as a string.
     */
    @Override
    public String toString() {
        return getCoordinate().toString() + ":" + getNodeID();
    }

    /**
     * Assigns a unique ID to the node in the @code{nodeID} attribute.
     */
    private void assignID() {
        setNodeID(numNodes);
        numNodes++;
    }

    /**
     * Used to generate a random Node object with random coordinates within the parameters specified by the
     * static final variables from the Main class.
     * @return Node A new Node with random coordinate values.
     */
    public static Node generateRandomTSPNode() {
        return new Node(Coordinate.generateRandomCoordinate(Main.COORDINATE_MAX_WIDTH, Main.COORDINATE_MAX_HEIGHT));
    }

    /**
     * Used to check whether the Node has been visited by an algorithm in this run.
     * @return true: if the node has been visited, false: if the node has not been visited (by a solver)
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Sets the @code{visited} attribute to a new boolean value.
     * @param visited The new boolean value to assign to the @code{visited} attribute.
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Returns the value of the @code{nodeID} attribute.
     * @return nodeID The value of the @code{nodeID} attribute.
     */
    public int getNodeID() {
        return nodeID;
    }

    /**
     * Sets the value of the @code{nodeID} attribute to a new value.
     * @param nodeID The new value to assign the @code{nodeID} attribute to.
     */
    private void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    /**
     * Returns the value of the @code{x} attribute of the coordinate attribute of the Node.
     * @return x The value of the @code{x} attribute of the coordinate attribute of the Node.
     */
    public double getX() {
        return getCoordinate().getX();
    }

    /**
     * Returns the value of the @code{y} attribute of the coordinate attribute of the Node.
     * @return y The value of the @code{y} attribute of the coordinate attribute of the Node.
     */
    public double getY() {
        return getCoordinate().getY();
    }

    /**
     * Sets the value of the @code{numNodes} attribute to 0.
     */
    public static void restartNodeCounter() {
        numNodes = 0;
    }

    /**
     * Finds the closest node to this node in the input array of nodes.
     * @param otherNodes The input array of node from which we will find a closest node.
     * @return closestFoundNode The closest found node to this node.
     * @throws NoClosestNodeException Thrown if a closest node could not be found.
     */
    public Node getClosestNode(ArrayList<Node> otherNodes, boolean unvisited) throws NoClosestNodeException {
        if (!otherNodes.contains(this)) {
            throw new NoClosestNodeException("This node was not found in the input list.");
        }
        Node closestFoundNode = null;
        double distanceToClosestNode = Double.MAX_VALUE;
        for (Node n : otherNodes) {
            if (!(this.equals(n))) {
                if ((unvisited && !n.isVisited()) || (!unvisited)) {
                    double distance = this.getVectorTo(n).magnitude();
                    if (distance < distanceToClosestNode) {
                        closestFoundNode = n;
                        distanceToClosestNode = distance;
                    }
                }
            }
        }
        if (closestFoundNode == null) {
            throw new NoClosestNodeException("There was no closest node.");
        }
        return closestFoundNode;
    }

}
