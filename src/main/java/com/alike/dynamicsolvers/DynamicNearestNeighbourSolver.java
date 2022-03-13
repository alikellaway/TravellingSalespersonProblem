package com.alike.dynamicsolvers;

import com.alike.customexceptions.EdgeSuperimpositionException;
import com.alike.customexceptions.EdgeToSelfException;
import com.alike.customexceptions.NoClosestNodeException;
import com.alike.dynamicgraphsystem.DynamicGraph;
import com.alike.solution_helpers.RepeatedFunctions;
import com.alike.staticgraphsystem.*;

import java.util.ArrayList;

/**
 * Class uses the nearest neighbour (or greedy) algorithm repeatedly at a set interval to give a route through
 * the moving nodes of a dynamic graph.
 */
public class DynamicNearestNeighbourSolver {
    /**
     * The dgraph this solver will solve.
     */
    private DynamicGraph graph;

    /**
     * A reference to the node container this class is operating on.
     */
    private NodeContainer nodeContainer;

    /**
     * A reference to the edge container this class is operating on.
     */
    private EdgeContainer edgeContainer;

    /**
     * A boolean describing whether this solver is actively calculating solutions.
     */
    private volatile boolean running;

    /**
     * A record of the node we start each solve from.
     */
    private Node origin;

    /**
     * The node the algorithm is currently at.
     */
    private Node currentNode;

    /**
     * Used to construct a new @code{DynamicNearestNeighbourSolver} object.
     * @param graph The @code{DynamicGraph} we will be solving.
     */
    public DynamicNearestNeighbourSolver(DynamicGraph graph) {
        setGraph(graph);
        setNodeContainer(getGraph().getNodeContainer());
        setEdgeContainer(getGraph().getEdgeContainer());
        getGraph().setAllNodesUnvisited();
        setRunning(false);
        setOrigin(nodeContainer.getNodeSet().get(0)); // The node from which we always start.
    }

    /**
     * Used to being the solution's process. Once running, it will continue to resolve the dgraph repeatedly with an
     * interval of @code{delayPerStep} per solve.
     * @param delayPerStep The time delay between each route recalculation.
     */
    public void runSolution(int delayPerStep) {
        graph.wake(); // Doesn't begin movement - just allows it to listen for pause/play commands.
        int maxEdges = nodeContainer.getNodeSet().size(); // The number of edges in a complete tour
        setRunning(true); // Another thread can set this volatile to false and stop the execution.
        setCurrentNode(origin);
        while (running) {
            try {
                graph.stop(); // Pause graph movement, so we can calculate distances between nodes.
                currentNode.setVisited(true); // Set where we are to "visited".
                Node nextNode; // Space for the next node.
                if (!(edgeContainer.getEdgeSet().size() == maxEdges - 1)) {
                    nextNode = findClosestUnvisitedNode(); // If we haven't completed a tour, find the next closest.
                } else {
                    nextNode = getOrigin(); // If we have completed a tour, go back to origin.
                }
                graph.move(); // Resume graph movement.
                // Construct the edge between where we are and next node.
                edgeContainer.add(new Edge(currentNode, nextNode));
                // Move head.
                setCurrentNode(nextNode);
                // If we have completed a tour delete the first added edge
                if (edgeContainer.getEdgeSet().size() == maxEdges) {
                    RepeatedFunctions.sleep(delayPerStep); // We wait before we clear and recalculate.
                    edgeContainer.clear(); // Clear
                    getGraph().setAllNodesUnvisited();
                }
            } catch(NoClosestNodeException ignored) { // Happens everytime we complete a route.
            } catch (EdgeToSelfException | EdgeSuperimpositionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the value of the @code{running} attribute to false.
     */
    public void stop() {
        this.running = false;
    }

    /**
     * Finds the closest node to the @code{currentNode} that is not itself, unvisted and not the origin.
     * @return closestFound The closest found node to the @code{currentNode}.
     * @throws NoClosestNodeException Thrown if a closest node could not be found (might have exhausted the pool).
     */
    private Node findClosestUnvisitedNode() throws NoClosestNodeException {
        Node closestFound = null;
        double shortestDist = Double.MAX_VALUE;
        ArrayList<Node> set = getGraph().getNodeContainer().getNodeSet();
        for (Node n : set) {
            if (!n.equals(getOrigin()) && !n.equals(currentNode) && !n.isVisited()) { // If its not the origin or the start node.
                double dist = currentNode.getVectorTo(n).magnitude();
                if (dist < shortestDist) {
                    shortestDist = dist; closestFound = n;
                }
            }
        }
        if (closestFound == null) {
            throw new NoClosestNodeException("Could not find a closest unvisited node.");
        }
        return closestFound;
    }

    /**
     * Returns the value of the @code{graph} attribute of the DynamicGraph stored in this classes @code{graph} attribute.
     * @return graph The value of the @code{graph} attribute within the @code{graph} attribute of this class.
     */
    public StaticGraph getGraph() {
        return graph.getUnderlyingGraph();
    }

    /**
     * Sets the value of the @code{graph} attribute.
     * @param graph The new value to assign the @code{graph} attribute.
     */
    public void setGraph(DynamicGraph graph) {
        this.graph = graph;
    }

    /**
     * Gets the value of the @code{origin} attribute.
     * @return origin The value of the @code{origin} attribute.
     */
    public Node getOrigin() {
        return this.origin;
    }

    /**
     * Sets the value of the @code{origin} attribute.
     * @param origin The new value to assign the @code{origin} attribute.
     */
    public void setOrigin(Node origin) {
        this.origin = origin;
    }

    /**
     * Stops this solver from repeatedly solving the underlying graph.
     */
    public void kill() {
        this.running = false;
    }

    /**
     * Sets the value of the @code{nodeContainer} attribute.
     * @param nodeContainer The new value to assign to the @code{nodeContainer} attribute.
     */
    public void setNodeContainer(NodeContainer nodeContainer) {
        this.nodeContainer = nodeContainer;
    }

    /**
     * Sets the value of the @code{edgeContainer} attribute.
     * @param edgeContainer The new value to assign to the @code{edgeContainer} attribute.
     */
    public void setEdgeContainer(EdgeContainer edgeContainer) {
        this.edgeContainer = edgeContainer;
    }

    /**
     * Sets the value of the @code{currentNode} attribute to a new value.
     * @param currentNode The new value to assign to the @code{currentNode} attribute.
     */
    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    /**
     * Sets the value of the @code{running} attribute to a new value.
     * @param running The new value to assign the @code{running} attribute.
     */
    public void setRunning(boolean running) {
        this.running = running;
    }
}
