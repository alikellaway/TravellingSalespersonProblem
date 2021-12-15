package com.alike.dtspgraphsystem;

import com.alike.Main;
import com.alike.tspgraphsystem.Coordinate;

import java.util.ArrayList;
import java.util.Random;

/**
 * CoordinateMover class is used to move a coordinate along/up/down one value depending on the method called.
 */
public class CoordinateMover {

    private ArrayList<Coordinate> coordinates;

    private int movementSpeed;

    private Coordinate[] coordinateVelocities = null;

    private Random rand;

    public CoordinateMover(ArrayList<Coordinate> coordinates) {
        setCoordinates(coordinates);
        rand = new Random();
    }

    /**
     * Moves a coordinate to one of the surrounding coordinates (randomly). Coordinates will not hit the edge i.e. will
     * not overflow and will not bounce of the edge.
     */
    public void stepRandomly() {
        for (Coordinate coordinate : coordinates) {
            // Get the possible directions
            int[] possibleDirections = getPossibleDirections(coordinate);
            // Choose a random direction by choosing a random index and move the coordinate in that direction.
            moveCoordinateAStep(coordinate, possibleDirections[rand.nextInt(possibleDirections.length - 1)]);
        }
    }

    public void stepParticularly() {
        if (coordinateVelocities == null) {
            coordinateVelocities = new Coordinate[coordinates.size()];
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
        return coordinate.getY() == 0;
    }

    /**
     * Returns true if the input coordinate is touching the bottomm edge of the coordinate space i.e. y = Max
     * @param coordinate The coordinate to check.
     * @return boolean: True if the coordinate is touching the top edge.
     */
    private boolean coordinateTouchingBottomEdge(Coordinate coordinate) {
        return coordinate.getY() == Main.COORDINATE_MAX_HEIGHT;
    }

    /**
     * Returns true if the input coordinate is touching the left edge of the coordinate space i.e. x = 0
     * @param coordinate The coordinate to check.
     * @return boolean: True if the coordinate is touching the left edge.
     */
    private boolean coordinateTouchingLeftEdge(Coordinate coordinate) {
        return coordinate.getX() == 0;
    }

    /**
     * Returns true if the input coordinate is touching the right edge of the coordinate space i.e. x = Max
     * @param coordinate The coordinate to check.
     * @return boolean: True if the coordinate is touching the right edge of the coordinate space.
     */
    private boolean coordinateTouchingRightEdge(Coordinate coordinate) {
        return coordinate.getX() == Main.COORDINATE_MAX_WIDTH;
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

    private void moveCoordinateAStep(Coordinate currC, int directionInt) {
        switch(directionInt) {
            case -1:
                // The coordinate did not move - do nothing
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
}
