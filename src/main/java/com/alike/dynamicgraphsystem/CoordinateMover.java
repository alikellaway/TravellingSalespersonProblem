package com.alike.dynamicgraphsystem;

import com.alike.Main;
import com.alike.staticgraphsystem.Coordinate;
import com.alike.staticgraphsystem.Vector;

import java.util.ArrayList;
import java.util.Random;

/**
 * CoordinateMover class is used to move a coordinate along/up/down one value depending on the method called.
 */
public class CoordinateMover {
    /**
     * The list of coordinates we are manipulating.
     */
    private ArrayList<Coordinate> coordinates;

    /**
     * The value of which movement speed scales.
     */
    private int movementSpeed;

    /**
     * An array to store the velocities of the coordinates (if required).
     */
    private Vector[] coordinateVelocities = null;

    /**
     * The random object used to generate the random components of the class.
     */
    private final Random rand = new Random();

    /**
     * This enum is used to represent directions in the following code.
     */
    private enum Direction {
        UP, UPRIGHT, RIGHT, DOWNRIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT, NONE;
    }

    /**
     * Creates a new CoordinateMover object.
     * @param coordinates The list of Coordinates that this mover will be manipulating.
     * @param speed The value to assign to the @code{movementSpeed} attribute.
     */
    public CoordinateMover(ArrayList<Coordinate> coordinates, int speed) {
        setCoordinates(coordinates);
        setMovementSpeed(speed);
    }

    /**
     * A wrapper method for the @code{stepRandomly} method.
     */
    public void stepRandomly() {
        stepRandomly(movementSpeed);
    }

    /**
     * Moves each coordinate in @code{coordinates} by their value in @code{coordinateVelocities} (matched by index).
     */
    public void stepByVelocity() {
        // If the list of velocities is not yet populated then initialise it and fill it with random velocities.
        if (coordinateVelocities == null) {
            coordinateVelocities = new Vector[coordinates.size()];
            for (int i = 0; i < coordinates.size(); i++) {
                // Produces velocities with integer numbers
                coordinateVelocities[i] = Vector.randomVector(movementSpeed); // This takes into account the speed.
            }
        }
        // Start the movement
        for (int i = 0; i < coordinates.size(); i++) { // Move each coordinate
            moveCoordinateByAVector(coordinates.get(i), coordinateVelocities[i]);
        }
    }

