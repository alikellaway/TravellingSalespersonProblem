package com.alike.graphical;

import com.alike.customexceptions.NonSquareCanvasException;
import com.alike.solvers.HilbertFractalCurveSolver;
import com.alike.graphsystem.Coordinate;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static com.alike.solution_helpers.RepeatedFunctions.isPowerOfTwo;

/**
 * Animates a Hilbert fractal curve generated inside a @code{HilbertFractalCurveSolver} onto a java fx canvas.
 * Class extends AnimationTimer so the drawing of the curve can be animated out rather than just printed.
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
     * The number of coordinates drawn on the line each loop of the animator (smaller is slower).
     */
    private int drawStep = 512; // Chose this value randomly.

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

    private int period = 0;

    /**
     * Draws the hilbert curve in hfcs onto the canvas. This works by drawing a little more of the line on each time
     * it is called rather than redrawing the whole line (so it is faster).
     */
    private void draw() {
        // Draw the lines from the graph that are after our progress counter but before the draw limit
        for (int i = 1 + progressCounter; i < drawLimit; i++) { // Start at one so we can draw backwards.
            // Design the line - vary the color with the path completion
            int hue = map(period*i, path.length);
//            gc.setStroke(Color.hsb(hue, 1, 1, 2.0/hfcs.getOrder()));
//            gc.setStroke(Color.grayRgb(hue, 0.5D));
            gc.setStroke(Color.rgb(255,0,0, 1));
            try { // Draw the line
                gc.strokeLine(path[i].getX(), path[i].getY(), path[i - 1].getX(), path[i - 1].getY());
            } catch (IndexOutOfBoundsException ignored) {
            }
            progressCounter++;
        }
        /* Advance the draw limit further, so next frame can draw more. */
        drawLimit += drawStep;
        if (drawLimit > path.length) {
            drawLimit = path.length;
        }
//        period++;
//        period = (int) Math.pow(period, period);
    }

    /**
     * Maps a value to a fraction of 360 to find a HSB colour (we assume the minimum value i could be is 0).
     * @param coordinateIndex The index of the part coordinate we are colouring in the path.
     * @param maxIndex The length of the path.
     * @return float The value as a color between 0 and 360.
     */
    private int map(float coordinateIndex, float maxIndex) {
        return Math.abs(((int) (255.0 * (coordinateIndex/maxIndex))) % 255);
//        return (int) (i/iMax) * 360; // Note that when using the hue value in the hsb method, it will loop round to red
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
        double h = canvas.getHeight() - GraphAnimator.NODE_RADIUS*2;
        double w = canvas.getWidth() - GraphAnimator.NODE_RADIUS*2;
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

}
