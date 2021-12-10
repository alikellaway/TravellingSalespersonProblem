package com.alike.tspgraphsystem;

import com.alike.customexceptions.EdgeSuperimpositionException;
import com.alike.customexceptions.EdgeToSelfException;
import com.alike.customexceptions.InvalidGraphException;
import com.alike.customexceptions.NodeSuperimpositionException;

/**
 * Class contains functionality to generate TSPGraph objects of certain types or with certain properties.
 */
public class TSPGraphGenerator {

    /**
     * This number represents the value the radius will take relative to the maximum x inside the polygonal graph
     * generator method: @code{generateRegularPolygonalGraph{}}.
     */
    private static final double CIRCLE_RADIUS_RATIO = 0.45;

    /**
     * Uses parametric equations to retrieve points on a circle to generate regular polygonal graphs.
     * @param numCorners The number of corners our regular polygon will have.
     * @param xMax The maximum X value a coordinate can take.
     * @param yMax The maximum Y value a coordinate can take.
     * @return @code{TSPGraph} A new graph containing nodes arranged in the shape of a polygon.
     * @throws InvalidGraphException Thrown if the @code{numCorners} parameter is less than 3.
     * @throws NodeSuperimpositionException Thrown if an attempt is made to superimpose another node.
     */
    public static TSPGraph generateRegularPolygonalGraph(int numCorners, int xMax, int yMax) throws InvalidGraphException, NodeSuperimpositionException {
        if (numCorners < 3) { // Check the graph has at least 3 nodes.
            throw new InvalidGraphException("Cannot generate a graph with less than 3 nodes.");
        }
        // Create a graph to populate
        TSPGraph tspGraph = new TSPGraph();
        // Calculate a radius to be a quarter of the width of the screen
        double r = xMax * CIRCLE_RADIUS_RATIO; // Chose 2.1 cos visibly pleasing
        // Work our way around the circle in a step wise manner
        double angleStep = (2 * Math.PI)/numCorners; // This is the step in angle between each corner
        double currentAngle = 0;
        while (currentAngle < 2 * Math.PI) {
            // Generate the coordinate for this step
            int y = (int) (r * Math.cos(currentAngle) + yMax/2);
            int x = (int) (r * Math.sin(currentAngle) + xMax/2);
            Coordinate c = new Coordinate(x, y);
            tspGraph.getNodeContainer().add(new TSPNode(c));
            currentAngle += angleStep;
        }
        return tspGraph;
    }

    /**
     * Used to generate a random TSPGraph object with randomized node positions and randomized edges (if needed).
     * @param numNodes The number of nodes the random graph must have.
     * @param addEdges Whether or not the method should output a graph with randomized edges already assigned.
     * @return @code{g} A new randomized TSPGraph object.
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
