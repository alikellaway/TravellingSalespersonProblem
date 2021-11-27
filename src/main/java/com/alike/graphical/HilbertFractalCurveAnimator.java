package com.alike.graphical;

import com.alike.Main;
import com.alike.customexceptions.NonSquareCanvasException;
import com.alike.solutions.HilbertFractalCurveSolver;
import com.alike.tspgraphsystem.Coordinate;
import com.alike.tspgraphsystem.TSPGraph;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Class used to animate a Hilbert fractal curve onto a java fx canvas.
 * @author alike
 */
public class HilbertFractalCurveAnimator extends AnimationTimer {


    /**
     * The graphics context of the canvas object.
     */
    private GraphicsContext gc; // graphics context

    /**
     * The color to draw the hilbert curve lines.
     */
    private static final Color LINE_COLOR = Color.rgb(255, 255, 255);


    /**
     * A counter used to record how far through the drawing process we are (higher leads to faster drawing speed).
     */
    private int progressCounter = 0;

    /**
     * A counter used to limit how far the current draw cycle will draw into the hilbert curve.
     */
    private int drawLimit = 0;

    /**
     * The series of coordinates that is our path.
     */
    private ArrayList<Coordinate> path;

    private HilbertFractalCurveSolver hfcs;

    /**
     * The number of lines to draw in one drawing cycle.
     */
//    private final int linesPerStep = total - 1;

    /**
     * Used to check if the order of the graph has changed while we are drawing (we need to reset the progress counter
     * to 0 if it has)
     */
    private int orderWeAreDrawing;

    /**
     * Used to construct a new @code{HilbertFractalCurveAnimator} object.
     * @param canvas The canvas on which to animate.
     * @param hfcs The Hilbert solver containing the path we want to join.
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
     * Used to draw the hilbert curve onto the canvas.
     */
    private void draw() {
        if (getOrderWeAreDrawing() != HilbertFractalCurveSolver.order && HilbertFractalCurveSolver.order < 10) { // The graph we are drawing is not current
            progressCounter = 0;
            path = hfcs.getCornerCoordinates();
            gc.clearRect(0,0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        }
        // Draw the lines
        // gc.setStroke(LINE_COLOR);
        for (int i = 1 + progressCounter; i < drawLimit; i++) {
            float hue = map(HilbertFractalCurveSolver.order * i, path.size()); // Vary the color with the path completion.
            gc.setStroke(Color.hsb(hue, 1, 1, 2.0/HilbertFractalCurveSolver.order));
            try {
                gc.strokeLine(path.get(i).getX(), path.get(i).getY(), path.get(i-1).getX(), path.get(i-1).getY());
            } catch (IndexOutOfBoundsException e) {

            }
            progressCounter++;
        }

        // Draw the indexes (used for debugging etc)
        /* gc.setFill(LINE_COLOR);
        for (int i = 0; i < path.size(); i++) {
            gc.fillText(Integer.toString(i), path[i].getX() + 5, path[i].getY() - 5);
        } */
        drawLimit += 1024; // This is how many lines will be drawn each draw cycle
        if (drawLimit > path.size()) {
            drawLimit = path.size();
        }


            if ((progressCounter + 1) % hfcs.getCornerCoordinates().size() == 0) {
                progressCounter = 0;
            }





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
     * Sets the value of the @code{path} attribute to a new value.
     * @param newPath The new value to become the @code{path} attribute.
     */
    private void setPath(ArrayList<Coordinate> newPath) {
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


    public void setHfcs(HilbertFractalCurveSolver hfcs) {
        this.hfcs = hfcs;
    }


    public void setOrderWeAreDrawing(int orderWeAreDrawing) {
        this.orderWeAreDrawing = orderWeAreDrawing;
    }

    public int getOrderWeAreDrawing() {
        return this.orderWeAreDrawing;
    }
}
