package com.alike.graphsystem;

/**
 * An interface such that we can handle @code{StaticGraphs} and @code{DynamicGraphs} simultaneously.
 */
public interface Graph {
    /**
     * Returns the number of nodes in the graph.
     */
    int getNumNodes();

    /**
     * Outputs the graph's values as a string that can be stored and later read back into program memory.
     * @param delimiter The character used to separate graph information.
     */
    String toStorageFormat(char delimiter);
}
