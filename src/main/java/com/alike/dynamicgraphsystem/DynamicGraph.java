package com.alike.dynamicgraphsystem;

import com.alike.solution_helpers.RepeatedFunctions;
import com.alike.staticgraphsystem.Graph;
import com.alike.staticgraphsystem.StaticGraph;

/**
 * Class combines the @code{StaticGraph} and @code{CoordinateMover} classes to create a dynamic travelling salesperson
 * problem graph where the nodes move.
 */
public class DynamicGraph implements Graph {
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
    private StaticGraph graph;

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
     * Boolean describing whether this cm is able to listen to move and pause commands.
     */
    private volatile boolean awake;

    /**
     * A double representing the average route length during this DTSPs life-time.
     */
    private double averageRouteLength;

    /**
     * Constructs a new DTSP object.
     * @param graph The underlying graph object of the DTSP.
     * @param stepRandomly Whether the nodes are moved using the stepRandomly method in the coordinate mover.
     * @param stepByVelocity Whether the nodes are moved using the stepByVelocity method in the coordinate mover.
     */
    public DynamicGraph(StaticGraph graph, boolean stepRandomly, boolean stepByVelocity) {
        cm = new CoordinateMover(graph.getNodeContainer().getNodeCoordinates(), DEF_MOVEMENT_SPEED);
        setEdgeStateManager(new EdgeStateManager());
        setGraph(graph);
        stop(); // Assigns moving var to false.
        setSteppingRandomly(stepRandomly);
        setSteppingByVelocity(stepByVelocity);
        setDelayPerStep(DEF_DELAY_PER_STEP);
        setAwake(false);
    }

    public DynamicGraph(StaticGraph graph, boolean stepRandomly, boolean stepByVelocity, int movementSpeed) {
        cm = new CoordinateMover(graph.getNodeContainer().getNodeCoordinates(), movementSpeed);
        setEdgeStateManager(new EdgeStateManager());
        setGraph(graph);
        stop(); // Assigns moving var to false.
        setSteppingRandomly(stepRandomly);
        setSteppingByVelocity(stepByVelocity);
        setDelayPerStep(DEF_DELAY_PER_STEP);
        setAwake(false);
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
     * Sets the value of the @code{awake} attribute to true and starts the coordinate mover thread. This state allows
     * this object to listen for move and stop commands that resume or pause movement of the nodes.
     */
    public void wake() {
        setAwake(true);
        this.moving = false;
        Thread thread = new Thread(() -> {
            while (this.awake) {
                while (moving) {
                    if (steppingRandomly) {
                        cm.stepRandomly();
                    }
                    if (steppingByVelocity) {
                        cm.stepByVelocity();
                    }
                    RepeatedFunctions.sleep(delayPerStep);
                    updateAverageRouteLength();
                }
            }
        });
        thread.start();
    }

    /**
     * Sets the value of the @code{awake} attribute to false which kills the mover's listener thread.
     */
    public void kill() {
        stop();
        this.awake = false;
    }

    /**
     * Sets the value of the @code{moving} attribute to true which will resume the coordinate's movement.
      */
    public void move() {
        moving = true;
    }

    /**
     * Sets the value of the @code{moving} attribute to false which will pause the coordinate's movement.
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

    /**
     * Updates the @code{averageRouteLength} attribute so that it is up-to-date.
     */
    private void updateAverageRouteLength() {
        double currentLength = getUnderlyingGraph().getEdgeContainer().getTotalLength();
        this.averageRouteLength = (this.averageRouteLength + currentLength) / 2;
    }

    /**
     * Returns the value of the @code{averageRouteLength} attribute.
     * @return averageRouteLength The value of the @code{averageRouteLength} attribute.
     */
    public double getAverageRouteLength() {
        return this.averageRouteLength;
    }

    /**
     * Sets the value of the @code{graph} attribute to a new value.
     * @param graph The new value to assign the @code{graph} attribute.
     */
    public void setGraph(StaticGraph graph) {
        this.graph = graph;
    }

    /**
     * Returns the value of the @code{graph} attribute (essentially getGraph(){}).
     * @return graph The value of the @code{graph} attribute.
     */
    public StaticGraph getUnderlyingGraph() {
        return this.graph;
    }

    /**
     * Sets the value of the @code{awake} attribute.
     * @param awake The new value to assign the @code{awake} attribute.
     */
    public void setAwake(boolean awake) {
        this.awake = awake;
    }
}
