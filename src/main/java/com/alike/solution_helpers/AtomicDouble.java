package com.alike.solution_helpers;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicDouble extends Number implements Comparable<AtomicDouble> {


    private final AtomicReference<Double> atomicReference;

    public AtomicDouble(Double val) {
        atomicReference = new AtomicReference<>(val);
    }

    @Override
    public int compareTo(AtomicDouble other) {
        return Double.compare(this.doubleValue(), other.doubleValue());
    }

    public boolean compareAndSet(double updatedValue) {
        return atomicReference.compareAndSet(atomicReference.get(), updatedValue);
    }

    @Override
    public int intValue() {
        return atomicReference.get().intValue();
    }

    @Override
    public long longValue() {
        return atomicReference.get().longValue();
    }

    @Override
    public float floatValue() {
        return atomicReference.get().floatValue();
    }

    @Override
    public double doubleValue() {
        return atomicReference.get();
    }
}
