package com.alike.tspgraphsystem;

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
}