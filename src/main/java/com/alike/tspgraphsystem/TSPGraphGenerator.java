package com.alike.tspgraphsystem;

import com.alike.Main;
import com.alike.customexceptions.*;

/**
 * Class contains functionality to generate TSPGraph objects of certain types or with certain properties.
 */
public class TSPGraphGenerator {

    /**
     * This number represents the fraction of the window the radius is for the circle generated in the regular
     * polygon graph creator method.
     */
    private static final double CIRCLE_RADIUS_RATIO = 0.45;

    /**
     * This number represents the fraction of the window the x radius is for an ellipse generated in the irregular
     * polygon graph creator method.
     */
    private static final double ELLIPSE_X_RADIUS_RATIO = 0.45;

    /**
     * This number represents the fraction of the window the y radius is for an ellipse generated in the irregular
     * polygon graph creator method.
     */
    private static final double ELLIPSE_Y_RADIUS_RATIO = 0.45;

    /**
     * Uses parametric equations to retrieve points on a circle to generate regular polygonal graphs.
     * @param numCorners The number of corners our regular polygon will have.
     * @return @code{TSPGraph} A new graph containing nodes arranged in the shape of a polygon.
     * @throws InvalidGraphException Thrown if the @code{numCorners} parameter is less than 3.
     * @throws NodeSuperimpositionException Thrown if an attempt is made to superimpose another node.
     */
    public static TSPGraph generateRegularPolygonalGraph(int numCorners) throws InvalidGraphException, NodeSuperimpositionException {
        if (numCorners < 3) { // Check the graph has at least 3 nodes.
            throw new InvalidGraphException("Cannot generate a graph with less than 3 nodes.");
        }
        TSPNode.restartNodeCounter();
        // Create a graph to populate
        TSPGraph tspGraph = new TSPGraph();
        // Calculate radius from width or height of screen of the screen (depends on which is smaller)
        int min = Math.min(Main.COORDINATE_MAX_WIDTH, Main.COORDINATE_MAX_HEIGHT);
        double r = min * CIRCLE_RADIUS_RATIO; // Chose 2.1 cos visibly pleasing
        // Work our way around the circle in a step wise manner
        double angleStep = (2 * Math.PI)/numCorners; // This is the step in angle between each corner
        double currentAngle = 0;
        while (currentAngle < 2 * Math.PI) {
            // Generate the coordinate for this step
            int y = (int) (r * Math.cos(currentAngle) + Main.COORDINATE_MAX_HEIGHT/2);
            int x = (int) (r * Math.sin(currentAngle) + Main.COORDINATE_MAX_WIDTH/2);
            Coordinate c = new Coordinate(x, y);
            tspGraph.getNodeContainer().add(new TSPNode(c));
            currentAngle += angleStep;
        }
        return tspGraph;
    }


    /**
     * Generates points that lie on an ellipse to create an irregular polygon.
     * @param numCorners The number of corners the polygon will have.
     * @param radiusX The maximum coordinate in the x direction.
     * @param radiusY The maximum coordinate in the y direction.
     * @return tspGraph A @code{TSPGraph} that contains new nodes arranged as an irregular polygon.
     * @throws InvalidGraphException Thrown if the input numCorners is less than 3.
     * @throws NodeSuperimpositionException Thrown if an attempt is made to superimpose a node.
     * @throws RadiusExceedingBoundaryException Thrown if an attempt is made to stretch a polygon outside the display
     * area.
     */
    public static TSPGraph generateIrregularPolygonalGraph(
            int numCorners, int radiusX, int radiusY
        ) throws InvalidGraphException, NodeSuperimpositionException, RadiusExceedingBoundaryException {
        if (numCorners < 3) { // Check the graph has at least 3 nodes.
            throw new InvalidGraphException("Cannot generate a graph with less than 3 nodes.");
        }
        // Check the polgyon is not stretched too far to be displayed.
        if (radiusX * 2 > Main.COORDINATE_MAX_WIDTH) {
            throw new RadiusExceedingBoundaryException("The radius of the polygon ("+ radiusX + ") was too large in " +
                    "the x direction to be displayed as the maximum x diameter is " + Main.COORDINATE_MAX_WIDTH);
        }
        if (radiusY * 2 > Main.COORDINATE_MAX_HEIGHT) {
            throw new RadiusExceedingBoundaryException("The radius of the polygon (" + radiusY + ") was too large in" +
                    " the y direction to be displayed as the maximum y diameter is " + Main.COORDINATE_MAX_HEIGHT);
        }
        TSPNode.restartNodeCounter(); // We need the nodes in each graph to be numbered from 0.
        // Create a graph to populate
        TSPGraph tspGraph = new TSPGraph();
        // Note: 2Pi is the number of radians in a circle
        double maxRad = 2 * Math.PI;
        // Work our way around the circle in a step wise manner
        double angleStep = (maxRad)/numCorners; // This is the step in angle between each corner
            // Note 2Pi is the number of radians in a circle
        double currentAngle = 0;
        while (currentAngle < maxRad) {
            // Generate the coordinate for this step
            int x = (int) (radiusX * Math.sin(currentAngle) + Main.COORDINATE_MAX_WIDTH/2);
            int y = (int) (radiusY * Math.cos(currentAngle) + Main.COORDINATE_MAX_HEIGHT/2);
            // We have divided by two to move the coordinate to the middle of the window ^ and away from the origin.
            Coordinate c = new Coordinate(x, y);
            /* Check that the node is not outside the coordinate space in either direction. If it is then set the
             * value to the max/min value. */
            if (c.getX() > radiusX) {
                c.setX(radiusX);
            }
            if (c.getY() > radiusY) {
                c.setY(radiusY);
            }
            if (c.getX() < 0) {
                c.setX(0);
            }
            if (c.getY() < 0) {
                c.setY(0);
            }
            // Add the coordinate to the container
            tspGraph.getNodeContainer().add(new TSPNode(new Coordinate(x, y)));
            currentAngle += angleStep; // Increment the angle
        }
        return tspGraph;
    }

    /**
     * Used to generate a random TSPGraph object with randomized node positions and randomized edges (if needed).
     * @param numNodes The number of nodes the random graph must have.
     * @param addEdges Whether or not the method should output a graph with randomized edges already assigned.
     * @return g A new randomized TSPGraph object.
     */
    public static TSPGraph generateRandomGraph(int numNodes, boolean addEdges) {
        TSPNode.restartNodeCounter();
        // Create some random nodes
        TSPNodeContainer nSet = new TSPNodeContainer();
        for (int i = 0; i < numNodes; i++) {
            try {
                nSet.add(TSPNode.generateRandomTSPNode());
            } catch (NodeSuperimpositionException e) {
                i--;
            }
        }
        // Create an empty edge set.
        TSPEdgeContainer eSet = new TSPEdgeContainer();
        if (addEdges) { // Fill with random edges using trial and error if edges are required.
            for (int x = 0; x < numNodes; x++) {
                try {
                    eSet.add(new TSPEdge(nSet.getNodeSet().get(x), nSet.getNodeSet().get((x + 1) % numNodes)));
                } catch (EdgeSuperimpositionException | EdgeToSelfException e) {
                    x--;
                }
            }
        }
        // Construct a new TSPGraph object and return it.
        TSPGraph g = new TSPGraph();
        g.setNodeContainer(nSet);
        g.setEdgeContainer(eSet);
        return g;
    }
}
