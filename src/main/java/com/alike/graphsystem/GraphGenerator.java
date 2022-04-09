package com.alike.graphsystem;

import com.alike.Main;
import com.alike.customexceptions.*;

/**
 * Class contains functionality to generate StaticGraph objects of certain types or with certain properties.
 */
public class GraphGenerator {

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
     * @return StaticGraph A new graph containing nodes arranged in the shape of a polygon.
     * @throws InvalidGraphException Thrown if the @code{numCorners} parameter is less than 3.
     * @throws NodeSuperimpositionException Thrown if an attempt is made to superimpose another node.
     */
    public static StaticGraph generateRegularPolygonalGraph(int numCorners) throws InvalidGraphException, NodeSuperimpositionException {
        if (numCorners < 3) { // Check the graph has at least 3 nodes.
            throw new InvalidGraphException("Cannot generate a graph with less than 3 nodes.");
        }
        Node.restartNodeCounter();
        // Create a graph to populate
        StaticGraph staticGraph = new StaticGraph();
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
            staticGraph.getNodeContainer().add(new Node(c));
            currentAngle += angleStep;
        }
        return staticGraph;
    }


    /**
     * Generates points that lie on an ellipse to create an irregular polygon.
     * @param numCorners The number of corners the polygon will have.
     * @param radiusX The maximum coordinate in the x direction.
     * @param radiusY The maximum coordinate in the y direction.
     * @return tspGraph A @code{StaticGraph} that contains new nodes arranged as an irregular polygon.
     * @throws InvalidGraphException Thrown if the input numCorners is less than 3.
     * @throws NodeSuperimpositionException Thrown if an attempt is made to superimpose a node.
     * @throws RadiusExceedingBoundaryException Thrown if an attempt is made to stretch a polygon outside the display
     * area.
     */
    public static StaticGraph generateIrregularPolygonalGraph(
            int numCorners, int radiusX, int radiusY
        ) throws InvalidGraphException, NodeSuperimpositionException, RadiusExceedingBoundaryException {
        if (numCorners < 3) { // Check the graph has at least 3 nodes.
            throw new InvalidGraphException("Cannot generate a graph with less than 3 nodes.");
        }
        // Check the polgyon is not stretched too far to be displayed.
//        if (radiusX * 2 > Main.COORDINATE_MAX_WIDTH) {
//            throw new RadiusExceedingBoundaryException("The radius of the polygon ("+ radiusX + ") was too large in " +
//                    "the x direction to be displayed as the maximum x diameter is " + Main.COORDINATE_MAX_WIDTH);
//        }
//        if (radiusY * 2 > Main.COORDINATE_MAX_HEIGHT) {
//            throw new RadiusExceedingBoundaryException("The radius of the polygon (" + radiusY + ") was too large in" +
//                    " the y direction to be displayed as the maximum y diameter is " + Main.COORDINATE_MAX_HEIGHT);
//        }
        Node.restartNodeCounter(); // We need the nodes in each graph to be numbered from 0.
        // Create a graph to populate
        StaticGraph staticGraph = new StaticGraph();
        // Note: 2Pi is the number of radians in a circle
        double maxRad = 2 * Math.PI;
        // Work our way around the circle in a step wise manner
        double angleStep = (maxRad)/numCorners; // This is the step in angle between each corner
            // Note 2Pi is the number of radians in a circle
        double currentAngle = 0;
        while (currentAngle < maxRad) {
            // Generate the coordinate for this step
            int x = (int) (radiusX * ELLIPSE_X_RADIUS_RATIO * Math.sin(currentAngle) + Main.COORDINATE_MAX_WIDTH/2);
            int y = (int) (radiusY * ELLIPSE_Y_RADIUS_RATIO * Math.cos(currentAngle) + Main.COORDINATE_MAX_HEIGHT/2);
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
            staticGraph.getNodeContainer().add(new Node(new Coordinate(x, y)));
            currentAngle += angleStep; // Increment the angle
        }
        return staticGraph;
    }

    /**
     * Used to generate a random StaticGraph object with randomized node positions and randomized edges (if needed).
     * @param numNodes The number of nodes the random graph must have.
     * @param generateRandomEdges Whether the method should output a graph with randomized edges already assigned.
     * @return g A new randomized StaticGraph object.
     */
    public static StaticGraph generateRandomGraph(int numNodes, boolean generateRandomEdges ) {
        Node.restartNodeCounter();
        // Create some random nodes
        NodeContainer nSet = new NodeContainer();
        while (nSet.getNodeSet().size() != numNodes) {
            Node newNode = Node.generateRandomTSPNode();
            try {
                nSet.add(newNode);
            } catch (NodeSuperimpositionException e) {
                /* We failed to add the node in because it clashed with something already in the plane.
                * We need to regenerate a node without incrementing the node counter in the node class. */
                Node.decrementNumNodes();
                // If the plane is completely full, then we want to terminate.
                if (nSet.getNodeSet().size() == Main.COORDINATE_MAX_WIDTH * Main.COORDINATE_MAX_HEIGHT) {
                    e.printStackTrace();
                }
            }
        }
        nSet.trimToSize();
        // Create an empty edge set.
        EdgeContainer eSet = new EdgeContainer();
        if (generateRandomEdges) { // Fill with random edges using trial and error if edges are required.
            for (int x = 0; x < numNodes; x++) {
                try {
                    eSet.add(new Edge(nSet.getNodeSet().get(x), nSet.getNodeSet().get((x + 1) % numNodes)));
                } catch (EdgeSuperimpositionException | EdgeToSelfException e) {
                    x--;
                }
            }
        }
        // Construct a new StaticGraph object and return it.
        StaticGraph g = new StaticGraph();
        g.setNodeContainer(nSet);
        g.setEdgeContainer(eSet);
        return g;
    }

    public static DynamicGraph generateRandomDynamicGraph(int numNodes, int nodeSpeed, boolean randomMovement, boolean vectorMovement) {
        StaticGraph g = generateRandomGraph(numNodes, false);
        Vector[] vectors = Vector.randomVectors(numNodes, nodeSpeed);
        DynamicGraph dg = new DynamicGraph(g, randomMovement, vectorMovement);
        dg.getCm().setCoordinateVelocities(vectors);
        return dg;
    }
}
