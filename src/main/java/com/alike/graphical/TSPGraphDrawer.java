package com.alike.graphical;

import com.alike.tspgraphsystem.TSPGraph;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class TSPGraphDrawer extends AnimationTimer {

    private Canvas canvas;
    private TSPGraph graph;
    private GraphicsContext graphicsContext;
    private int editsPerRedraw;

    public TSPGraphDrawer(TSPGraph graph, Canvas canvas, int editsPerRedraw) {
        setGraph(graph);
        setCanvas(canvas);
        setGraphicsContext(canvas.getGraphicsContext2D());
        setEditsPerRedraw(editsPerRedraw);
    }

    @Override
    public void handle(long l) {
        if (graph.getEdgeContainer().getEditCount() % editsPerRedraw == 0) {
            clearCanvas(graphicsContext);
            graph.drawGraph(graphicsContext);
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
}
