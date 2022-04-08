package com.alike.solvertestsuite;

public class Stopwatch {
    /**
     * The value representing invalidity in this class.
     */
    private final long invalid = -1L;

    /**
     * A record of the time this Stopwatch was started.
     */
    private long startTime = invalid;

    /**
     * A record of the time this stop watch was stopped.
     */
    private long stopTime = invalid;

    /**
     * Creates a new Stopwatch object.
     */
    public Stopwatch() {}

    /**
     * Notes a start time for the stop watch.
     */
    public void start() {
        setStartTime(System.nanoTime());
    }

    /**
     * Notes an end time for the stop watch.
     */
    public void stop() {
        if (getStartTime() != invalid) {
            setStopTime(System.nanoTime());
        }
    }

    /**
     * Returns the time this stop watch was running for in nano-seconds.
     * @return long The number of nano-seconds this stop watch was running for.
     */
    public long getTimeNs() {
        return stopTime - startTime;
    }

    /**
     * Returns the value of the @code{startTime} attribute.
     * @return startTime The value of the @code{startTime} attribute.
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Assigns the value of the @code{startTime} attribute.
     * @param startTime The new value to assign the @code{startTime} attribute.
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * Returns the value of the @code{stopTime} attribute.
     * @return stopTime The value of the @code{stopTime} attribute.
     */
    public long getStopTime() {
        return stopTime;
    }

    /**
     * Assigns a new value to the @code{stopTime} attribute.
     * @param stopTime The new value to assign the @code{stopTime} attribute.
     */
    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }
}
