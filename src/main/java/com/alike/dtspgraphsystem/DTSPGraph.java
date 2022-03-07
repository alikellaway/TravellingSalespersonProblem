package com.alike.dtspgraphsystem;

import com.alike.solution_helpers.RepeatedFunctions;
import com.alike.tspgraphsystem.Graph;
import com.alike.tspgraphsystem.TSPGraph;

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
    private volatile boolean moving;

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
     * A reference to the underlying graph object which this object is manipulating.
     */
    private TSPGraph graph;

    /**
     * The @code{EdgeStateManager} object that will be managing the status of edges (offline/online).
     */
    private EdgeStateManager edgeStateManager;

    /**
     * The default value for the @code{movementSpeed} attribute.
     */
    private static final int DEF_MOVEMENT_SPEED = 3;

    /**
     * The default value for the @code{delayPerStep} attribute.
     */
    private static final int DEF_DELAY_PER_STEP = 10;

    /**
     * Constructs a new DTSP object.
     * @param graph The underlying graph object of the DTSP.
     * @param stepRandomly Whether the nodes are moved using the stepRandomly method in the coordinate mover.
     * @param stepByVelocity Whether the nodes are moved using the stepByVelocity method in the coordinate mover.
     */
    public DTSPGraph(TSPGraph graph, boolean stepRandomly, boolean stepByVelocity) {
        cm = new CoordinateMover(graph.getNodeContainer().getNodeCoordinates(), DEF_MOVEMENT_SPEED);
        setEdgeStateManager(new EdgeStateManager());
        this.graph = graph;
        stop(); // Assigns moving var to false.
        setSteppingRandomly(stepRandomly);
        setSteppingByVelocity(stepByVelocity);
        setDelayPerStep(DEF_DELAY_PER_STEP);
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
                RepeatedFunctions.sleep(delayPerStep);
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
     * Returns the number of nodes the underlying graph has.
     * @return numNodes The number of nodes the graph has.
     */
    public int getNumNodes() {
        return graph.getNumNodes();
    }

    /**
     * Returns the value of the @code{edgeStateManager} attribute.
     * @return edgeStateManger The value of the @code{edgeStateManager} attribute.
     */
    public EdgeStateManager getEdgeStateManager() {
        return edgeStateManager;
    }

    /**
     * Sets the value of the @code{edgeStateManager} attribute.
     * @param edgeStateManager The new value to assign to the @code{edgeStateManager} attribute.
     */
    public void setEdgeStateManager(EdgeStateManager edgeStateManager) {
        this.edgeStateManager = edgeStateManager;
    }

    public TSPGraph getUnderlyingGraph() {
        return this.graph;
    }
}