    /**
     * Sets the value of the @code{coordinates} attribute to a new value.
     * @param coordinates The new value to assign to the @code{coordinates} value.
     */
    public void setCoordinates(ArrayList<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Moves a coordinate by a vector a reflects the coordinate if it hits a boundary line.
     * @param c The coordinate to move.
     * @param v The vector by which to move the coordinate.
     */
    private void moveCoordinateByAVector(Coordinate c, Vector v) {
        // Do the x component of the vector first.
        if (v.getX() < 0) { // The vector is moving the coordinate left.
            for (int x = 0; x < v.getX() * -1; x++) {
                moveCoordinateAStep(c, Direction.LEFT); // decrement x (move it left)
                // If we hit the left wall then we need to invert the velocity's x component and skip.
                if (c.getX() <= 0) {
                    v.invertX();
                    break;
                }
            }
        } else { // The vector is moving the coordinate right.
            for (int x = 0; x < v.getX(); x++) {
                moveCoordinateAStep(c, Direction.RIGHT); // Increment x (move it right).
                // If we hit the right wall then we need to invert the velocity's x component and stop.
                if (c.getX() >= Main.COORDINATE_MAX_WIDTH) {
                    v.invertX();
                    break;
                }
            }
        }
        // Do the y component of the vector.
        if (v.getY() < 0) { // The vector is moving the coordinate upwards.
            for (int y = 0; y < v.getY() * -1; y++) {
                moveCoordinateAStep(c, Direction.UP); // Decrement y (move it upwards).
                // If we hit the top wall then we need to invert the velocity's y component and skip.
                if (c.getY() <= 0) {
                    v.invertY();
                    break;
                }
            }
        } else { // The vector is moving the coordinate downwards.
            for (int y = 0; y < v.getY(); y++) {
                moveCoordinateAStep(c, Direction.DOWN); // Increment y (move it down).
                // If we hit the bottom wall then we need to invert the velocity's y component and skip.
                if (c.getY() >= Main.COORDINATE_MAX_HEIGHT) {
                    v.invertY();
                    break;
                }
            }
        }
    }

    /**
     * Moves a coordinate to one of the surrounding coordinates (randomly). Coordinates will not hit the edge i.e. will
     * not overflow and will not bounce of the edge.
     */
    private void stepRandomly(int speed) { // We can use recursion here to make the particle move more per tick.
        for (Coordinate coordinate : coordinates) {
            // Get the possible directions
            Direction[] possibleDirections = getPossibleDirections(coordinate);
            // Choose a random Direction by choosing a random index and move the coordinate in that Direction.
            Direction dir = possibleDirections[rand.nextInt(possibleDirections.length)];
            moveCoordinateAStep(coordinate, dir);
        }
        if (speed != 0) {
            stepRandomly(speed - 1);
        }
    }

    /**
     * Returns true if the input coordinate is touching the top edge of the coordinate space i.e. y = 0;
     * @param coordinate The coordinate to check.
     * @return boolean: True if the coordinate is touching the top edge.
     */
    private boolean coordinateTouchingTopEdge(Coordinate coordinate) {
        return coordinate.getY() <= 0;
    }

    /**
     * Returns true if the input coordinate is touching the bottomm edge of the coordinate space i.e. y = Max
     * @param coordinate The coordinate to check.
     * @return boolean: True if the coordinate is touching the top edge.
     */
    private boolean coordinateTouchingBottomEdge(Coordinate coordinate) {
        return coordinate.getY() >= Main.COORDINATE_MAX_HEIGHT;
    }

    /**
     * Returns true if the input coordinate is touching the left edge of the coordinate space i.e. x = 0
     * @param coordinate The coordinate to check.
     * @return boolean: True if the coordinate is touching the left edge.
     */
    private boolean coordinateTouchingLeftEdge(Coordinate coordinate) {
        return coordinate.getX() <= 0;
    }

    /**
     * Returns true if the input coordinate is touching the right edge of the coordinate space i.e. x = Max
     * @param coordinate The coordinate to check.
     * @return boolean: True if the coordinate is touching the right edge of the coordinate space.
     */
    private boolean coordinateTouchingRightEdge(Coordinate coordinate) {
        return coordinate.getX() >= Main.COORDINATE_MAX_WIDTH;
    }



    /**
     * Deduces which of the 8 possible directions a coordinate can step to.
     * @param c The coordinate to check possible directions for.
     * @return int[] A list of the possible directions represented as an integer array.
     */
    private Direction[] getPossibleDirections(Coordinate c) {
        /*
            When choosing a Direction from a coordinate, it can be one of the 8 surrounding coordinates. Direction
            0 is directly up, 1 is to the top right, 2 is to the right and so on until Direction 7 is to the top
            left. A coordinate can also not move which will add -1 to the Direction list (this is always possible)
         */
        Direction[] possibleDirections; // If unobstructed then all are available.
        // Top left check
        if (coordinateTouchingLeftEdge(c) && coordinateTouchingTopEdge(c)) {
            possibleDirections = new Direction[]{Direction.NONE, Direction.RIGHT, Direction.DOWNRIGHT, Direction.DOWN}; // Can only move right, right down, down.
        // Top right check
        } else if (coordinateTouchingTopEdge(c) && coordinateTouchingRightEdge(c)) {
            possibleDirections = new Direction[]{Direction.NONE, Direction.DOWN, Direction.DOWN_LEFT, Direction.LEFT}; // Can only move down, left down, left.
        // Bottom right check
        } else if (coordinateTouchingRightEdge(c) && coordinateTouchingBottomEdge(c)) {
            possibleDirections = new Direction[]{Direction.NONE, Direction.UP, Direction.LEFT, Direction.UP_LEFT}; // Can only move up, left, up left.
        // Bottom left check
        } else if (coordinateTouchingLeftEdge(c) && coordinateTouchingBottomEdge(c)) {
            possibleDirections = new Direction[]{Direction.NONE, Direction.UP, Direction.UPRIGHT, Direction.RIGHT}; // Can only move up, up right, right.
        // Top edge check
        } else if (coordinateTouchingTopEdge(c)) {
            possibleDirections = new Direction[]{Direction.NONE, Direction.RIGHT, Direction.DOWNRIGHT, Direction.DOWN, Direction.DOWN_LEFT, Direction.LEFT};
        // Right edge check
        } else if (coordinateTouchingRightEdge(c)) {
            possibleDirections = new Direction[]{Direction.NONE, Direction.UP, Direction.DOWN, Direction.DOWN_LEFT, Direction.RIGHT, Direction.UPRIGHT};
        // Bottom edge check
        } else if (coordinateTouchingBottomEdge(c)) {
            possibleDirections = new Direction[]{Direction.NONE, Direction.UP, Direction.UPRIGHT, Direction.RIGHT, Direction.LEFT, Direction.UP_LEFT};
        // Left edge check
        } else if (coordinateTouchingLeftEdge(c)) {
            possibleDirections = new Direction[]{Direction.NONE, Direction.UP, Direction.UPRIGHT, Direction.RIGHT, Direction.DOWNRIGHT, Direction.DOWN};
        // Not obstructed
        } else {
            possibleDirections = new Direction[]{Direction.NONE, Direction.UP, Direction.UPRIGHT, Direction.RIGHT, Direction.DOWNRIGHT, Direction.DOWN, Direction.DOWN_LEFT, Direction.LEFT, Direction.UP_LEFT};
        }
        return possibleDirections;
    }

    /**
     * Shifts an input coordinate in one unit in a given Direction.
     * @param c The coordinate to adjust.
     * @param direction The Direction in which to adjust the coordinate.
     */
    private void moveCoordinateAStep(Coordinate c, Direction direction) {
        switch(direction) {
            case NONE:
                // The coordinate did not move - do nothing
                break;
            case UP: // Moving straight up
                c.setY(c.getY() - 1);
                break;
            case UPRIGHT: // Moving up right
                c.setX(c.getX() + 1);
                c.setY(c.getY() - 1);
                break;
            case RIGHT: // Moving right
                c.setX(c.getX() + 1);
                break;
            case DOWNRIGHT: // Moving down right
                c.setX(c.getX() + 1);
                c.setY(c.getY() + 1);
                break;
            case DOWN: // Moving down
                c.setY(c.getY() + 1);
                break;
            case DOWN_LEFT: // Moving down left
                c.setX(c.getX() - 1);
                c.setY(c.getY() + 1);
                break;
            case LEFT: // Moving left
                c.setX(c.getX() - 1);
                break;
            case UP_LEFT: // Moving up left
                c.setX(c.getX() - 1);
                c.setY(c.getY() - 1);
                break;
        }
    }

    /**
     * Sets the @code{movementSpeed} attribute to a new value.
     * @param movementSpeed The new value to assign the @code{movementSpeed} attribute.
     */
    public void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    /**
     * Sets the value of the @code{coordinateVelocities} attribute to a new value.
     * @param coordinateVelocities The new value to assign the @code{coordinateVelocities} attribute.
     */
    public void setCoordinateVelocities(Vector[] coordinateVelocities) {
        this.coordinateVelocities = coordinateVelocities;
    }
}
