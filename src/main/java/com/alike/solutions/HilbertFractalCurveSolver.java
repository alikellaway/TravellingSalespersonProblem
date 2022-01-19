package com.alike.solutions;

import com.alike.Main;
import com.alike.customexceptions.*;
import com.alike.solution_helpers.RepeatedFunctions;
import com.alike.solution_helpers.TestResult;
import com.alike.tspgraphsystem.*;
import java.util.ArrayList;

import static com.alike.solution_helpers.RepeatedFunctions.isPowerOfTwo;

/**
 * Class constructs hilbert fractal curves and then uses the path to solve a TSPGraph route.
 * @author alike
 */
public class HilbertFractalCurveSolver implements Solver {
    /**
     * The graph which we are solving.
     */
    private TSPGraph graph;

    /**
     * The order of Hilbert curve to construct (how many iterations to do).
     */
    public static int order = 11;

    /**
     * The number of sectors that the fractal will be broken into (e.g. quadrants at order 2).
     */
    private final int N = (int) Math.pow(2, order);

    /**
     * The total number of points that will be drawn.
     */
    public final int total = N * N;

    /**
     * The series of coordinates that is used to draw the curve. It's the corners since we can use them to stroke lines
     * on a canvas.
     */
    private static ArrayList<Coordinate> cornerCoordinates;

    /**
     * All the coordinates the curve travels through (not just the corners).
     */
    private static ArrayList<Coordinate> curveCoordinates = new ArrayList<>();

    /**
     * Used to construct a new @code{HilbertFractalCurveSolver} object.
     * @param graph The graph which we are solving.
     * @throws NonSquareCanvasException Thrown if the canvas is not a square and of side length a power of two.
     */
    public HilbertFractalCurveSolver(TSPGraph graph) throws NonSquareCanvasException {
        RepeatedFunctions.validateGraph(graph);
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

        // Calculate the hilbert curve corners and store them as a sequence of Coordinates
        for (int i = 0; i < total; i++) {
            cornerCoordinates.add(getHilbertCorner(i));
            float len = (float) Main.WINDOW_MAX_WIDTH / N;
            cornerCoordinates.get(i).mult(len);
            cornerCoordinates.get(i).add(len/2, len/2);
        }
        /* Need to get all the points that are covered by the lines that will join the corners since cornerCoordinates
           only contains the points on the corners of the hilbert curve currently. */
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
        // Update progress in console - NB: at this point we have only a hilbert path around a coordinate space
        System.out.println("Order " + order + ", Coordinates reached: " + curveCoordinates.size());
    }

    /**
     * Used to instruct @code{this} to find a route around the @code{graph} attribute.
     * @param delayPerStep Time to wait between adding edges to the graphs edge container (so we can see it drawn).
     * @return A pair containing the value of the @code{graph} attribute and a double - the length of its route.
     */
    public TestResult runSolution(int delayPerStep) {
        try {// Try to construct the route.
            constructRoute(delayPerStep);
        } catch (EdgeSuperimpositionException | InterruptedException e) {
            // Construct route method through some error.
            e.printStackTrace();
        } catch (NodeMissedException | EdgeToSelfException e) {// On route failure, try again with higher order.
            order++;
            if (order == 12) { // Causes memory error
                // While this is an odd way of writing it, it allows us to keep the solver implementing Solver.
                try {
                    throw new FractalDensityException("Attempted to create Hilbert of order 12 which causes a memory" +
                            " exception.");
                } catch (FractalDensityException ex) {
                    ex.printStackTrace();
                }
            }
            // Start a new solution with a higher order.
            try {
                new HilbertFractalCurveSolver(graph).runSolution(delayPerStep); // Try again at a higher order
            } catch (NonSquareCanvasException ex) {
                ex.printStackTrace();
            }
        }
        return new TestResult(graph, graph.getEdgeContainer().getTotalLength());
    }

    /**
     * Constructs an edge container for the @code{graph} using the list output by @code{getNodesOrdered}.
     * @param delayPerStep The time wait after each edge addition (so we can see it draw if we want).
     * @throws EdgeSuperimpositionException Thrown if an attempt is made to create an edge that already exists.
     * @throws NodeMissedException Thrown if a node is missed using the current hilbert curve as the map.
     * @throws InterruptedException Thrown if the thread is interrupted.
     */
    public void constructRoute(int delayPerStep)
            throws EdgeSuperimpositionException, NodeMissedException, InterruptedException, EdgeToSelfException {
        ArrayList<TSPNode> nodesOrdered = getNodesOrdered();
        TSPEdgeContainer container = new TSPEdgeContainer();
        graph.setEdgeContainer(container); // We do this here, so we can see the path as its being constructed
        for (int i = 0; i < nodesOrdered.size(); i++) {
            System.out.println();
            // Create the edge and add it
            container.add(new TSPEdge(nodesOrdered.get(i), nodesOrdered.get((i + 1) % nodesOrdered.size())));
            RepeatedFunctions.sleep(delayPerStep);
        }
    }

    /**
     * Returns the nodes in an order using the current hilbert curve to map the nodes into an order.
     * @return @code{nodesOrdered} A list containing the nodes in the order the hilbert curve hits them.
     * @throws NodeMissedException Thrown if the current hilbert curve misses nodes when used as the map.
     */
    public ArrayList<TSPNode> getNodesOrdered() throws NodeMissedException {
        // Find which nodes we want and put them in order according to the graph.
        // We copy the node array here so that we can remove the node from the list once we've found it.
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
        // Check to see if we missed nodes - if so throw an exception.
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
        RepeatedFunctions.validateGraph(graph);
        this.graph = graph;
    }

    /**
     * Returns the value of the @code{cornerCoordinates} attribute.
     * @return @code{cornerCoordinates} The value of the @code{cornerCoordinates} attribute.
     */
    public ArrayList<Coordinate> getCornerCoordinates() {
        return cornerCoordinates;
    }
}

