package com.alike.solution_helpers;

import com.alike.customexceptions.InvalidGraphException;
import com.alike.staticgraphsystem.Graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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

    /**
     * Returns whether an iterable contains duplicate elements in it.
     * @param i The iterable object.
     * @param <E> The type in the iterable object.
     * @return boolean True if the iterable contained duplicate elements.
     */
    public static <E> boolean hasDuplicates(Iterable<E> i) {
        HashSet<E> unique = new HashSet<>();
        for (E e : i) {
            if (!unique.add(e)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether an array contains duplicate elements in it.
     * @param a The array object to check for duplicates.
     * @param <E> The type stored in the array object.
     * @return boolean True if the array  contained duplicate elements.
     */
    public static <E> boolean hasDuplicates(E[] a) {
        HashSet<E> unique = new HashSet<>();
        for (E e : a) {
            if (!unique.add(e)) {
                return true;
            }
        }
        return false;
    }
}