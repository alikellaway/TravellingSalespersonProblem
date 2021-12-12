package com.alike.tspgraphsystem;

import com.alike.customexceptions.NodeSuperimpositionException;

import java.util.ArrayList;
import java.util.Random;

/**
 * Used to represent coordinates.
 * @author alike
 */
public class Coordinate {
    /**
     * The x value for the coordinate.
     */
    private int x;
    /**
     * The y value for the coordinate.
     */
    private int y;

    /**
     * Initialises a new coordinate.
     * @param x The x value of the new coordinate.
     * @param y The y value of the new coordinate.
     */
    public Coordinate(int x, int y) {
        setX(x);
        setY(y);
    }

    /**
     * Used to get a vector from this coordinate to another coordinate.
     * @param otherCoordinate The coordinate object to find vector to.
     * @return Vector A new vector object describing the vector between this coordinate and another coordinate.
     */
    public Vector getVectorTo(Coordinate otherCoordinate) {
        double vectorX = otherCoordinate.getX() - getX();
        double vectorY = otherCoordinate.getY() - getY();
        return new Vector(vectorX, vectorY);
    }

    /**
     * Returns the value of the x attribute of the coordinate.
     * @return x The value of the x attribute of the coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the value of the x attribute to a new value.
     * @param x The new value to set the x attribute to.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Returns the value of the y attribute of the coordinate.
     * @return y The value of the y attribute of the coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the value of the y attribute to a new value.
     * @param y The new value to set the y attribute to.
     */
    public void setY(int y) {
        this.y = y;
    }



    /**
     * Used to represent the object as a string in terminal or any text format.
     * @return string The coordinate represented as a string.
     */
    @Override
    public String toString() {
        return "(" + getX() + "," + getY() + ")";
    }

    /**
     * Used to generate a random coordinate.
     * @param xMax The maximum value a coordinate can have in the x direction.
     * @param yMax The maximum value a coordinate can have in the y direction.
     * @return c A new coordinate with random x and y attribute values.
     */
    public static Coordinate generateRandomCoordinate(int xMax, int yMax) {
        Random r = new Random();
        return new Coordinate(r.nextInt(xMax), r.nextInt(yMax));
    }

    /**
     * Multiplies the coordinate values by some factor (like a PVector).
     * @param factor The factor by which to multiply.
     */
    public void mult(float factor) {
        setX((int) (getX()*factor));
        setY((int) (getY()*factor));
    }

    /**
     * Adds the parameter values to the coordinate components.
     * @param addX The value to add to X.
     * @param addY The value to add to Y.
     */
    public void add(float addX, float addY) {
        setX((int) (getX() + addX));
        setY((int) (getY() + addY));
    }

    /**
     * Used to check if two coordinate objects have the same x and y attribute values.
     * @param o The coordinate object we are checking against.
     * @return bool True if the coordinates have equal attribute values.
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) { // If o is self
            return true;
        }
        if (!(o instanceof Coordinate c)) {
            return false;
        } // If not a coordinate
        // If they have the same x and y values theyre equal
        return this.getX() == c.getX() && this.getY() == c.getY();
    }

    /**
     * Constructs a node container from a string output from the @code{toStorageFormat} method.
     * @param coordinateListString The string from which we are trying to parse a container.
     * @return nc The node container constructed using the information in the input string.
     * @throws NodeSuperimpositionException Thrown if an attempt is made to superimpose a node on another.
     */
    public static ArrayList<Coordinate> parseCoordinateList(String coordinateListString) {
        ArrayList<Coordinate> cL = new ArrayList<>();
        String[] cs = coordinateListString.split(";");
        for (String c : cs) {
            String[] values = c.split(",");
            int x = Integer.parseInt(values[0].substring(1));
            int y = Integer.parseInt(values[1].substring(0, values[1].length() - 1));
            cL.add(new Coordinate(x, y));
            System.out.println(x + "," + y);
        }
        return cL;
    }

    /**
     * Takes in an arraylist of coordinates and outputs a string that can be used to later reconstruct an equal
     * input coordinate list.
     * @return sfCl The storage format coordinate list.
     */
    public static String coordinateListToStorageFormat(ArrayList<Coordinate> cL) {
        StringBuilder sfCl = new StringBuilder(); // This will help us construct what we write
        for (Coordinate c : cL) {
            sfCl.append(c.toString()).append(";"); // Write each coordinate in
        }
        return sfCl.toString();
    }
}
