package com.alike.solvers;

import com.alike.Main;
import com.alike.customexceptions.*;
import com.alike.solution_helpers.RepeatedFunctions;
import com.alike.solvertestsuite.Fail;
import com.alike.solvertestsuite.Solution;
import com.alike.solvertestsuite.SolverOutput;
import com.alike.staticgraphsystem.*;
import java.util.ArrayList;

import static com.alike.solution_helpers.RepeatedFunctions.isPowerOfTwo;

/**
 * Class constructs a pseudo hilbert fractal curve and then overlays the curve onto a TSP to find an order of traversal.
 * @author alike
 */
public class HilbertFractalCurveSolver implements Solver {
    /*
        NTS: The curve should be able to perfectly cover all 1047576 points in a 1024x1024 canvas using only
        the corners in the order 10 curve. Likewise, it should be able to cover all 262144 points with an order 9
     */
    /**
     * The graph which we are solving.
     */
    private StaticGraph graph;

    /**
     * The order of Hilbert curve to construct (how many iterations to do).
     */
    public static int order = 8;

    /**
     * The number of sectors that the fractal will be broken into (e.g. quadrants at order 2).
     */
    private final int N = (int) Math.pow(2, order);

    /**
     * The number of points that will be in a pseudo hilbert curve of order @code{order}.
     */
    public final int numCorners = N * N; // Where n is 2^order.

    /**
     * The series of corners in the curve.
     */
    private Coordinate[] curveCornerCoordinates; // Don't initialise yet, as its usually very large.

    /**
     * All the coordinates the curve travels through (not just the corners).
     */
    private static ArrayList<Coordinate> curveCoordinates = new ArrayList<>();

//    /**
//     * Used to pass the start time to the end solution (if we needed to recursively solve).
//     */
//    private static Long solutionStartTime = null;

    /**
     * Used to construct a new @code{HilbertFractalCurveSolver} object.
     * @param graph The graph which we are solving.
     */
    public HilbertFractalCurveSolver(StaticGraph graph) {
        validateCoordinateSpaceIsSquareOfSideLenPowerOf2();
        // Set our graph
        setGraph(graph);
        // Construct the curve.
        constructHilbertFractalCurve();
        // Check if curve has duplicates.
        System.out.println(RepeatedFunctions.hasDuplicates(getCornerCoordinates()));
        // Find the minimum and maximum x and y values for the coordinates in the list.
        int xM = Integer.MIN_VALUE;
        int yM = Integer.MIN_VALUE;
        int xU = Integer.MAX_VALUE;
        int yU = Integer.MAX_VALUE;
        for (Coordinate c : curveCornerCoordinates) {
            int x = c.getX();
            int y = c.getY();
            if (x > xM) xM = x;
            if (x < xU) xU = x;
            if (y > yM) yM = y;
            if (y < yU) yU = y;
        }
        System.out.println(xM + " " + xU + " " + yM + " " + yU);
    }

    /**
     * Constructs new @code{HilbertFractalCurveSolver} object without a graph, so we can set one at a later time
     * if we choose.
     */
    public HilbertFractalCurveSolver() {
        validateCoordinateSpaceIsSquareOfSideLenPowerOf2();
        constructHilbertFractalCurve();
    }

