package com.alike.graphical;

import com.alike.graphsystem.*;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Class runs at frame rate and views the graph object input during construction and then updates the canvas object
 * input during construction to represent what the graph looks like in that during that moment. This is not synced with
 * solution classes - it runs simultaneously.
 * @author alike
 */
public class TSPGraphAnimator extends AnimationTimer {

    /**
     * The scene in which the graph is being animated (reference is used to update the title of the scene).
     */
    private Stage stage;

    /**
     * The canvas on which we are animating.
     */
    private Canvas canvas;

    /**
     * The graph which we are animating.
     */
    private StaticGraph graph;

    /**
     * The graphics context of the canvas object.
     */
    private GraphicsContext graphicsContext;

    /**
     * The amount of edits to a graph after which we will redraw it (for optimisation algorithms or big maps).
     */
    private int editsPerRedraw;

    /**
     * A boolean describing whether node ids should be drawn onto the animation. Drawn if true.
     */
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
    private static final Color NODE_COLOR = Color.rgb(138, 99, 85);
    // nice blue = Color.rgb(62, 134, 180);
    // nice green = Color.rgb(77, 170, 81);
    // twig brown = Color.rgb(138, 99, 85);
    // bee yellow = Color.rgb(247, 186, 17);
    // Neat purple = Color.rgb(88, 0, 255);

    /**
     * The colour of the outline of the node.
     */
    private static final Color NODE_OUTLINE_COLOR = Color.rgb(20, 20, 20);

    /**
     * The width of the outline of the node.
     */
    private static final double NODE_OUTLINE_WIDTH = 2.5;

    /**
     * The color of the lines representing the edges of the graph in the animations.
     */
    private static final Color LINE_COLOR = Color.rgb(77, 170, 81);
    // nice red = Color.rgb(199, 84, 80);
    // white = Color.rgb(240, 240, 240);  --Pure white is too bright
    // leaf green = Color.rgb(77, 170, 81);

    /**
     * Used to initialise a TSPGraphAnimator object.
     * @param stage The stage on which the @code{canvas} will be animated.
     * @param canvas The canvas on which the @code{graph} will be animated.
     * @param graph The graph that will be animated.
     * @param editsPerRedraw The number of edits accrued before the graph is redrawn.
     * @param drawNodeIDs Whether or not the node numbers should be displayed in the drawing.
     */
    public TSPGraphAnimator(Stage stage, Canvas canvas, StaticGraph graph, int editsPerRedraw, boolean drawNodeIDs) {
        setGraph(graph);
        setCanvas(canvas);
        setGraphicsContext(canvas.getGraphicsContext2D());
        setEditsPerRedraw(editsPerRedraw);
        setDrawNodeIDs(drawNodeIDs);
        setStage(stage);
    }

    /**
     * The code run each frame of the animation.
     * @param l The length of each frame (nanoseconds).
     */
    @Override
    public void handle(long l) {
        if (graph.getEdgeContainer().getEditCount() % editsPerRedraw == 0) {
            drawGraph(areDrawNodeIDs());
        }
        stage.setTitle("TSP Length: " + String.format("%.2f", graph.getEdgeContainer().getTotalLength()));
    }

    /**
     * Sets the @code{graph} attribute to a new value.
     * @param graph The new value to set the @code{graph} attribute to.
     */
    public void setGraph(StaticGraph graph) {
        this.graph = graph;
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
     * @param numbered Whether or not node numbers should be displayed in the drawing.
     */
    public void drawGraph(boolean numbered) {
        clearCanvas(graphicsContext);
        // Design lines
        graphicsContext.setStroke(LINE_COLOR);
        graphicsContext.setLineWidth(LINE_WIDTH);
        // graphicsContext.setLineDashes(5, 10); // Set lines to dotted
        // Draw lines
        for (Edge edge : graph.getEdgeContainer().getEdgeSet()) {
            drawEdge(edge);
        }
        // Design nodes
        graphicsContext.setFill(TSPGraphAnimator.NODE_COLOR);
        graphicsContext.setStroke(TSPGraphAnimator.NODE_OUTLINE_COLOR);
        graphicsContext.setLineWidth(NODE_OUTLINE_WIDTH);
        // graphicsContext.setLineDashes(); // Turns of dotted lines when outlining nodes
        // Draw nodes
        for (Node node : graph.getNodeContainer().getNodeSet()) {
            drawNode(node);
        }
        // Number nodes
        if (numbered) {
            graphicsContext.setFill(LINE_COLOR);
            for (Node node: graph.getNodeContainer().getNodeSet()) {
                graphicsContext.fillText(Integer.toString(node.getNodeID()), node.getX(), node.getY() - 2);
            }
        }
    }

    /**
     * Draws an edge onto the current canvas.
     * @param edge The edge to be drawn onto the current canvas.
     */
    private void drawEdge(Edge edge) {
        Coordinate start = edge.getStartNode().getCoordinate();
        Coordinate end = edge.getEndNode().getCoordinate();
        Coordinate tempStart = getCoordinateAdjustedForCentreNode(start);
        Coordinate tempEnd = getCoordinateAdjustedForCentreNode(end);
        graphicsContext.strokeLine(tempStart.getX(), tempStart.getY(), tempEnd.getX(), tempEnd.getY());
    }

    /**
     * Draws a node onto the current canvas.
     * @param node The node to be drawn on the current canvas.
     */
    private void drawNode(Node node) {
        // Oval format : gc.fillOval(x, y, w, h)
        graphicsContext.fillOval(node.getX(), node.getY(), NODE_RADIUS * 2, NODE_RADIUS * 2); // Main node
        graphicsContext.strokeOval(node.getX(), node.getY(), NODE_RADIUS * 2, NODE_RADIUS * 2); // Node outline
    }

    /**
     * Returns a new coordinate with the x and y values increased by a node radius.
     * @param cIn The coordinate to adjust.
     */
    private static Coordinate getCoordinateAdjustedForCentreNode(Coordinate cIn) {
        return new Coordinate(cIn.getX() + NODE_RADIUS, cIn.getY() + NODE_RADIUS);

    }

    /**
     * Returns the value of the @code{drawNodeIDs} attribute.
     * @return drawNodeIDs The value of the @code{drawNodeIDs} attribute.
     */
    public boolean areDrawNodeIDs() {
        return drawNodeIDs;
    }

    /**
     * Sets the @code{drawNodeIDs} attribute to a new value.
     * @param drawNodeIDs The new value to assign to the @code{drawNodeIDs} attribute.
     */
    public void setDrawNodeIDs(boolean drawNodeIDs) {
        this.drawNodeIDs = drawNodeIDs;
    }

    /**
     * Sets the @code{stage} attribute to a new value.
     * @param stage The new value to assign the @code{stage} attribute.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
