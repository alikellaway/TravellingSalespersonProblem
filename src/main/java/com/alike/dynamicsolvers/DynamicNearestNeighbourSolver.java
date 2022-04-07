package com.alike.dynamicsolvers;

import com.alike.customexceptions.EdgeSuperimpositionException;
import com.alike.customexceptions.EdgeToSelfException;
import com.alike.customexceptions.NoClosestNodeException;
import com.alike.dynamicgraphsystem.DynamicGraph;
import com.alike.solution_helpers.RepeatedFunctions;
import com.alike.solution_helpers.Timer;
import com.alike.solvers.NearestNeighbourSolver;
import com.alike.solvertestsuite.DynamicSolution;
import com.alike.solvertestsuite.SolverOutput;
import com.alike.staticgraphsystem.*;

import java.util.ArrayList;

/**
 * Class uses the nearest neighbour (or greedy) algorithm repeatedly at a set interval to give a route through
 * the moving nodes of a dynamic dgraph.
 */
public class DynamicNearestNeighbourSolver {
    /**
     * The dgraph this solver will solve.
     */
    private DynamicGraph dgraph;

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

    private NearestNeighbourSolver nns;

    /**
     * Used to construct a new @code{DynamicNearestNeighbourSolver} object.
     * @param dgraph The @code{DynamicGraph} we will be solving.
     */
    public DynamicNearestNeighbourSolver(DynamicGraph dgraph) {
        setDgraph(dgraph);
        setNodeContainer(getDgraph().getNodeContainer());
        setEdgeContainer(getDgraph().getEdgeContainer());
        getDgraph().setAllNodesUnvisited();
        setRunning(false);
        setOrigin(nodeContainer.getNodeSet().get(0)); // The node from which we always start.
        nns = new NearestNeighbourSolver(dgraph.getUnderlyingGraph());
    }

    /**
     * Used to being the solution's process. Once running, it will continue to resolve the dgraph repeatedly with an
     * interval of @code{delayPerStep} per solve until the @code{running} attribute is changed to false.
     * @param delayPerSolve The time delay between each route recalculation.
     */
    public void runSolution(int delayPerSolve) {
        dgraph.wake(); // Doesn't begin movement - just allows it to listen for pause/play commands.
        setRunning(true); // Another thread can set this volatile to false and stop the execution.
        while (running) {
            dgraph.stop(); // Pause dgraph movement, so we can calculate distances between nodes.
            nns.runSolution(0);
            dgraph.move(); // Resume dgraph movement.
            RepeatedFunctions.sleep(delayPerSolve);
            dgraph.getUnderlyingGraph().getEdgeContainer().clear();
        }
    }

    public SolverOutput runSolution(int runTime, int delayPerSolve) {
        dgraph.wake(); // Listen for start stop commands.
        Timer t = new Timer();
        t.time(runTime);
        while (t.isTiming()) {
            try {
                dgraph.stop();
                nns.runSolution(0);
                dgraph.move();
                RepeatedFunctions.sleep(delayPerSolve);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new DynamicSolution(dgraph.getAverageRouteLength(), runTime);
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
        ArrayList<Node> set = getDgraph().getNodeContainer().getNodeSet();
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
     * Returns the value of the @code{dgraph} attribute of the DynamicGraph stored in this classes @code{dgraph} attribute.
     * @return dgraph The value of the @code{dgraph} attribute within the @code{dgraph} attribute of this class.
     */
    public StaticGraph getDgraph() {
        return dgraph.getUnderlyingGraph();
    }

    /**
     * Sets the value of the @code{dgraph} attribute.
     * @param dgraph The new value to assign the @code{dgraph} attribute.
     */
    public void setDgraph(DynamicGraph dgraph) {
        this.dgraph = dgraph;
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
     * Stops this solver from repeatedly solving the underlying dgraph.
     */
    public void kill() {
        setRunning(false);
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
