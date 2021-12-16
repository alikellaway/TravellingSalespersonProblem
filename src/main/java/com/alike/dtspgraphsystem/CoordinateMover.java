package com.alike.dtspgraphsystem;

import com.alike.Main;
import com.alike.tspgraphsystem.Coordinate;
import com.alike.tspgraphsystem.Vector;

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

    private ArrayList<double[]> accruedPositionalError = null;

    /**
     * The random object used to generate the random components of the class.
     */
    private final Random rand = new Random();;


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
     * Moves a coordinate to one of the surrounding coordinates (randomly). Coordinates will not hit the edge i.e. will
     * not overflow and will not bounce of the edge.
     */
    private void stepRandomly(int speed) { // We can use recursion here to make the particle move more per tick.
        for (Coordinate coordinate : coordinates) {
            // Get the possible directions
            int[] possibleDirections = getPossibleDirections(coordinate);
            // Choose a random direction by choosing a random index and move the coordinate in that direction.
            int dirInt = possibleDirections[rand.nextInt(possibleDirections.length)];
            moveCoordinateAStep(coordinate, dirInt);
        }
        if (speed != 0) {
            stepRandomly(speed - 1);
        }
    }

    public void stepByVelocity() {
        // If the list of velocities is not yet populated then initialise it and fill it with random velocities.
        if (coordinateVelocities == null) {
            coordinateVelocities = new Vector[coordinates.size()];
            for (int i = 0; i < coordinates.size(); i++) {
                // Produces velocities with integer numbers
                coordinateVelocities[i] = generateRandomVector(movementSpeed); // This takes into account the speed.
            }
            // We can also assume the positional error was not initialised.
            accruedPositionalError = new ArrayList<>(); // Doubles are initialised as 0s.
            for (int i = 0; i < coordinates.size(); i++) {
                accruedPositionalError.add(new double[]{0.0, 0.0});
                System.out.println(coordinateVelocities[i]  + " " + accruedPositionalError.get(i)[0] + " " + accruedPositionalError.get(i)[1]);
            }
        }
        // Start the movement
        for (int i = 0; i < coordinates.size(); i++) { // Move each coordinate
            moveCoordinateByAVector(coordinates.get(i), coordinateVelocities[i]);
        }
    }

    private void moveCoordinateByAVector(Coordinate c, Vector v) {
        // Do the x component of the vector first.
        if (v.getX() < 0) { // The vector is moving the coordinate left.
            for (int x = 0; x < v.getX() * -1; x++) {
                moveCoordinateAStep(c, 6); // decrement x (move it left)
                // If we hit the left wall then we need to invert the velocity's x component and skip.
                if (c.getX() <= 0) {
                    v.invertX();
                    break;
                }
            }
        } else { // The vector is moving the coordinate right.
            for (int x = 0; x < v.getX(); x++) {
                moveCoordinateAStep(c, 2); // Increment x (move it right).
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
                moveCoordinateAStep(c, 0); // Decrement y (move it upwards).
                // If we hit the top wall then we need to invert the velocity's y component and skip.
                if (c.getY() <= 0) {
                    v.invertY();
                    break;
                }
            }
        } else { // The vector is moving the coordinate downwards.
            for (int y = 0; y < v.getY(); y++) {
                moveCoordinateAStep(c, 4); // Increment y (move it upwards).
                // If we hit the bottom wall then we need to invert the velocity's y component and skip.
                if (c.getY() >= Main.COORDINATE_MAX_HEIGHT) {
                    v.invertY();
                    break;
                }
            }
        }
    }








    public ArrayList<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Coordinate> coordinates) {
        this.coordinates = coordinates;
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
     * @return int[] A list of the possible directions.
     */
    private int[] getPossibleDirections(Coordinate c) {
        // Get a direction
            /*
                When choosing a direction from a coordinate, it can be one of the 8 surrounding coordinates. Direction
                0 is directly up, 1 is to the top right, 2 is to the right and so on until direction 7 is to the top
                left. A coordinate can also not move which will add -1 to the direction list (this is always possible)
             */
        int[] possibleDirections; // If unobstructed then all are available.
        // Top left check
        if (coordinateTouchingLeftEdge(c) && coordinateTouchingTopEdge(c)) {
            possibleDirections = new int[]{-1, 2, 3, 4}; // Can only move right, right down, down.
            // Top right check
        } else if (coordinateTouchingTopEdge(c) && coordinateTouchingRightEdge(c)) {
            possibleDirections = new int[]{-1, 4, 5, 6}; // Can only move down, left down, left.
            // Bottom right check
        } else if (coordinateTouchingRightEdge(c) && coordinateTouchingBottomEdge(c)) {
            possibleDirections = new int[]{-1, 0, 6, 7}; // Can only move up, left, up left.
            // Bottom left check
        } else if (coordinateTouchingLeftEdge(c) && coordinateTouchingBottomEdge(c)) {
            possibleDirections = new int[]{-1, 0, 1, 2}; // Can only move up, up right, right.
            // Top edge check
        } else if (coordinateTouchingTopEdge(c)) {
            possibleDirections = new int[]{-1, 2, 3, 4, 5, 6};
            // Right edge check
        } else if (coordinateTouchingRightEdge(c)) {
            possibleDirections = new int[]{-1, 0, 4, 5, 6, 7};
            // Bottom edge check
        } else if (coordinateTouchingBottomEdge(c)) {
            possibleDirections = new int[]{-1, 0, 1, 2, 6, 7};
            // Left edge check
        } else if (coordinateTouchingLeftEdge(c)) {
            possibleDirections = new int[]{-1, 0, 1, 2, 3, 4};
        } // Not obstructed
        else {
            possibleDirections = new int[]{-1, 0, 1, 2, 3, 4, 5, 6, 7};
        }
        return possibleDirections;
    }

    /**
     * Shifts an input coordinate in one unit in a given direction.
     * @param currC The coordinate to adjust.
     * @param directionInt The direction in which to adjust the coordinate.
     */
    private void moveCoordinateAStep(Coordinate currC, int directionInt) {
        switch(directionInt) {
            case -1:
                // The coordinate did not move - do nothing
                break;
            case 0: // Moving straight up
                currC.setY(currC.getY() - 1);
                break;
            case 1: // Moving up right
                currC.setX(currC.getX() + 1);
                currC.setY(currC.getY() - 1);
                break;
            case 2: // Moving right
                currC.setX(currC.getX() + 1);
                break;
            case 3: // Moving down right
                currC.setX(currC.getX() + 1);
                currC.setY(currC.getY() + 1);
                break;
            case 4: // Moving down
                currC.setY(currC.getY() + 1);
                break;
            case 5: // Moving down left
                currC.setX(currC.getX() - 1);
                currC.setY(currC.getY() + 1);
                break;
            case 6: // Moving left
                currC.setX(currC.getX() - 1);
                break;
            case 7: // Moving up left
                currC.setX(currC.getX() - 1);
                currC.setY(currC.getY() - 1);
                break;
        }
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    /**
     * Sets the @code{movementSpeed} attribute to a new value.
     * @param movementSpeed The new value to assign the @code{movementSpeed} attribute.
     */
    public void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    /**
     * Returns a random vector that has a magnitude of the input magnitude by using a randomly generated angle and
     * parametric equations to get a vector with a magnitude of the radius of a circle. It then rounds these.
     * @param magnitude The required magnitude of the output vector.
     * @return Vector A new randomized vector that has a magnitude close to the input magnitude.
     */
    private Vector generateRandomVector(double magnitude) {
        double angle = rand.nextDouble(2.0 * Math.PI);
        double vX = Math.round(magnitude * Math.cos(angle));
        double vY = Math.round(magnitude * Math.sin(angle));
        return new Vector(vX, vY);
    }
}
