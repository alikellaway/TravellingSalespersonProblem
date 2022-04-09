package com.alike.graphsystem;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author alike
 * Class used to represent Vectors in code and holds relevant Vector manipulation logic.
 */
public class Vector {
    /**
     * The value of the x component of this Vector.
     */
    private double x;
    /**
     * The value of the y component of this Vector.
     */
    private double y;

    /**
     * Used to initialise a new Vector object given a new x and y component.
     * @param x The x component of the new Vector.
     * @param y The y component of the new Vector.
     */
    public Vector(double x, double y) {
        setX(x);
        setY(y);
    }

    /**
     * Used to calculate the magnitude of this Vector.
     * @return mag The magnitude of this Vector.
     */
    public double magnitude() {
        return Math.sqrt(x*x + y*y);
    }

    /**
     * Used to represent this Vector as a string.
     * @return String The Vector information layed out in a string.
     */
    @Override
    public String toString() {
        return "x = " + x + ", y = " + y;
    }

    /**
     * Returns value of the x attribute of this Vector.
     * @return x The value of the x attribute.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x attribute of this Vector to a new value.
     * @param x The new value to assign the
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Returns the value of the y attribute.
     * @return y The value of the y attribute.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the value of the y attribute to a new value.
     * @param y The new value to assign the y attribute.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Used to deduce whether this vector is horizontal i.e. has no Y component.
     * @return boolean true if the vector is horizontal.
     */
    public boolean isHorizontal() {
        return getY() == 0;
    }

    /**
     * Returns a new vector that is the normalization of this vector.
     * @return newVector A new Vector object with equal direction to this Vector object but a magnitude of 1.0.
     */
    public Vector getNormalized() {
        double mag = magnitude();
        return new Vector(getX()/mag, getY()/mag);
    }

    /**
     * Outputs a new vector reflecting the result of subtracting the input vector from this vector.
     * @param oV The other vecotr (vector to subtract).
     * @return new Vector A new vector - the result of this calculation - the input vector.
     */
    public Vector sub(Vector oV) {
        return new Vector(getX() - oV.getX(), getY() - oV.getY());
    }

    /**
     * Outputs a new vector reflecting the result of adding the input vector to this vector.
     * @param oV The other vector (vector to add).
     * @return new Vector A new vector - the result of this calculation - the input vector.
     */
    public Vector add(Vector oV) {
        return new Vector(getX() + oV.getX(), getY() + oV.getX());
    }

    /**
     * Inverts the x component of this vector.
     */
    public void invertX() {
        setX(getX() * -1);
    }

    /**
     * Inverts the y component of this vector.
     */
    public void invertY() {
        setY(getY() * -1);
    }

    /**
     * Inverts both components of this vector.
     */
    public void invert() {
        invertX();
        invertY();
    }

    /**
     * Returns a random vector that has a magnitude of the input magnitude by using a randomly generated angle and
     * parametric equations to get a vector with a magnitude of the radius of a circle. It then rounds these.
     * @param magnitude The required magnitude of the output vector.
     * @return Vector A new randomized vector that has a magnitude close to the input magnitude.
     */
    public static Vector randomVector(double magnitude) {
        double angle = ThreadLocalRandom.current().nextDouble(2.0 * Math.PI);
        double vX = Math.round(magnitude * Math.cos(angle));
        double vY = Math.round(magnitude * Math.sin(angle));
        return new Vector(vX, vY);
    }

    /**
     * Used to generate an array of specified length of random vectors with a specified magnitude.
     * @param num The number of vectors to generate.
     * @param magnitude The magnitude of each vector in the output.
     * @return vectors An array containing @code{num} randomized vectors of magnitude @code{magnitude}.
     */
    public static Vector[] randomVectors(int num, int magnitude) {
        Vector[] vectors = new Vector[num];
        for (int i = 0; i < num; i++) {
            vectors[i] = randomVector(magnitude);
        }
        return vectors;
    }

    /**
     * Returns the vector in the format "x,y" for storage - this saves a lot of () in the storage file and will
     * minorly help the read times for the coordinate list parser.
     * @return string The coordinate in storage format.
     */
    public String toStorageFormat() {
        return getX() + "," + getY();
    }

    /**
     * Parses a vector from a string if the data is in the format "x,y".
     * @param vectorString The string from which to parse a vector.
     * @return vector A new vector containing the values from the string.
     */
    public static Vector parseVector(String vectorString) {
        String[] xny = vectorString.split(",");
        return new Vector(Double.parseDouble(xny[0]), Double.parseDouble(xny[1]));
    }
}