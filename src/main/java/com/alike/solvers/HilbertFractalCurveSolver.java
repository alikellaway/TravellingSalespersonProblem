package com.alike.solvers;

import com.alike.Main;
import com.alike.customexceptions.*;
import com.alike.solution_helpers.RepeatedFunctions;
import com.alike.solvertestsuite.Fail;
import com.alike.solvertestsuite.Solution;
import com.alike.solvertestsuite.SolverOutput;
import com.alike.graphsystem.*;
import com.alike.time.Stopwatch;

import java.util.ArrayList;

import static com.alike.solution_helpers.RepeatedFunctions.isPowerOfTwo;

/**
 * Class constructs a pseudo hilbert fractal curve and then overlays the curve onto a TSP to find an order of traversal.
 * @author alike
 */
public class HilbertFractalCurveSolver implements StaticSolver {
    /**
     * The graph which we are solving.
     */
    private StaticGraph graph;

    /**
     * The order of Hilbert curve to construct (how many iterations to do).
     */
    private int order;

    /**
     * The number of sectors that the fractal will be broken into (e.g. quadrants at order 2).
     */
    private int N = (int) Math.pow(2, order);

    /**
     * The number of points that will be in a pseudo hilbert curve of order @code{order}.
     */
    private int numCorners = (int) Math.pow(N, 2); // Where n is 2^order.

    /**
     * The series of coordinates in the curve.
     */
    private Coordinate[] curveCoordinates; // Don't initialise yet, as its usually very large.

    /**
     * The object in charge of recording time in this class.
     */
    private final Stopwatch stopwatch = new Stopwatch();

    /**
     * The set of coordinates pertaining to the order 1 curve.
     */
    private final Coordinate[] orderOneCurve = {
            new Coordinate(0,0),
            new Coordinate(0,1),
            new Coordinate(1,1),
            new Coordinate(1,0)
    };

    /**
     * Used to construct a new @code{HilbertFractalCurveSolver} object.
     * @param graph The graph which we are solving.
     */
    public HilbertFractalCurveSolver(StaticGraph graph) {
        validateCoordinateSpaceIsSquareOfSideLenPowerOf2();
        // Set our graph
        setGraph(graph);
        // Construct the curve.
        setValues();
        constructHilbertCurve(false);
    }

    /**
     * Constructs new @code{HilbertFractalCurveSolver} object without a graph, so we can set one at a later time
     * if we choose.
     */
    public HilbertFractalCurveSolver() {
        validateCoordinateSpaceIsSquareOfSideLenPowerOf2();
        setValues();
        constructHilbertCurve(false);
    }

    /**
     * Constructs a new @code{HilbertFractalCurveSolver} object without a graph, for the intention of using the object
     * to construct a curve for drawing.
     * @param order The order of the curve to construct.
     */
    public HilbertFractalCurveSolver(int order) {
        validateCoordinateSpaceIsSquareOfSideLenPowerOf2();
        setOrder(order);
        setN((int) Math.pow(2, order));
        setNumCorners((int) Math.pow(getN(), 2));
        constructHilbertCurve(true);
        System.out.println(numCorners);
    }

