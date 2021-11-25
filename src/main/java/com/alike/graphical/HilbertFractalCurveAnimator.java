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

    /**
     * The order of Hilbert curve to draw (how many iterations to animate).
     */
    private int order = 7;

    /**
     * The number of sectors that the fractal will be broken into (e.g. quadrants at order 2).
     */
    private final int n = (int) Math.pow(2, order);

    /**
     * The total number of points that will be drawn.
     */
    private final int total = n * n;

    /**
     * A counter used to record how far through the drawing process we are (higher leads to faster drawing speed).
     */
    private int progressCounter = 0;

    /**
     * The series of coordinates that is our path.
     */
    private final Coordinate[] path = new Coordinate[total];

    /**
     * The number of lines to draw in one drawing cycle.
     */
//    private final int linesPerStep = total - 1;

    /**
     * Used to construct a new @code{HilbertFractalCurveAnimator} object.
     * @param canvas The canvas on which to animate.
     * @param graph The graph which we are animating.
     * @throws NonSquareCanvasException Thrown if the canvas is not a square and of side length a power of two.
     */
    public HilbertFractalCurveAnimator(Canvas canvas, TSPGraph graph) throws NonSquareCanvasException {
        setCanvas(canvas);
        setGraph(graph);
        setGc(canvas.getGraphicsContext2D());
        // Calculate the hilbert curve
        for (int i = 0; i < total; i++) {
            path[i] = hilbert(i);
            float len = (float) canvas.getWidth() / n;
            path[i].mult(len);
            path[i].add(len/2, len/2);
        }
        System.out.println(path.length);
        System.out.println(canvas.getWidth() * canvas.getHeight());
    }

    /**
     * The code that is run each frame.
     * @param l The length of each frome time.
     */
    @Override
    public void handle(long l) {
        draw();
    }

    /**
     * Used to draw the hilbert curve onto the canvas.
     */
    private void draw() {
        // Draw the lines
//        gc.setStroke(LINE_COLOR);
        for (int i = 1; i < progressCounter; i++) {
            gc.beginPath();
            gc.moveTo(path[i].getX(), path[i].getY());
            float hue = map(i, path.length); // Vary the color with the path completion.
            gc.setStroke(Color.hsb(hue, 1, 1));
            gc.lineTo(path[i - 1].getX(), path[i - 1].getY());
            gc.stroke();
        }
        // Draw the indexes (used for debugging etc)
        /* gc.setFill(LINE_COLOR);
        for (int i = 0; i < path.length; i++) {
            gc.fillText(Integer.toString(i), path[i].getX() + 5, path[i].getY() - 5);
        } */
        progressCounter += 10; // This is how many lines will be drawn each draw cycle
        if (progressCounter >= path.length) {
            progressCounter = 0;
            gc.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        }
    }

    /**
     * Used to return the coordinate of a point i on the coordinate.
     * @param i The i th step on a path using the hilbert curve as a route.
     * @return c The coordinates of the i th step on the path.
     */
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

    /**
     * Sets the canvas to a new value and checks for certain constraints.
     * @param canvas The new value to become the @code{canvas} attribute.
     * @throws NonSquareCanvasException Thrown if the parameter canvas is not a square and of side length of power of 2.
     */
    public void setCanvas(Canvas canvas) throws NonSquareCanvasException {
        double h = canvas.getHeight() - TSPGraphAnimator.NODE_RADIUS*2;
        double w = canvas.getWidth() - TSPGraphAnimator.NODE_RADIUS*2;
        if (h != w || (!isPowerOfTwo(h)) ) { // We don't need to check both for power of two since they should be equal
            throw new NonSquareCanvasException("Canvas must be a square of side lengths of power of 2, the received " +
                    "canvas was of dimensions (w,h): (" + canvas.getWidth() + ", " + canvas.getHeight() );
        }
        this.canvas = canvas;
    }

    /**
     * Sets the @code{graph} attribute to a new value.
     * @param graph The new value to become the @code{graph} attribute.
     */
    public void setGraph(TSPGraph graph) {
        this.graph = graph;
    }

    /**
     * Sets the @code{gc} attribute to a new value.
     * @param gc The new value to become the @code{gc} attribute.
     */
    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    /**
     * Outputs a boolean describing whether the input number was a power of two or not.
     * @param num The number we are checking.
     * @return boolean true if the number is a power of 2, false if not.
     */
    private boolean isPowerOfTwo(double num) {
        while (num > 1) {
            num = num / 2;
        }
        return num == 1.00;
    }

    /**
     * Maps a value to a fraction of 360 to find a HSB colour (we assume the minimum value i could be is 0).
     * @param i The input value.
     * @param iMax The maximum value the input value could be.
     * @return float The value as a color between 0 and 360.
     */
    private float map(float i, float iMax) {
        return (i/iMax) * 360;
    }

    /**
     * Returns the value of the @code{path} attribute.
     * @return @code{path} The value of the @code{path} attribute.
     */
    public Coordinate[] getPath() {
        return this.path;
    }

    /* TODO: move responsibility of getting the path to the solver and then figure out how to get the nodes in order.
        You may need to use some sort of nearest node or something since the number of path points cannot equal the
        number of pixels - closest we can get is order 8 (double check) - the console prints out the number of path
        points and then the number of pixels in the canvas i.e. available to place nodes.
     */
}
