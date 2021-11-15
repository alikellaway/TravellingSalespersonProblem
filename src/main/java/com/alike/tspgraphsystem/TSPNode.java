package com.alike.tspgraphsystem;

import com.alike.Main;

/**
 * Used to represent nodes (cities) in the TSP.
 * @author alike
 */
public class TSPNode {
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

//    /**
//     * A record of the edges currently connected to this node. This is used to quickly check for edge superimposition
//     * rather than checking all other edges that exist (Note: "in" and "out" are arbitrary).
//     */
//    private TSPEdge inEdge;
//    private TSPEdge outEdge;
    /**
     * The unique ID of this node object.
     */
    private int nodeID;

    /**
     * Initialises a node given an x and y coordinate value.
     * @param x The x positional value of the node.
     * @param y The y positional valye of the node.
     */
    public TSPNode(int x, int y) {
        Coordinate c = new Coordinate(x, y);
        setCoordinate(c);
        assignID();
//        setInEdge(null);
//        setOutEdge(null);
    }

    /**
     * Initialises a node given a GraphSystem.Coordinate object.
     * @param c The GraphSystem.Coordinate to give the node object.
     */
    public TSPNode(Coordinate c) {
        setCoordinate(c);
        assignID();
//        setInEdge(null);
//        setOutEdge(null);
    }


//    public int getNodeOrder() {
//        int out = 0;
//        if (getInEdge() != null) {out++;}
//        if (getOutEdge() != null) {out++;}
//        return out;
//    }

    public Vector getVectorTo(TSPNode otherNode) {
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

    public double getX() {
        return getCoordinate().getX();
    }

    public double getY() {
        return getCoordinate().getY();
    }

    @Override
    public String toString() {
        return getCoordinate().toString();
    }

    private void assignID() {
        setNodeID(numNodes);
        numNodes++;
    }

    public int getNodeID() {
        return nodeID;
    }

    private void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    public static TSPNode generateRandomTSPNode() {
        return new TSPNode(Coordinate.generateRandomCoordinate(Main.COORDINATE_MAX_WIDTH, Main.COORDINATE_MAX_HEIGHT));
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}