    /**
     * Used to instruct @code{this} to find a route around the @code{graph} attribute.
     * @param delayPerStep Time to wait between adding edges to the graphs edge container (so we can see it drawn).
     * @return A pair containing the value of the @code{graph} attribute and a double - the length of its route.
     */
    public SolverOutput runSolution(int delayPerStep) {
        try { // Try to construct the route.
            stopwatch.clear();
            stopwatch.start();
            constructRoute(delayPerStep);
            return new Solution(graph, graph.getEdgeContainer().getTotalLength(), stopwatch.getTimeNs());
        } catch (Exception e) {
            return new Fail(e, graph);
        } catch (Error e) {
            return new Fail(e, graph);
        }
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
            graph.getEdgeContainer().clear();
            for (int i = 0; i < nodesOrdered.size(); i++) {
                // Create the edge and add it
                graph.getEdgeContainer().add(new Edge(nodesOrdered.get(i), nodesOrdered.get((i + 1) % nodesOrdered.size())));
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
     * @throws HilbertCurveUnconstructedException Thrown if the curve has not yet been constructed.
     */
    public ArrayList<Node> getNodesOrdered() throws NodeMissedException, HilbertCurveUnconstructedException {

        if (curveCoordinates == null || curveCoordinates.length == 0) {
            throw new HilbertCurveUnconstructedException("No curve constructed.");
        }
        // Find which nodes we want and put them in order according to the graph.
        // We copy the node array here so that we can remove the node from the list once we've found it.

        ArrayList<Node> nodes = new ArrayList<>(graph.getNodeContainer().getNodeSet());
        ArrayList<Node> nodesInOrder = new ArrayList<>();
        for (Coordinate c : curveCoordinates) {
            for (int i = nodes.size() - 1; i >= 0; i--) {
                Node n = nodes.get(i);
                if (n.getCoordinate().match(c)) {
                    nodesInOrder.add(n);
                    nodes.remove(i); // Removing them as we go will speed it up as we advance
                    break; // Don't continue looping through the nodes if we found one.
                }
            }
        }
        // Check to see if we missed nodes - if so throw an exception.
        if (nodesInOrder.size() != graph.getNumNodes()) {
            StringBuilder sb = new StringBuilder("Node(s) missed: ");
            for (Node n : nodes) {
                sb.append(n.toString()).append(", ");
            }
            sb.delete(sb.length()-2, sb.length()-1); // Remove the last comma
            throw new NodeMissedException(sb.toString());
        }
        nodesInOrder.trimToSize();
        return nodesInOrder;
    }

    /**
     * Used to return the coordinates of an index i on the curve.
     * Algorithm found: http://blog.marcinchwedczuk.pl/iterative-algorithm-for-drawing-hilbert-curve
     * @param i The i th corner we find the coordinates of.
     * @return c The coordinates of the i th step on the curve.
     */
    private Coordinate getHilbertCorner(int i) {
        // Mask by 3 to find which quadrant the coordinate is in the order above.
        int index = i & 3; // Works by only caring about the last two bits of the number.
        Coordinate c = orderOneCurve[index].copy(); // This is order 1 (the simplest case)
        // As we go up, we need to continue to check the bits and shift across.
        for (int j = 1; j < order; j++) {
            int len = (int) Math.pow(2, j); // The relative length of this order curve
            i = i >>> 2; // Shift over and get the next two bits.
            index = i & 3; // Mask by three again. (Think: which quadrant are we in this order?)
            // 0 and 2 remain the same, but 1 and 3 are swapped in the rotation
            if (index == 0) {
                int temp = c.getX();
                c.setX(c.getY());
                c.setY(temp);
            } else if (index == 1) {
                c.setY(c.getY() + len);
            } else if (index == 2) {
                c.setX(c.getX() + len);
                c.setY(c.getY() + len);
            } else {
                int temp = len - 1 - c.getX();
                c.setX(len - 1 - c.getY());
                c.setY(temp);
                c.setX(c.getX() + len);
            }
        }
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
        return curveCoordinates;
    }

    /**
     * Used to verify that the coordinate space we are operating in is a square and that it's side length is a power
     * of two.
     */
    private void validateCoordinateSpaceIsSquareOfSideLenPowerOf2() {
        // Check that we are running this solver in a square context with a side length or a n^2
        int w = Main.coordinateMaxWidth;
        int h = Main.coordinateMaxHeight;
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
    private void constructHilbertCurve(boolean centre) {
        curveCoordinates = new Coordinate[numCorners]; // Create space for them.
        for (int i = 0; i < numCorners; i++) {
            curveCoordinates[i] = getHilbertCorner(i); // Get & insert coordinates for each corner
            // Change it from 0, 1 format to fit out window.
            float len = (float) Main.coordinateMaxWidth / N;
            curveCoordinates[i].mult(len);
            // Moves it to the middle - good for animator
            if (centre) {
                 curveCoordinates[i].add(len/2, len/2);
            }
        }
    }

    /**
     * Returns the value of the @code{order} attribute.
     * @return order The value of the @code{order} attribute.
     */
    public int getOrder() {
        return order;
    }

    /**
     * Assigns a new value to the @code{order} attribute.
     * @param order The new value to assign the @code{order} attribute.
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * Returns the value of the @code{N} attribute.
     * @return N The value of the @code{N} attribute.
     */
    public int getN() {
        return N;
    }

    /**
     * Sets the value of the @code{N} attribute.
     * @param n The new value to assign the @code{N} attribute.
     */
    public void setN(int n) {
        this.N = n;
    }

    /**
     * Assigns a new value to the @code{numCorners} attribute.
     * @param numCorners The new value to assign the @code{numCorners} attribute.
     */
    public void setNumCorners(int numCorners) {
        this.numCorners = numCorners;
    }

    /**
     * Assigns the correct values for solving using a hilbert curve using the coordinate space.
     */
    private void setValues() {
        /* The number of corners in a hilbert curve of a given order has
         *  (2^order)^2 corners. Since we try to fill the space, we can
         *  deduce our order given we know how many pixels we need to fill.
         *  i.e. width^2 = (2^order)^2
         *  i.e. width = 2^order */
        setOrder((int) (Math.log(Main.coordinateMaxWidth)/Math.log(2))); // Java has no log2(x)
        // We also know that N is our side length given that N = 2 ^ order
        setN(Main.coordinateMaxWidth);
        setNumCorners((int) Math.pow(getN(), 2));
    }
}

