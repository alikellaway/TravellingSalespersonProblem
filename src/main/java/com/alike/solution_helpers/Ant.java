package com.alike.solution_helpers;

import com.alike.tspgraphsystem.TSPEdgeContainer;

import java.util.concurrent.Callable;

public class Ant implements Callable<Ant> {

    private TSPEdgeContainer route;

    @Override
    public Ant call() {
        System.out.println("Initialised ant.");
        return null;
    }

    public TSPEdgeContainer getRoute() {
        return route;
    }
}
