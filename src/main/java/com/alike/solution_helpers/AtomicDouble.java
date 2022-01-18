package com.alike.solution_helpers;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Class used to allow Ant threads to manipulate doubles in the pheromone and probabilty matrices without concurrency
 * issues.
 */
public class AtomicDouble extends Number implements Comparable<AtomicDouble> {

    /**
     * The atomic reference used to create the atomic double object.
     */
    private final AtomicReference<Double> atomicReference;

    /**
     * Constructs a new AtomicDouble object.
     * @param val The starting value of the double.
     */
    public AtomicDouble(Double val) {
        atomicReference = new AtomicReference<>(val);
    }

    /**
     * Used to compare this AtomicDouble's value to another AtomicDouble's value atomically.
     * @param other The other AtomicDouble to compare this value to.
     * @return An integer: -1 if this is less than other, 0 if they're equal and 1 if this is bigger than other.
     */
    @Override
    public int compareTo(AtomicDouble other) {
        return Double.compare(this.doubleValue(), other.doubleValue());
    }

    /**
     * Atomically sets the value to the newValue if the current value is equal to the atomicReference's value.
     * @param newValue The new value to assign the AtomicDouble.
     * @return boolean True if successful and false if the actual value was not equal to the expected value.
     */
    public boolean compareAndSet(double newValue) {
        return atomicReference.compareAndSet(atomicReference.get(), newValue);
    }

    /**
     * Returns the value of this double as an integer (lossy).
     * @return Integer the value of the double cast to an integer.
     */
    @Override
    public int intValue() {
        return atomicReference.get().intValue();
    }

    /**
     * Converts the value of this AtomicDouble to a long and returns it.
     * @return Long The double value as a long.
     */
    @Override
    public long longValue() {
        return atomicReference.get().longValue();
    }

    /**
     * Converts the value of this AtomicDouble to a long and returns it.
     * @return Float The double value as a float.
     */
    @Override
    public float floatValue() {
        return atomicReference.get().floatValue();
    }

    /**
     * Returns the value of this AtomicDouble.
     * @return Double The value of this AtomicDouble.
     */
    @Override
    public double doubleValue() {
        return atomicReference.get();
    }
}
