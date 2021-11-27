package com.alike.graphical;

import com.alike.tspgraphsystem.*;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Class runs at frame rate and views the graph object input during construction and then updates the canvas object
 * input during construction to represent what the graph looks like in that during that moment. This is not synced with
 * solution classes - it runs simultaneously.
 * @author alike
 */
public class TSPGraphAnimator extends AnimationTimer {
    /**
     * The canvas on which we are animating.
     */
    private Canvas canvas;

    /**
     * The graph which we are animating.
     */
    private TSPGraph graph;

    /**
     * The graphics context of the canvas object.
     */
    private GraphicsContext graphicsContext;

    /**
     * The amount of edits to a graph after which we will redraw it (for optimisation algorithms or big maps).
     */
    private int editsPerRedraw;

    private boolean drawNodeIDs;

    /**
     * The drawn radius of the nodes.
     */
    public static final int NODE_RADIUS = 10; // Nice val = 10

    /**
     * The width of the edges that are drawn in the animation.
     */
    private static final double LINE_WIDTH = 2; // Nice val = 2/3/4

    /**
     * The color of the nodes in the animations.
     */
//    public static final Color NODE_COLOR = Color.GREEN;
    private static final Color NODE_COLOR = Color.rgb(62, 134, 180);

    private static final Color NODE_OUTLINE_COLOR = Color.rgb(20, 20, 20);

    private static final double NODE_OUTLINE_WIDTH = 2.5;

    /**
     * The color of the lines representing the edges of the graph in the animations.
     */
//    public static final Color LINE_COLOR = Color.MAGENTA;
    private static final Color LINE_COLOR = Color.rgb(199, 84, 80);



    /**
     * Used to initialise a TSPGraphAnimator object.
     * @param graph The graph that will be animated.
     * @param canvas The canvas we will be drawing to.
     * @param editsPerRedraw The value to assign to the @code{editsPerRedraw} attribute.
     */
    public TSPGraphAnimator(Canvas canvas, TSPGraph graph, int editsPerRedraw, boolean drawNodeIDs) {
        setGraph(graph);
        setCanvas(canvas);
        setGraphicsContext(canvas.getGraphicsContext2D());
        setEditsPerRedraw(editsPerRedraw);
        setDrawNodeIDs(drawNodeIDs);
    }

    /**
     * The code run each frame of the animation.
     * @param l The length of each frame (nanoseconds).
     */
    @Override
    public void handle(long l) {
        if (graph.getEdgeContainer().getEditCount() % editsPerRedraw == 0) {
            clearCanvas(graphicsContext);
            drawGraph(areDrawNodeIDs());
        }
    }

    /**
     * Sets the @code{graph} attribute to a new value.
     * @param graph The new value to set the @code{graph} attribute to.
     */
    public void setGraph(TSPGraph graph) {
        this.graph = graph;
    }

    /**
     * Returns the value of the @code{graphicsContext} attribute.
     * @return @code{graphicsContext} The value of the @code{graphicsContext} attribute.
     */
    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    /**
     * Sets the value of the @code{graphicsContext} attribute to a new value.
     * @param graphicsContext The new value to assign the @code{graphicsContext}.
     */
    public void setGraphicsContext(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    /**
     * Clears a canvas (makes the canvas white, so we can redraw the graph).
     * @param gc The @code{graphicsContext} attribute of the canvas object.
     */
    private void clearCanvas(GraphicsContext gc) {
        gc.clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Sets the @code{canvas} attribute to a new value.
     * @param canvas The new value to assign the @code{canvas} attribute.
     */
    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    /**
     * Sets the @code{editsPerRedraw} attribute to a new value.
     * @param editsPerRedraw The new value to assign the @code{editsPerRedraw} attribute.
     */
    public void setEditsPerRedraw(int editsPerRedraw) {
        this.editsPerRedraw = editsPerRedraw;
    }

    /**
     * Draws the currently stored graph in its current state into the graphicsContext of the current canvas.
     */
    public void drawGraph(boolean numbered) {
        // Design lines
        graphicsContext.setStroke(LINE_COLOR);
        graphicsContext.setLineWidth(LINE_WIDTH);
        // graphicsContext.setLineDashes(5, 10); // Set lines to dotted
        // Draw lines
        for (TSPEdge edge : graph.getEdgeContainer().getEdgeSet()) {
            drawEdge(edge);
        }
        // Design nodes
        graphicsContext.setFill(TSPGraphAnimator.NODE_COLOR);
        graphicsContext.setStroke(TSPGraphAnimator.NODE_OUTLINE_COLOR);
        graphicsContext.setLineWidth(NODE_OUTLINE_WIDTH);
        // graphicsContext.setLineDashes(); // Turns of dotted lines when outlining nodes
        // Draw nodes
        for (TSPNode node : graph.getNodeContainer().getNodeSet()) {
            drawNode(node);
        }
        // Number nodes
        if (numbered) {
            graphicsContext.setFill(LINE_COLOR);
            for (TSPNode node: graph.getNodeContainer().getNodeSet()) {
                graphicsContext.fillText(Integer.toString(node.getNodeID()), node.getX(), node.getY() - 2);
            }
        }
    }

    /**
     * Draws an edge onto the current canvas.
     * @param edge The edge to be drawn onto the current canvas.
     */
    private void drawEdge(TSPEdge edge) {
        Coordinate start = edge.getStartNode().getCoordinate();
        Coordinate end = edge.getEndNode().getCoordinate();
        Coordinate tempStart = new Coordinate(start.getX(), start.getY());
        Coordinate tempEnd = new Coordinate(end.getX(), end.getY());
        adjustCoordinateToCentreNode(tempStart);
        adjustCoordinateToCentreNode(tempEnd);
        graphicsContext.strokeLine(tempStart.getX(), tempStart.getY(), tempEnd.getX(), tempEnd.getY());
    }

    /**
     * Draws a node onto the current canvas.
     * @param node The node to be drawn on the current canvas.
     */
    private void drawNode(TSPNode node) {
        // Oval format : gc.fillOval(x, y, w, h)
        graphicsContext.fillOval(node.getX(), node.getY(), NODE_RADIUS*2, NODE_RADIUS*2); // Main node
        graphicsContext.strokeOval(node.getX(), node.getY(), NODE_RADIUS*2, NODE_RADIUS*2); // Node outline
    }

    /**
     * Used to adjust coordinates of edges so that they appear to end and start in the middle of drawn nodes.
     * @param c The coordinate to adjust.
     */
    private static void adjustCoordinateToCentreNode(Coordinate c) {
        c.setX(c.getX() + TSPGraphAnimator.NODE_RADIUS);
        c.setY(c.getY() + TSPGraphAnimator.NODE_RADIUS);
    }

    public boolean areDrawNodeIDs() {
        return drawNodeIDs;
    }

    public void setDrawNodeIDs(boolean drawNodeIDs) {
        this.drawNodeIDs = drawNodeIDs;
    }
}
