package com.alike.dtspgraphsystem;

import com.alike.tspgraphsystem.Coordinate;
import com.alike.tspgraphsystem.TSPGraph;

/**
 * Class combines the @code{TSPGraph} and @code{CoordinateMover} classes to create a dynamic travelling salesperson
 * problem graph where the nodes move.
 */
public class DTSPGraph {
    /**
     * The mover that will be moving the nodes on the graph around.
     */
    private CoordinateMover cm;

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

    private int delayPerStep;

    /**
     * Constructs a new DTSP object.
     * @param graph The underlying graph object of the DTSP.
     * @param movementSpeed The number value affecting the speed at which the nodes move.
     * @param stepRandomly Whether the nodes are moved using the stepRandomly method in the coordinate mover.
     * @param stepByVelocity Whether the nodes are moved using the stepByVelocity method in the coordinate mover.
     */
    public DTSPGraph(TSPGraph graph, int movementSpeed, int delayPerStep, boolean stepRandomly, boolean stepByVelocity) {
        cm = new CoordinateMover(graph.getNodeContainer().getNodeCoordinates(), movementSpeed);
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

    public void setDelayPerStep(int delayPerStep) {
        this.delayPerStep = delayPerStep;
    }
}
