package com.alike.tspgraphsystem;

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
}