    /**
     * Used to instruct @code{this} to find a route around the @code{graph} attribute.
     * @param delayPerStep Time to wait between adding edges to the graphs edge container (so we can see it drawn).
     * @return A pair containing the value of the @code{graph} attribute and a double - the length of its route.
     */
    public SolverOutput runSolution(int delayPerStep) {
        try { // Try to construct the route.
            Runtime.getRuntime().gc(); // Reclaim as much memory as possible.
            long startTime = System.nanoTime();
            constructRoute(delayPerStep);
            long finishTime = System.nanoTime();
            return new Solution(graph, graph.getEdgeContainer().getTotalLength(), finishTime - startTime);
        } /* catch (HilbertCurveUnconstructedException e) { // Tried to get a route without a constructed curve.
            e.printStackTrace();
        } catch (NodeMissedException e) {// On route failure, try again with higher order.
                order++;
                if (order == 11) { // Causes memory error

            // While this is an odd way of writing it, it allows us to keep the solver implementing Solver.
                    try {
                        throw new FractalDensityException("Attempted to create Hilbert of order 12 which causes a memory" +
                                " exception.");
                    } catch (FractalDensityException ex) {
            return new Fail(e, graph);
                    }
                }
                // Start a new solution with a higher order.
                new HilbertFractalCurveSolver(graph).runSolution(delayPerStep); // Try again at a higher order
        }*/catch (Exception e) {
            return new Fail(e, graph);
        } catch (Error e) {
            return new Fail(e, graph);
        }
    }

    /**
     * Called to construct the hilbert curve and store it as a sequence of coordinates.
     */
    private void constructHilbertFractalCurve() {
        // Calculate the hilbert curve corners and store them as a sequence of Coordinates
        calculateHilbertCurveCornerCoordinates();
        /* Need to get all the points that are covered by the lines that will join the corners since
           curveCornerCoordinates only contains the points on the corners of the hilbert curve currently. */
//        System.out.println(curveCornerCoordinates.length);
//        calculateHilbertCurveLineCoordinates();
        // Update progress in console - NB: at this point we have only a hilbert path around a coordinate space
        // System.out.println("Order " + order + ", Coordinates reached: " + curveCoordinates.size());
    }

