package com.alike.solvertestsuite;

/**
 * Class can be used to measure execution time of other code by calling start and stop methods, similar to how a
 * stop watch works in real life.
 * @author alike
 */
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
     * Initialises a new stop watch object.
     */
    public Stopwatch() {}

    /**
     * Creates a new stop watch that is starting if the input boolean is true.
     * @param startNow Whether this stop watch should be started immediately.
     */
    public Stopwatch(boolean startNow) {
        if (startNow) {
            start();
        }
    }

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
        } // Only set a stop time if we have a start time.
    }

    /**
     * Stops the stopwatch if not already stopped and returns the time this stop watch was running for in nano-seconds.
     * @return long The number of nano-seconds this stop watch was running for.
     */
    public long getTimeNs() {
        if (stopTime != invalid){
            return stopTime - startTime;
        } else if (isStarted()) {
            stop();
            return getTimeNs();
        } else {
            return invalid;
        }
    }

    /**
     * Stops the stopwatch if not already stopped and returns the time this stop watch was running for in
     * micro-seconds.
     * @return micro-seconds The time elapsed in micro-seconds.
     */
    public long getTimeMus() {
        long val = getTimeNs();
        if (val != invalid) {
            return getTimeNs()/1000;
        }
        return val;
    }

    /**
     * Stops the stopwatch if not already stopped and returns the time this stop watch was running for in milliseconds.
     * @return milliseconds The time elapsed in milliseconds.
     */
    public long getTimeMs() {
        long val = getTimeMus();
        if (val != invalid) {
            return getTimeMus()/1000;
        }
        return val;
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

    /**
     * Used to reset the stopwatch (so it might be used again)
     */
    public void clear() {
        setStartTime(invalid);
        setStopTime(invalid);
    }

    /**
     * Returns a boolean that is true if the @code{start()} method has been called.
     * @return boolean true if the stop watch has been started.
     */
    public boolean isStarted() {
        return startTime != invalid;
    }
}
