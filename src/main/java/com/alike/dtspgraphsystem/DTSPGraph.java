package com.alike.dtspgraphsystem;

import com.alike.tspgraphsystem.Graph;
import com.alike.tspgraphsystem.TSPEdge;
import com.alike.tspgraphsystem.TSPGraph;

import java.util.ArrayList;

/**
 * Class combines the @code{TSPGraph} and @code{CoordinateMover} classes to create a dynamic travelling salesperson
 * problem graph where the nodes move.
 */
public class DTSPGraph implements Graph {
    /**
     * The mover that will be moving the nodes on the graph around.
     */
    private final CoordinateMover cm;

    /**
     * A boolean that describes whether the DTSP's nodes are currently moving. True if they are moving.
     */
    private boolean moving;

    /**
     * A boolean that describes whether the DTSP's nodes are being moved using the stepRandomly method in the
     * coordinate mover.
     */
    private boolean steppingRandomly;

    /**
     * A boolean that describes whether the DTSP's nodes are being moved using the stepByVelocity method in the
     * coordinate mover.
     */
    private boolean steppingByVelocity;

    /**
     * The delay per movement step or tick (if you don't want a delay then put 0).
     */
    private int delayPerStep;

    /**
     * A collection of edges which have been disallowed (taken offline).
     */
    private final ArrayList<TSPEdge> disallowedEdges;

    /**
     * A reference to the underlying graph object which this object is manipulating.
     */
    private TSPGraph graph;

    /**
     * Constructs a new DTSP object.
     * @param graph The underlying graph object of the DTSP.
     * @param movementSpeed The number value affecting the speed at which the nodes move.
     * @param stepRandomly Whether the nodes are moved using the stepRandomly method in the coordinate mover.
     * @param stepByVelocity Whether the nodes are moved using the stepByVelocity method in the coordinate mover.
     */
    public DTSPGraph(TSPGraph graph, int movementSpeed, int delayPerStep, boolean stepRandomly, boolean stepByVelocity) {
        cm = new CoordinateMover(graph.getNodeContainer().getNodeCoordinates(), movementSpeed);
        disallowedEdges = new ArrayList<>();
        this.graph = graph;
        stop(); // Assigns moving var to false.
        setSteppingRandomly(stepRandomly);
        setSteppingByVelocity(stepByVelocity);
        setDelayPerStep(delayPerStep);
    }

    /**
     * Sets the @code{steppingRandomly} attribute to a new value.
     * @param steppingRandomly The new value to assign the @code{steppingRandomly} attribute.
     */
    public void setSteppingRandomly(boolean steppingRandomly) {
        this.steppingRandomly = steppingRandomly;
    }

    /**
     * Sets the @code{steppingByVelocity} attribute to a new value.
     * @param steppingByVelocity The new value to assign the @code{steppingByVelocity} attribute.
     */
    public void setSteppingByVelocity(boolean steppingByVelocity) {
        this.steppingByVelocity = steppingByVelocity;
    }

    /**
     * Starts a thread that calls the step methods desired repeatedly until the @code{move} attribute becomes false.
     */
    public void move() {
        moving = true;
        Thread thread = new Thread(() -> {
            while (moving) {
                if (steppingRandomly) {
                    cm.stepRandomly();
                }
                if (steppingByVelocity) {
                    cm.stepByVelocity();
                }
                try {
                    Thread.sleep(delayPerStep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * Sets the move attribute to false (which will stop the graphs movmement if it is already moving else nothing).
     */
    public void stop() {
        moving = false;
    }

    /**
     * Sets the value of the @code{delayPerStep} attribute to a new value.
     * @param delayPerStep The new value to assign to the @code{delayPerStep} attribute.
     */
    public void setDelayPerStep(int delayPerStep) {
        this.delayPerStep = delayPerStep;
    }

    /**
     * Used to check if an edge is allowed on the DTSP graph.
     * @param edge The edge to check for.
     * @return boolean True if the edge is disallowed. False if the edge is allowed.
     */
    public boolean edgeDisallowed(TSPEdge edge) {
        for (TSPEdge e : disallowedEdges) {
            if (e.equals(edge)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Used to disallow edges on the DTSP.
     * @param edge The edge to disallow on the DTSP.
     */
    public void disallowEdge(TSPEdge edge) {
        disallowedEdges.add(edge);
    }

    /**
     * Used to re-allow edges on the DTSP that have since been disallowed.
     * @param edge The edge to re-allow.
     */
    public void reAllowEdge(TSPEdge edge) {
        disallowedEdges.removeIf(edge::equals);
    }

    public int getNumNodes() {
        return graph.getNumNodes();
    }
}
