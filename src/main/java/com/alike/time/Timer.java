package com.alike.time;

import com.alike.solution_helpers.RepeatedFunctions;

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
    private void time(int timerLength) {
        this.timing = true;
        Thread t = new Thread(() -> {
            runTimer(timerLength);
        });
        t.start();
    }

    /**
     * Runs the timer for a specified amount of milliseconds.
     * @param timerLength The number of millisecons the timer should run for.
     * @param inInternalThread If the timer should time in the thread it's constructed in or if it should time in a
     *                         thread created inside this object (used for different things). When true, the thread in
     *                         which the Timer object is created can continue running while the timer is timing; if
     *                         false then the outcome will be making the thread the time() method is called in sleep().
     */
    public void time(int timerLength, boolean inInternalThread) {
        this.timing = true;
        if (inInternalThread) {
            time(timerLength);
        } else {
            runTimer(timerLength);
        }
    }

    /**
     * Returns the value of the @code{timing} attribute.
     * @return timing The value of the @code{timing} attribute.
     */
    public boolean isTiming() {
        return this.timing;
    }

    /**
     * Used internally to make the thread its called in sleep for the input amount of time.
     * @param milliseconds The number of milliseconds which the thread should sleep for.
     */
    private void runTimer(int milliseconds) {
        RepeatedFunctions.sleep(milliseconds);
        this.timing = false;
    }
}
