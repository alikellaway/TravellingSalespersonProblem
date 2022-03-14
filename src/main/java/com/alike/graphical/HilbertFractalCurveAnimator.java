package com.alike.graphical;

import com.alike.Main;
import com.alike.customexceptions.NonSquareCanvasException;
import com.alike.solvers.HilbertFractalCurveSolver;
import com.alike.staticgraphsystem.Coordinate;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import static com.alike.solution_helpers.RepeatedFunctions.isPowerOfTwo;

/**
 * Animates a Hilbert fractal curve generated inside a @code{HilbertFractalCurveSolver} onto a java fx canvas.
 * @author alike
 */
public class HilbertFractalCurveAnimator extends AnimationTimer {
    /**
     * The graphics context of the canvas object.
     */
    private GraphicsContext gc; // graphics context

    /**
     * A counter used to record how far through the drawing process we are so we don't re-draw the whole line each
     * cycle.
     */
    private int progressCounter = 0;

    /**
     * A counter used to limit how far the current draw cycle will draw into the hilbert curve past the progressCounter.
     */
    private int drawLimit = 0;

    /**
     * The series of coordinates that is our path.
     */
    private Coordinate[] path;

    /**
     * A reference to the @code{HilbertFractalCurveSolver} object we will be animating.
     */
    private HilbertFractalCurveSolver hfcs;

    /**
     * Used to check if the order of the graph has changed while we are drawing (we need to reset the progress counter
     * to 0 if it has)
     */
    private int orderWeAreDrawing;

    /**
     * This number will cap the maximum order that can be drawn by the animator (as a solver may try to push it further)
     * My computer struggles to draw any  higher than 9, however, this cap may vary on your computer.
     */
    private final int order_limit = 9;

    /**
     * Used to construct a new @code{HilbertFractalCurveAnimator} object.
     * @param canvas The canvas on which to animate.
     * @param hfcs The Hilbert solver containing the path we want to draw.
     * @throws NonSquareCanvasException Thrown if the canvas is not a square and of side length a power of two.
     */
    public HilbertFractalCurveAnimator(Canvas canvas, HilbertFractalCurveSolver hfcs) throws NonSquareCanvasException {
        checkCanvas(canvas); // Check the canvas is valid
        setGc(canvas.getGraphicsContext2D());
        setHfcs(hfcs);
        setOrderWeAreDrawing(HilbertFractalCurveSolver.order);
        setPath(hfcs.getCornerCoordinates());
    }

    /**
     * The code that is run each frame.
     * @param l The current frames time stamp.
     */
    @Override
    public void handle(long l) {
        draw();
    }

    /**
     * Draws the hilbert curve in hfcs onto the canvas. This works by drawing a little more of the line on each time
     * it is called rather than redrawing the whole line (so it is faster).
     */
    private void draw() {
        /* We must check if the hfcs hasn't changed its order during our drawing process which may happen as its
        run solution method is recursive. My PC struggles to draw any order above 9 so that's the limit. */
        if (getOrderWeAreDrawing() != HilbertFractalCurveSolver.order && HilbertFractalCurveSolver.order <= order_limit) {
            progressCounter = 0;
            path = hfcs.getCornerCoordinates();
            gc.clearRect(0,0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        }
        // Draw the lines from the graph that are after our progress counter but before the draw limit
        for (int i = 1 + progressCounter; i < drawLimit; i++) {
            // Design the line - vary the color with the path completion
            float hue = map(HilbertFractalCurveSolver.order * i, path.length);
            gc.setStroke(Color.hsb(hue, 1, 1, 2.0/HilbertFractalCurveSolver.order));
            try { // Draw the line
                gc.strokeLine(path[i].getX(), path[i].getY(), path[i - 1].getX(), path[i - 1].getY());
            } catch (IndexOutOfBoundsException ignored) {
            }
            progressCounter++;
        }
        /* Draw limit describes what index we go up to in the path, here we increase it so we can go further in the
           next cycle. It is important it's the side length so the line is completed. */
        drawLimit += Main.COORDINATE_MAX_WIDTH;
        if (drawLimit > path.length) {
            drawLimit = path.length;
        }
    }

    /**
     * Maps a value to a fraction of 360 to find a HSB colour (we assume the minimum value i could be is 0).
     * @param i The input value.
     * @param iMax The maximum value the input value could be.
     * @return float The value as a color between 0 and 360.
     */
    private float map(float i, float iMax) {
        return (i/iMax) * 360; // Note that when using the hue value in the hsb method, it will loop round to red
    }

    /**
     * Sets the value of the @code{path} attribute to a new value.
     * @param newPath The new value to become the @code{path} attribute.
     */
    private void setPath(Coordinate[] newPath) {
        this.path = newPath;
    }

    /**
     * Sets the canvas to a new value and checks for certain constraints.
     * @param canvas The new value to become the @code{canvas} attribute.
     * @throws NonSquareCanvasException Thrown if the parameter canvas is not a square and of side length of power of 2.
     */
    public void checkCanvas(Canvas canvas) throws NonSquareCanvasException {
        double h = canvas.getHeight() - TSPGraphAnimator.NODE_RADIUS*2;
        double w = canvas.getWidth() - TSPGraphAnimator.NODE_RADIUS*2;
        if (h != w || (!isPowerOfTwo(h)) ) { // We don't need to check both for power of two since they should be equal
            throw new NonSquareCanvasException("Canvas must be a square of side lengths of power of 2, the received " +
                    "canvas was of dimensions (w,h): (" + canvas.getWidth() + ", " + canvas.getHeight() );
        }
    }

    /**
     * Sets the @code{gc} attribute to a new value.
     * @param gc The new value to become the @code{gc} attribute.
     */
    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    /**
     * Sets the value of the @code{hfcs} attribute to a new value.
     * @param hfcs The new value to become the @code{hfcs} attribute.
     */
    public void setHfcs(HilbertFractalCurveSolver hfcs) {
        this.hfcs = hfcs;
    }

    /**
     * Sets the @code{orderWeAreDrawing} attribute to a new value.
     * @param orderWeAreDrawing The new value to become the @code{orderWeAreDrawing} attribute.
     */
    public void setOrderWeAreDrawing(int orderWeAreDrawing) {
        this.orderWeAreDrawing = orderWeAreDrawing;
    }

    /**
     * Returns the value of the @code{orderWeAreDrawing} attribute.
     * @return orderWeAreDrawing The value of the @code{orderWeAreDrawing} attribute.
     */
    public int getOrderWeAreDrawing() {
        return this.orderWeAreDrawing;
    }
}
