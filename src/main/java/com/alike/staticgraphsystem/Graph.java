package com.alike.staticgraphsystem;

/**
 * An interface such that we can handle @code{StaticGraphs} and @code{DynamicGraphs} simultaneously.
 */
public interface Graph {
    /**
     * Returns the number of nodes in the graph.
     * @returns numNodes The number of nodes in the graph.
     */
    int getNumNodes();
}
