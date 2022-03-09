package com.alike.solvers;

import com.alike.customexceptions.EdgeSuperimpositionException;
import com.alike.customexceptions.EdgeToSelfException;
import com.alike.customexceptions.NoClosestNodeException;
import com.alike.dtspgraphsystem.DynamicGraph;
import com.alike.solution_helpers.RepeatedFunctions;
import com.alike.tspgraphsystem.*;

import java.util.ArrayList;

public class DynamicNearestNeighbourSolver {
    /**
     * The dgraph this solver will solve.
     */
    private DynamicGraph graph;

    private volatile boolean running;

    /**
     * The node the algorithm is currently at.
     */
    private Node currentNode;

    public DynamicNearestNeighbourSolver(DynamicGraph graph) {
        setGraph(graph);
    }

    public void runSolution(int delayPerStep) {
        NodeContainer nodeContainer = getGraph().getNodeContainer();
        EdgeContainer edgeContainer = getGraph().getEdgeContainer();
        getGraph().setAllNodesUnvisited();
        int maxEdges = nodeContainer.getNodeSet().size(); // The number of edges in a complete tour
        graph.wake(); // Doesn't begin movement - just allows it to listen for pause/play commands.
        this.running = true;
        this.currentNode = nodeContainer.getNodeSet().get(0);
        while (running) {
            try {
                graph.stop();
                Node nextNode = findClosestUnvisitedNode();
                graph.move();
                Edge e = new Edge(currentNode, nextNode);
                edgeContainer.add(e);
                currentNode = nextNode;
                // If we have filled delete the first added edge
                if (edgeContainer.getEdgeSet().size() == maxEdges) {
                    edgeContainer.clear();
                    getGraph().setAllNodesUnvisited();
                } // TODO removing the first element is really inefficient - different data structure?
            } catch(NoClosestNodeException ignored) {

            } catch (EdgeToSelfException | EdgeSuperimpositionException e) {
                e.printStackTrace();
            }
            RepeatedFunctions.sleep(delayPerStep);
        }
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
     * Sets the value of the @code{running} attribute to false.
     */
    public void stop() {
        this.running = false;
    }

    private void traverseToNearest() {
        graph.stop();
        // Find and travel to the closest node.

    }

    private Node findClosestUnvisitedNode() throws NoClosestNodeException {
        ArrayList<Node> set = getGraph().getNodeContainer().getNodeSet();
        return currentNode.getClosestNode(set, true);

    }
}
