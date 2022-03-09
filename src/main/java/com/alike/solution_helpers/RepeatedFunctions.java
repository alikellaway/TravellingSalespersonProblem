package com.alike.solution_helpers;

import com.alike.customexceptions.InvalidGraphException;
import com.alike.staticgraphsystem.Graph;

/**
 * Class contains some useful functions that are repeated multiple times in the project (or have no obvious space).
 * @author alike
 */
public class RepeatedFunctions {

    /**
     * Outputs a boolean describing whether the input number was a power of two or not.
     * @param num The number we are checking.
     * @return boolean true if the number is a power of 2, false if not.
     */
    public static boolean isPowerOfTwo(double num) {
        while (num > 1) {
            num = num / 2;
        }
        return num == 1.00;
    }

    /**
     * Used to sleep the thread that executes it.
     * @param milliseconds The amount of time in milliseconds that the thread should sleep for.
     */
    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to validate whether a graph is legal by throwing an exception when an input graph is not.
     * @param graph The graph to validate.
     */
    public static void validateGraph(Graph graph) {
        try {
            if (graph.getNumNodes() < 3) {
                throw new InvalidGraphException("The graph had fewer than 3 nodes.");
            }
        } catch (InvalidGraphException e) {
            e.printStackTrace();
        }
    }
}