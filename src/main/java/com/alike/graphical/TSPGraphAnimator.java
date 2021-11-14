package com.alike.graphical;

import com.alike.tspgraphsystem.Coordinate;
import com.alike.tspgraphsystem.TSPEdge;
import com.alike.tspgraphsystem.TSPGraph;
import com.alike.tspgraphsystem.TSPNode;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TSPGraphAnimator extends AnimationTimer {

    private Canvas canvas;
    private TSPGraph graph;
    private GraphicsContext graphicsContext;
    private int editsPerRedraw;
    public static final int NODE_DIAMETER = 60;
    public static final Color NODE_COLOR = Color.RED;

    public TSPGraphAnimator(TSPGraph graph, Canvas canvas, int editsPerRedraw) {
        setGraph(graph);
        setCanvas(canvas);
        setGraphicsContext(canvas.getGraphicsContext2D());
        setEditsPerRedraw(editsPerRedraw);
    }

    @Override
    public void handle(long l) {
        if (graph.getEdgeContainer().getEditCount() % editsPerRedraw == 0) {
            clearCanvas(graphicsContext);
            TSPGraphAnimator.drawGraph(graph, graphicsContext);
        }
    }


    public TSPGraph getGraph() {
        return graph;
    }

    public void setGraph(TSPGraph graph) {
        this.graph = graph;
    }

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    public void setGraphicsContext(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    /**
     * Clears a canvas.
     */
    private void clearCanvas(GraphicsContext gc) {
        gc.clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public int getEditsPerRedraw() {
        return editsPerRedraw;
    }

    public void setEditsPerRedraw(int editsPerRedraw) {
        this.editsPerRedraw = editsPerRedraw;
    }

    public static void drawGraph(TSPGraph graph, GraphicsContext gc) {
        gc.setFill(TSPGraphAnimator.NODE_COLOR);
        gc.setStroke(Color.BLACK);
        for (TSPNode node : graph.getNodeContainer().getNodeSet()) {
            // gc.fillOval(x, y, w, h)
            gc.fillOval(node.getX(), node.getY(), TSPGraphAnimator.NODE_DIAMETER, TSPGraphAnimator.NODE_DIAMETER);
        }
        for (TSPEdge edge : graph.getEdgeContainer().getEdgeSet()) {
            Coordinate start = edge.getStartNode().getCoordinate();
            Coordinate end = edge.getEndNode().getCoordinate();
            Coordinate tempStart = new Coordinate(start.getX(), start.getY());
            Coordinate tempEnd = new Coordinate(end.getX(), end.getY());
            adjustCoordinateToCentreNode(tempStart);
            adjustCoordinateToCentreNode(tempEnd);
            gc.strokeLine(tempStart.getX(), tempStart.getY(), tempEnd.getX(), tempEnd.getY());
        }
    }

    public static void drawRandomGraph(GraphicsContext gc) {
        drawGraph(TSPGraph.generateRandomGraph(), gc);
    }

    private static void adjustCoordinateToCentreNode(Coordinate c) {
        c.setX(c.getX() + TSPGraphAnimator.NODE_DIAMETER/2);
        c.setY(c.getY() + TSPGraphAnimator.NODE_DIAMETER/2);
    }
}
