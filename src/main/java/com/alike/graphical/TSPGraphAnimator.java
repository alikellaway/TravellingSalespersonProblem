package com.alike.graphical;

import com.alike.tspgraphsystem.*;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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

    /**
     * The diameter of the nodes in the animation.
     */
    public static final int NODE_DIAMETER = 20; // Nice val = 15

    /**
     * The color of the nodes in the animations.
     */
//    public static final Color NODE_COLOR = Color.GREEN;
    public static final Color NODE_COLOR = Color.rgb(62, 134, 180);

    /**
     * The color of the lines representing the edges of the graph in the animations.
     */
//    public static final Color LINE_COLOR = Color.MAGENTA;
    public static final Color LINE_COLOR = Color.rgb(199, 84, 80);



    /**
     * The width of the edges that are drawn in the animation.
     */
    public static final double LINE_WIDTH = 3; // Nice val = 3/4

    public static final Color BACK_GROUND_COLOR = Color.rgb(35,35,35);

    /**
     * Used to initialise a TSPGraphAnimator object.
     * @param graph The graph that will be animated.
     * @param canvas The canvas we will be drawing to.
     * @param editsPerRedraw The value to assign to the @code{editsPerRedraw} attribute.
     */
    public TSPGraphAnimator(Scene scene, Canvas canvas, TSPGraph graph, int editsPerRedraw) {
        setGraph(graph);
        setCanvas(canvas);
        setGraphicsContext(canvas.getGraphicsContext2D());
        setEditsPerRedraw(editsPerRedraw);
        scene.setFill(BACK_GROUND_COLOR);

    }

    /**
     * The code run each frame of the animation.
     * @param l The length of each frame (nanoseconds).
     */
    @Override
    public void handle(long l) {
        if (graph.getEdgeContainer().getEditCount() % editsPerRedraw == 0) {
            clearCanvas(graphicsContext);
            drawGraph();
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
//        gc.setFill(BACK_GROUND_COLOR);
//        gc.fillRect(0.0, 0.0, scene.getWidth(), scene.getHeight());
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
    public void drawGraph() {
        TSPEdgeContainer edges = graph.getEdgeContainer();
        graphicsContext.setStroke(LINE_COLOR);
        graphicsContext.setLineWidth(LINE_WIDTH);
        for (TSPEdge edge : edges.getEdgeSet()) {
            Coordinate start = edge.getStartNode().getCoordinate();
            Coordinate end = edge.getEndNode().getCoordinate();
            Coordinate tempStart = new Coordinate(start.getX(), start.getY());
            Coordinate tempEnd = new Coordinate(end.getX(), end.getY());
            adjustCoordinateToCentreNode(tempStart);
            adjustCoordinateToCentreNode(tempEnd);
            graphicsContext.strokeLine(tempStart.getX(), tempStart.getY(), tempEnd.getX(), tempEnd.getY());
        }

        graphicsContext.setFill(TSPGraphAnimator.NODE_COLOR);
        for (TSPNode node : graph.getNodeContainer().getNodeSet()) {
            // gc.fillOval(x, y, w, h)
            graphicsContext.fillOval(node.getX(), node.getY(), TSPGraphAnimator.NODE_DIAMETER, TSPGraphAnimator.NODE_DIAMETER);
        }
    }

    /**
     * Used to adjust coordinates of edges so that they appear to end and start in the middle of drawn nodes.
     * @param c The coordinate to adjust.
     */
    private static void adjustCoordinateToCentreNode(Coordinate c) {
        c.setX(c.getX() + TSPGraphAnimator.NODE_DIAMETER/2);
        c.setY(c.getY() + TSPGraphAnimator.NODE_DIAMETER/2);
    }
}
