package com.alike.graphical;

import com.alike.customexceptions.NonSquareCanvasException;
import com.alike.tspgraphsystem.Coordinate;
import com.alike.tspgraphsystem.TSPGraph;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HilbertFractalCurveAnimator extends AnimationTimer {

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
    private GraphicsContext gc; // graphics context

    /**
     * The color to draw the hilbert curve lines.
     */
    private static final Color LINE_COLOR = Color.rgb(255, 255, 255);

    private int order = 8;
    private int n = (int) Math.pow(2, order);
    int total = n * n;

    int progressCounter = 0;

    private Coordinate[] path = new Coordinate[total];

    public HilbertFractalCurveAnimator(Canvas canvas, TSPGraph graph) throws NonSquareCanvasException {
        setCanvas(canvas);
        setGraph(graph);
        setGc(canvas.getGraphicsContext2D());
        for (int i = 0; i < total; i++) {
            path[i] = hilbert(i);
            float len = (float) canvas.getWidth() / n;
            path[i].mult(len);
            path[i].add(len/2, len/2);
        }
    }

    private void draw() {
        // Draw the lines
//        gc.setStroke(LINE_COLOR);
        gc.beginPath();
        gc.moveTo(path[0].getX(), path[0].getY());
        for (int i = 0; i < progressCounter; i++) {
            float hue = map(i, path.length,360);
            gc.setStroke(Color.hsb(hue, 1, 1));
            gc.lineTo(path[i].getX(), path[i].getY());
        }
        gc.stroke();

        // Draw the indexes
//        gc.setFill(LINE_COLOR);
//        for (int i = 0; i < path.length; i++) {
//            gc.fillText(Integer.toString(i), path[i].getX() + 5, path[i].getY() - 5);
//        }
        progressCounter += 10;
        if (progressCounter >= path.length) {
            progressCounter = 0;
            gc.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        }
    }

    @Override
    public void handle(long l) {
        draw();
    }

    private Coordinate hilbert(int i) {
        Coordinate[] points = {
                new Coordinate(0,0),
                new Coordinate(0,1),
                new Coordinate(1,1),
                new Coordinate(1,0)};
        int index = i & 3;
        Coordinate c = points[index];

        for (int j = 1; j < order; j++) {
            int len = (int) Math.pow(2, j);
            i = i >>> 2;
            index = i & 3;
            if (index == 0) {
                float temp = c.getX();
                c.setX(c.getY());
                c.setY((int) temp);
            } else if (index == 1) {
                c.setY(c.getY() + len);
            } else if (index == 2) {
                c.setX(c.getX() + len);
                c.setY(c.getY() + len);
            } else {
                float temp = len - 1 - c.getX();
                c.setX(len - 1 - c.getY());
                c.setY((int) temp);
                c.setX(c.getX() + len);
            }
        }





        return c;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) throws NonSquareCanvasException {
        double h = canvas.getHeight() - TSPGraphAnimator.NODE_RADIUS*2;
        double w = canvas.getWidth() - TSPGraphAnimator.NODE_RADIUS*2;
        if (h != w || (!isPowerOfTwo(h)) ) { // We don't need to check both for power of two since they should be equal
            throw new NonSquareCanvasException("Canvas must be a square of side lengths of power of 2, the received " +
                    "canvas was of dimensions (w,h): (" + canvas.getWidth() + ", " + canvas.getHeight() );
        }
        this.canvas = canvas;
    }

    public TSPGraph getGraph() {
        return graph;
    }

    public void setGraph(TSPGraph graph) {
        this.graph = graph;
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    private boolean isPowerOfTwo(double num) {
        while (num > 1) {
            num = num / 2;
        }
        return num == 1.00;
    }

    private float map(int i, int iMax, int nMax) {
        return (i/nMax) * iMax;
    }
}
