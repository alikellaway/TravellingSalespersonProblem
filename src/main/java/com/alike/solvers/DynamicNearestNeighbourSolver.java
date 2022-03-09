package com.alike.solvers;

import com.alike.customexceptions.EdgeSuperimpositionException;
import com.alike.customexceptions.EdgeToSelfException;
import com.alike.customexceptions.NoClosestNodeException;
import com.alike.dtspgraphsystem.DTSPGraph;
import com.alike.solution_helpers.RepeatedFunctions;
import com.alike.tspgraphsystem.*;

import java.util.ArrayList;

public class DynamicNearestNeighbourSolver {
    /**
     * The dgraph this solver will solve.
     */
    private DTSPGraph graph;

    private volatile boolean running;

    /**
     * The node the algorithm is currently at.
     */
    private TSPNode currentNode;

    public DynamicNearestNeighbourSolver(DTSPGraph graph) {
        setGraph(graph);
    }

    public void runSolution(int delayPerStep) {
        TSPNodeContainer nodeContainer = getGraph().getNodeContainer();
        TSPEdgeContainer edgeContainer = getGraph().getEdgeContainer();
        getGraph().setAllNodesUnvisited();
        int maxEdges = nodeContainer.getNodeSet().size(); // The number of edges in a complete tour
        this.running = true;
        while (running) {
            try {
                graph.stop();
                TSPNode nextNode = findClosestUnvisitedNode();
                graph.move();
                TSPEdge e = new TSPEdge(currentNode, nextNode);
                edgeContainer.add(e);
                currentNode = nextNode;
                // If we have filled delete the first added edge
                if (edgeContainer.getEdgeSet().size() == maxEdges) {
                    edgeContainer.clear();
                } // TODO removing the first element is really inefficient - different data structure?
            } catch(NoClosestNodeException ignored) {

            } catch (EdgeToSelfException | EdgeSuperimpositionException e) {
                e.printStackTrace();
            }
            RepeatedFunctions.sleep(delayPerStep);
        }
    }


    /**
     * Returns the value of the @code{graph} attribute of the DTSPGraph stored in this classes @code{graph} attribute.
     * @return graph The value of the @code{graph} attribute within the @code{graph} attribute of this class.
     */
    public TSPGraph getGraph() {
        return graph.getUnderlyingGraph();
    }

    /**
     * Sets the value of the @code{graph} attribute.
     * @param graph The new value to assign the @code{graph} attribute.
     */
    public void setGraph(DTSPGraph graph) {
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

    private TSPNode findClosestUnvisitedNode() throws NoClosestNodeException {
        ArrayList<TSPNode> set = getGraph().getNodeContainer().getNodeSet();
        return currentNode.getClosestNode(set, true);

    }
}