package com.alike.graphsystem;

import com.alike.customexceptions.NodeSuperimpositionException;
import com.alike.solution_helpers.RepeatedFunctions;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Returns the value of the @code{cm} attribute.
     * @return cm The value of the @code{cm} attribute.
     */
    public CoordinateMover getCm() {
        return cm;
    }

    /**
     * Outputs the values of this graph as a string that can be stored in file and later read back into program.
     * @return
     */
    @Override
    public String toStorageFormat(char delimiter) throws IllegalArgumentException {
        if (delimiter == ',') {
            throw new IllegalArgumentException("Delimiter value cannot be ','");
        }
        StringBuilder sb = new StringBuilder();
        // Add the coordinates
        sb.append(getUnderlyingGraph().getNodeContainer().toStorageFormat(delimiter));
        sb.append(delimiter);
        sb.append(delimiter);
        // Add the velocities of nodes.
        Vector[] vecs = getCm().getCoordinateVelocities();
        StringBuilder vecS = new StringBuilder();
        for (Vector v : vecs) {
            vecS.append(v.toStorageFormat());
            vecS.append(delimiter);
        }
        sb.append(vecS.substring(0, vecS.length() - 1));
        return sb.toString();
    }

    /**
     * Loads a dynamic graph from storage format i.e. cx1,cy1;cx2,cy2;cxn,cyn;;vx1,vy1;vx2,vy2
     * @param storageFormatString The dynamic graphs string representation.
     * @param delimiter The delimiter used to separate graph information.
     * @param speed The speed the graph nodes should be set to.
     * @param randomMovement Whether the nodes should have step randomly on.
     * @param velocityMovement Whether the nodes should have step by velocity on.
     * @return dg A dynamic graph with nodes starting at the written places and starting velocities of the written
     * vectors.
     * @throws NodeSuperimpositionException Thrown if an attempt is made to create a node where one already lies.
     */
    public static DynamicGraph fromStorageFormat(String storageFormatString, char delimiter, int speed,
                                          boolean randomMovement, boolean velocityMovement)
            throws NodeSuperimpositionException {
        // NB: I am not happy with this code but I am rushing through it.
        // Create an array list of our information
        ArrayList<String> elements = new ArrayList<>(List.of(storageFormatString.split(String.valueOf(delimiter))));
        NodeContainer nodeContainer = new NodeContainer();
        if (!elements.contains("")) { // This input doesn't have node velocities => just a list of coordinates.
            for (String s : elements) {
                nodeContainer.add(new Node(Coordinate.parseCoordinate(s)));
            }
            StaticGraph g = new StaticGraph();
            g.setNodeContainer(nodeContainer);
            return new DynamicGraph(g, randomMovement, velocityMovement, speed);
        }
        // We did receive velocities => find nodes and velocities.
        int idxOfCoordVecSplit = elements.indexOf(""); // Index of split
        ArrayList<String> coordinateStrings = new ArrayList<>(elements.subList(0, idxOfCoordVecSplit));
        ArrayList<String> vectorStrings = new ArrayList<>(elements.subList(idxOfCoordVecSplit + 1, elements.size()));
        for (String s : coordinateStrings) { // Add nodes by looping through the coordinates
            System.out.println(s);
            nodeContainer.add(new Node(Coordinate.parseCoordinate(s)));
        }
        StaticGraph g = new StaticGraph(); // Create a graph object.
        g.setNodeContainer(nodeContainer); // Assing the node container.
        DynamicGraph dg = new DynamicGraph(g, randomMovement, velocityMovement, speed); // Create the dg
        // Get and assign the velocities.
        Vector[] velocities = new Vector[vectorStrings.size()];
        int idx = 0;
        for (String s : vectorStrings) {
            velocities[idx] = Vector.parseVector(s);
            idx++;
        }
        dg.getCm().setCoordinateVelocities(velocities);
        return dg;
    }
}
