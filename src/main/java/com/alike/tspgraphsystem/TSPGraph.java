package com.alike.tspgraphsystem;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TSPGraph {

    private TSPNodeContainer nodeContainer;
    private TSPEdgeContainer edgeContainer;

    private final Color nodeColor = Color.RED;
    private final int nodeDiameter = 10;


    public TSPGraph(TSPNodeContainer nodeContainer, TSPEdgeContainer edgeContainer) {
        setNodeContainer(nodeContainer);
        setEdgeContainer(edgeContainer);
    }

    public TSPGraph() {
        setNodeContainer(new TSPNodeContainer());
        setEdgeContainer(new TSPEdgeContainer());
    }

    public TSPNodeContainer getNodeContainer() {
        return nodeContainer;
    }

    public void setNodeContainer(TSPNodeContainer nodeContainer) {
        this.nodeContainer = nodeContainer;
    }

    public TSPEdgeContainer getEdgeContainer() {
        return edgeContainer;
    }

    public void setEdgeContainer(TSPEdgeContainer edgeContainer) {
        this.edgeContainer = edgeContainer;
    }

    public void drawGraph(GraphicsContext gc) {
        gc.setFill(nodeColor);
        for (TSPNode node : nodeContainer.getNodeSet()) {
            // gc.fillOval(x, y, w, h)
            gc.fillOval(node.getX(), node.getY(), nodeDiameter, nodeDiameter);
        }
        for (TSPEdge edge : edgeContainer.getEdgeSet()) {
            Coordinate start = edge.getStartNode().getCoordinate();
            Coordinate end = edge.getEndNode().getCoordinate();
            Coordinate tempStart = new Coordinate(start.getX(), start.getY());
            Coordinate tempEnd = new Coordinate(end.getX(), end.getY());
            adjustCoordinateToCentreNode(tempStart);
            adjustCoordinateToCentreNode(tempEnd);
            gc.strokeLine(tempStart.getX(), tempStart.getY(), tempEnd.getX(), tempEnd.getY());
        }
    }

    private void adjustCoordinateToCentreNode(Coordinate c) {
        c.setX(c.getX() + nodeDiameter/2);
        c.setY(c.getY() + nodeDiameter/2);
    }
}
