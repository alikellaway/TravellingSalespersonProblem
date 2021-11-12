package com.alike.tspgraphsystem;

public class Vector {

    private double x;
    private double y;

    public Vector(double x, double y) {
        setX(x);
        setY(y);
    }

    public Vector() {};

    public double magnitude() {
        return Math.sqrt(x*x + y*y);
    }

    @Override
    public String toString() {
        return "x = " + x + ", y = " + y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}