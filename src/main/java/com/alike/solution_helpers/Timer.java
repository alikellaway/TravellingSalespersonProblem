package com.alike.solution_helpers;

/**
 * Timer class used to indicate when a given time period has elapsed using an independant thread.
 */
public class Timer {
    /**
     * A boolean describing whether the timer is currently running.
     */
    private volatile boolean timing;

    /**
     * Initialises a new @code{Timer} object.
     */
    public Timer() {
    }

    /**
     * Begins a timer that times the given number of milliseconds before switching a Boolean.
     * @param timerLength The number of milliseconds which the timer should time during this run.
     */
    public void time(int timerLength) {
        this.timing = true;
        Thread t = new Thread(() -> {
            RepeatedFunctions.sleep(timerLength);
            this.timing = false;
        });
        t.start();
    }

    /**
     * Returns the value of the @code{timing} attribute.
     * @return timing The value of the @code{timing} attribute.
     */
    public boolean isTiming() {
        return this.timing;
    }
}
