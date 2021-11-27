package com.alike.solutions;

import com.alike.Main;
import com.alike.customexceptions.EdgeSuperimpositionException;
import com.alike.customexceptions.NodeMissedException;
import com.alike.customexceptions.NonSquareCanvasException;
import com.alike.tspgraphsystem.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Class used to animate a Hilbert fractal curve onto a java fx canvas.
 * @author alike
 */
public class HilbertFractalCurveSolver {

    /**
     * The graph which we are animating.
     */
    private TSPGraph graph;

    /**
     * The order of Hilbert curve to draw (how many iterations to animate).
     */
    public static int order = 8;

    private static final int ORDER_LIMIT = 14; // equates to roughly 1E59W points plotted in the line

    /**
     * The number of sectors that the fractal will be broken into (e.g. quadrants at order 2).
     */
    private final int N = (int) Math.pow(2, order);

    /**
     * The total number of points that will be drawn.
     */
    public final int total = N * N;


    /**
     * The series of coordinates that is used to draw the curve.
     */
    private static ArrayList<Coordinate> cornerCoordinates;

    /**
     * All the coordinates the path is travelling through.
     */
    private static ArrayList<Coordinate> curveCoordinates = new ArrayList<>();

    /**
     * Used to construct a new @code{HilbertFractalCurveAnimator} object.
     * @param graph The graph which we are animating.
     * @throws NonSquareCanvasException Thrown if the canvas is not a square and of side length a power of two.
     */
    public HilbertFractalCurveSolver(TSPGraph graph) throws NonSquareCanvasException {
        cornerCoordinates = new ArrayList<>();
        curveCoordinates = new ArrayList<>();

        // Check that we are running this solver in a square context with a side length or a n^2
        int w = Main.COORDINATE_MAX_WIDTH;
        int h = Main.COORDINATE_MAX_HEIGHT;
        if (h != w || (!isPowerOfTwo(h)) ) { // We don't need to check both for power of two since they should be equal
            throw new NonSquareCanvasException("Canvas must be a square of side lengths of power of 2, the received " +
                    "canvas was of dimensions (w,h): (" + w + ", " + h);
        }
        // Set our graph
        setGraph(graph);

        // Calculate the hilbert curve and store it as a sequence of points
        for (int i = 0; i < total; i++) {
            cornerCoordinates.add(hilbert(i));
            float len = (float) Main.WINDOW_MAX_WIDTH / N;

            cornerCoordinates.get(i).mult(len);
            cornerCoordinates.get(i).add(len/2, len/2);
        }
        /*
             Need to get all the points that are covered by the lines that join those points since cornerCoordinates
             only contains the points on the corners of the hilbert curve currently.
         */
        for (int pathIdx = 0; pathIdx < cornerCoordinates.size(); pathIdx++) {
            // Get our two coordinates to find the ones between.
            Coordinate thisCo = cornerCoordinates.get(pathIdx);
            curveCoordinates.add(thisCo);
            try {
                Coordinate nextCo = cornerCoordinates.get(pathIdx + 1);
                // Get the coordinates the line between them will go through.
                if (thisCo.getVectorTo(nextCo).isHorizontal()) { // The gaps are always horizontal or vertical
                    for (int i = thisCo.getX() + 1; i < nextCo.getX(); i++) {
                        curveCoordinates.add(new Coordinate(i, thisCo.getY()));
                    }
                } else { // Was a vertical line
                    for (int i = thisCo.getY() + 1; i < nextCo.getY(); i++) {
                        curveCoordinates.add(new Coordinate(i, thisCo.getX()));
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                // When we hit the end, do nothing.
            }
        }
        // Update progress in console
        System.out.println("Order " + order + ", Coordinates reached: " + curveCoordinates.size());
    }

    public Pair<TSPGraph, Double> runSolution(int delayPerStep) throws EdgeSuperimpositionException, NodeMissedException, InterruptedException, NonSquareCanvasException {
        try {
            constructRoute(delayPerStep);
        } catch (NodeMissedException e) {
            order++;
            if (order == 12) { // Causes memory error
                System.out.println("Re");
                return new Pair<>(graph, -1.0); // Use -1 to show it was a failure.
            }
            // Flush route to save RAM
            new HilbertFractalCurveSolver(graph).runSolution(delayPerStep);
        }
        return new Pair<>(graph, graph.getEdgeContainer().calculateCurrentRouteLength());
    }

    public TSPEdgeContainer constructRoute(int delayPerStep)
            throws EdgeSuperimpositionException, NodeMissedException, InterruptedException {
        ArrayList<TSPNode> nodesOrdered = getNodesOrdered();
        TSPEdgeContainer container = new TSPEdgeContainer();
        graph.setEdgeContainer(container); // We do this here so we can see the path as its being constructed
        System.out.println("here");
        for (int i = 0; i < nodesOrdered.size(); i++) {
            System.out.println();
            // Create the edge and add it
            container.add(new TSPEdge(nodesOrdered.get(i), nodesOrdered.get((i + 1) % nodesOrdered.size())));
            Thread.sleep(delayPerStep);
        }
        return container;
    }

    public ArrayList<TSPNode> getNodesOrdered() throws NodeMissedException {
        // Find which nodes we want and put them in order according to the graph.
        // We copy the node array here so that we can remove the node from the list once weve found it.
        ArrayList<TSPNode> nodes = new ArrayList<>(graph.getNodeContainer().getNodeSet());
        ArrayList<TSPNode> nodesOrdered = new ArrayList<>();

        for (Coordinate c : curveCoordinates) {
            for (int i = 0; i < nodes.size(); i++) {
                TSPNode n = nodes.get(i);
                if (n.getCoordinate().equals(c)) {
                    nodesOrdered.add(n);
                    nodes.remove(i); // Removing them as we go will speed it up as we advance
                }
            }
        }
//        System.out.println(nodesOrdered.size() + ":" + graph.getNumNodes());

        if (nodesOrdered.size() != graph.getNumNodes()) {
            StringBuilder sb = new StringBuilder("Node(s) missed: ");
            for (TSPNode n : nodes) {
                sb.append(n.toString()).append(", ");
            }
            throw new NodeMissedException(sb.toString(), nodes.size());
        }

        return nodesOrdered;
    }

    /**
     * Used to return the coordinate of a point i on the coordinate.
     * @param i The i th step on a cornerCoordinates using the hilbert curve as a route.
     * @return c The coordinates of the i th step on the cornerCoordinates.
     */
    private Coordinate hilbert(int i) {
        Coordinate[] points = { // This is the order 1
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
     * Sets the @code{graph} attribute to a new value.
     * @param graph The new value to become the @code{graph} attribute.
     */
    public void setGraph(TSPGraph graph) {
        this.graph = graph;
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
     * Returns the value of the @code{cornerCoordinates} attribute.
     * @return @code{cornerCoordinates} The value of the @code{cornerCoordinates} attribute.
     */
    public ArrayList<Coordinate> getCornerCoordinates() {
        return cornerCoordinates;
    }


}