    /**
     * Constructs an edge container for the @code{graph} using the list output by @code{getNodesOrdered}.
     * @param delayPerStep The time wait after each edge addition (so we can see it draw if we want).
     * @throws HilbertCurveUnconstructedException Thrown if this is run but no hilbert map has been constructed.
     * @throws NodeMissedException Thrown if a node is missed using the current hilbert curve as the map.
     */
    public void constructRoute(int delayPerStep) throws NodeMissedException, HilbertCurveUnconstructedException {
        try {
            ArrayList<Node> nodesOrdered = getNodesOrdered();
            EdgeContainer container = new EdgeContainer();
            graph.setEdgeContainer(container); // We do this here, so we can see the path as its being constructed
            for (int i = 0; i < nodesOrdered.size(); i++) {
                // Create the edge and add it
                container.add(new Edge(nodesOrdered.get(i), nodesOrdered.get((i + 1) % nodesOrdered.size())));
                RepeatedFunctions.sleep(delayPerStep);
            }
        } catch (EdgeSuperimpositionException | EdgeToSelfException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the nodes in an order using the current hilbert curve to map the nodes into an order.
     * @return nodesOrdered A list containing the nodes in the order the hilbert curve hits them.
     * @throws NodeMissedException Thrown if the current hilbert curve misses nodes when used as the map.
     */
    public ArrayList<Node> getNodesOrdered() throws NodeMissedException, HilbertCurveUnconstructedException {
        if (curveCornerCoordinates == null || curveCornerCoordinates.length == 0) {
            throw new HilbertCurveUnconstructedException("No curve constructed.");
        }
        // Find which nodes we want and put them in order according to the graph.
        // We copy the node array here so that we can remove the node from the list once we've found it.
        ArrayList<Node> nodes = new ArrayList<>(graph.getNodeContainer().getNodeSet());
        ArrayList<Node> nodesOrdered = new ArrayList<>();
//        for (Coordinate c : curveCornerCoordinates) {
//            for (int i = 0; i < nodes.size(); i++) {
//                Node n = nodes.get(i);
//                if (n.getCoordinate().equals(c)) {
//                    nodesOrdered.add(n);
//                    nodes.remove(i); // Removing them as we go will speed it up as we advance
//                }
//            }
//        }
        // Check to see if we missed nodes - if so throw an exception.
        if (nodesOrdered.size() != graph.getNumNodes()) {
            StringBuilder sb = new StringBuilder("Node(s) missed: ");
            for (Node n : nodes) {
                sb.append(n.toString()).append(", ");
            }
            sb.delete(sb.length()-2, sb.length()-1); // Remove the last comma
            throw new NodeMissedException(sb.toString());
        }
        nodesOrdered.trimToSize();
        return nodesOrdered;
    }

    /**
     * Used to return the coordinates of an index i on the curve.
     * Algorithm found: http://blog.marcinchwedczuk.pl/iterative-algorithm-for-drawing-hilbert-curve
     * @param i The i th corner we find the coordinates of.
     * @return c The coordinates of the i th step on the curve.
     */
    private Coordinate getHilbertCorner(int i) {
        // This is order 1 (the simplest case)
        Coordinate[] points = {new Coordinate(0,0),
                               new Coordinate(0,1),
                               new Coordinate(1,1),
                               new Coordinate(1,0)};
        // Mask by 3 to find which quadrant the coordinate is in the order above.
        int index = i & 3; // Works by only caring about the last two bits of the number.
        Coordinate c = points[index];
        // As we go up, we need to continue to check the bits and shift across.
        for (int j = 1; j < order; j++) {
            int len = (int) Math.pow(2, j); // The relative length of this order curve
            i = i >>> 2; // Shift over and get the next two bits.
            index = i & 3; // Mask by three again. (Think: which quadrant are we in this order?)
            // 0 and 2 remain the same, but 1 and 3 are swapped in the rotation
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
        System.out.println(c);
        return c;
    }

    /**
     * Sets the @code{graph} attribute to a new value.
     * @param graph The new value to become the @code{graph} attribute.
     */
    public void setGraph(StaticGraph graph) {
        RepeatedFunctions.validateGraph(graph);
        this.graph = graph;
    }

    /**
     * Returns the value of the @code{curveCornerCoordinates} attribute.
     * @return curveCornerCoordinates The value of the @code{curveCornerCoordinates} attribute.
     */
    public Coordinate[] getCornerCoordinates() {
        return curveCornerCoordinates;
    }

    /**
     * Used to verify that the coordinate space we are operating in is a square and that it's side length is a power
     * of two.
     */
    private void validateCoordinateSpaceIsSquareOfSideLenPowerOf2() {
        // Check that we are running this solver in a square context with a side length or a n^2
        int w = Main.COORDINATE_MAX_WIDTH;
        int h = Main.COORDINATE_MAX_HEIGHT;
        if (h != w || (!isPowerOfTwo(h)) ) { // We don't need to check both for power of two since they should be equal
            try {
                throw new NonSquareCanvasException("Canvas must be a square of side lengths of power of 2, the received " +
                        "canvas was of dimensions (w,h): (" + w + ", " + h);
            } catch (NonSquareCanvasException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Used to calculate the coordinates of every corner on our hilbert curve.
     */
    private void calculateHilbertCurveCornerCoordinates() {
        curveCornerCoordinates = new Coordinate[numCorners]; // Create space for them.
        for (int i = 0; i < numCorners; i++) {
            curveCornerCoordinates[i] = getHilbertCorner(i); // Get & insert coordinates for each corner
            // Change it from 0, 1 format to fit out window.
            float len = (float) Main.WINDOW_MAX_WIDTH / N;
            curveCornerCoordinates[i].mult(len);
            // Moves it to the middle - good for animator
//            curveCornerCoordinates[i].add(len/2, len/2);
        }
    }

    /**
     * Uses the generated corners to calculate all the corners touched by the line between each corner.
     */
    private void calculateHilbertCurveLineCoordinates() {
        curveCoordinates = new ArrayList<>();
        for (int pathIdx = 0; pathIdx < numCorners; pathIdx++) {
            // Get our two coordinates to find the ones between.
            Coordinate thisCo = curveCornerCoordinates[pathIdx];
            curveCoordinates.add(thisCo);
            try {
                Coordinate nextCo = curveCornerCoordinates[pathIdx + 1];
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
    }
}

