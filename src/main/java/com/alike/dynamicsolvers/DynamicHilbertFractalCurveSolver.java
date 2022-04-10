package com.alike.dynamicsolvers;

import com.alike.graphsystem.DynamicGraph;
import com.alike.solution_helpers.RepeatedFunctions;
import com.alike.solution_helpers.Timer;
import com.alike.solvers.HilbertFractalCurveSolver;
import com.alike.solvertestsuite.DynamicSolution;
import com.alike.solvertestsuite.SolverOutput;
import com.alike.solvertestsuite.Stopwatch;
import com.alike.graphsystem.StaticGraph;

public class DynamicHilbertFractalCurveSolver implements DynamicSolver {
    /**
     * A reference to the @code{HilbertFractalCurveSolver} object that will be repeatedly solving the graph.
     */
    private HilbertFractalCurveSolver hfcs;

    /**
     * The @code{DynamicGraph} that this object will be solving.
     */
    private DynamicGraph dgraph;

    /**
     * A reference to the underlying graph of the @code{dgraph} attribute.
     */
    private StaticGraph graph;

    /**
     * A boolean describing whether this solver is actively calculating solutions.
     */
    private volatile boolean running;

    /**
     * Constructs a new @code{DynamicHilberFractalCurveSolver} object.
     * @param dgraph The @code{DynamicGraph} object this solver will be solving or manipulating.
     */
    public DynamicHilbertFractalCurveSolver(DynamicGraph dgraph) {
        setGraph(dgraph.getUnderlyingGraph());
        setGraph(dgraph);
        setHfcs(new HilbertFractalCurveSolver(getGraph()));
    }

    /**
     * Continuously runs the solver until the @code{running} attribute is switched to false.
     * @param delayPerSolve The delay between each solve.
     */
    @Override
    public DynamicSolution startSolving(int delayPerSolve) {
        dgraph.wake(); // Listen for start stop commands.
        setRunning(true); // Set this object into the 'solving' state.
        long totalTime = 0;
        int numSolves = 0;
        Stopwatch watch = new Stopwatch();
        while (running) {
            try {
                dgraph.stop();
                watch.start();
                hfcs.runSolution(0);
                totalTime += watch.getTimeNs(); // Also stops the watch.
                numSolves++;
                watch.clear();
                dgraph.move();
                RepeatedFunctions.sleep(delayPerSolve);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new DynamicSolution(dgraph.getAverageRouteLength(), totalTime/numSolves);
    }

    /**
     * Runs the solver for the input number of milliseconds.
     * @param runTime The number of milliseconds the solver should be run for.
     * @param delayPerSolve The delay between each solve.
     * @return DynamicSolution An object containing information about the solver's activity.
     */
    @Override
    public SolverOutput solveForTime(int runTime, int delayPerSolve) {
        Thread th = new Thread(() -> {
            new Timer().time(runTime, false);
            kill();
        });
        th.start();
        return startSolving(delayPerSolve);
    }

    /**
     * Makes the solver calculate the input number of solutions to the @code{dgraph} before terminating.
     * @param numSolves The number of solutions the solver should find.
     * @param delayPerSolve The delay between each solve.
     * @return DynamicSolution A @code{SolverOutput} object containing information about the solves.
     */
    @Override
    public DynamicSolution calculateSolutions(int numSolves, int delayPerSolve) {
        if (numSolves == 0) {
            throw new IllegalArgumentException("Cannot complete 0 solves.");
        }
        dgraph.wake(); // Listen for start stop commands.
        setRunning(true); // Set this object into the 'solving' state (so its visible to other objects).
        long totalTime = 0; // The total time spent solving.
        Stopwatch watch = new Stopwatch(); // The tool used to record how much time each solve takes.
        int solvesCompleted = 0; // A counter to record how many solves have already been completed.
        while (solvesCompleted != numSolves) {
            try {
                dgraph.stop();
                watch.start();
                hfcs.runSolution(0);
                totalTime += watch.getTimeNs(); // Also stops the watch.
                numSolves++;
                watch.clear();
                dgraph.move();
                RepeatedFunctions.sleep(delayPerSolve);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setRunning(false);
        return new DynamicSolution(dgraph.getAverageRouteLength(), totalTime/numSolves);
    }

    /**
     * Returns the value of the @code{hfcs} attribute.
     * @return hfcs The value of the @code{hfcs} attribute.
     */
    public HilbertFractalCurveSolver getHfcs() {
        return hfcs;
    }

    /**
     * Assigns the value of the @code{hfcs} attribute.
     * @param hfcs The new value to assign the @code{hfcs} attribute.
     */
    public void setHfcs(HilbertFractalCurveSolver hfcs) {
        this.hfcs = hfcs;
    }

    /**
     * Returns the value of the @code{dgraph} attribute.
     * @return dgraph The value of the @code{dgraph} attribute.
     */
    public DynamicGraph getDgraph() {
        return dgraph;
    }

    /**
     * Returns the value of the @code{graph} attribute.
     * @return graph The value of the @code{graph} attribute.
     */
    public StaticGraph getGraph() {
        return graph;
    }

    /**
     * Assigns the value of the @code{dgraph} attribute given a dgraph.
     * @param dgraph The new value to assign the @code{dgraph} attribute.
     */
    @Override
    public void setGraph(DynamicGraph dgraph) {
        this.dgraph = dgraph;
    }

    /**
     * Assigns the value of the @code{graph} attribute given a graph.
     * @param graph The new value to assign the @code{graph} attribute.
     */
    public void setGraph(StaticGraph graph) {
        this.graph = graph;
    }

    /**
     * Returns the value of the @code{running} attribute.
     * @return running The value of the @code{running} attribute.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Assigns the value of the @code{running} attribute.
     * @param running The new value to assign the @code{running} attribute.
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Used to set halt the solver running by setting the @code{running} attribute to false.
     */
    public void kill() {
        setRunning(false);
    }
}
