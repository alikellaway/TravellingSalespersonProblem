package com.alike.solvers;

import com.alike.graphsystem.DynamicGraph;
import com.alike.solution_helpers.RepeatedFunctions;
import com.alike.solution_helpers.Timer;
import com.alike.solvertestsuite.DynamicSolution;
import com.alike.solvertestsuite.SolverOutput;
import com.alike.solvertestsuite.Stopwatch;
import com.alike.graphsystem.StaticGraph;

public class DynamicAntColonyOptimisationSolver implements DynamicSolver {
    /**
     * The dynamic graph through which graph interaction will occur.
     */
    private DynamicGraph dgraph;

    /**
     * A reference to the underlying graph construct of the @code{dgraph} (for easy code writing in this class).
     */
    private StaticGraph graph;

    /**
     * A reference to an @code{AntColonyOptimisationSolver} that will be used to repeatedly solve the @code{dgraph}.
     */
    private final AntColonyOptimisationSolver acos;

    /**
     * A boolean describing whether this solver is currently solving a @code{dgraph} or not. (true if solving)
     */
    private volatile boolean running;

    /**
     * The number of ants sent per solve of the dynamic graph.
     */
    private final int numAntsPerSolve = 100;

    /**
     * The object used for recording execution times in this class.
     */
    private final Stopwatch stopwatch = new Stopwatch();

    /**
     * Initialises a new @code{DynamicAntColonyOptimisationSolver} object.
     * @param dgraph The @code{DynamicGraph} object this object will be used to solve.
     */
    public DynamicAntColonyOptimisationSolver(DynamicGraph dgraph) {
        acos = new AntColonyOptimisationSolver(dgraph.getUnderlyingGraph());
        setGraph(dgraph);
        acos.setDelayPerStep(0);
        // Set you values to allow the path to react quickly.
        /*acos.setAlpha(0.02);
        acos.setBeta(11.5);
        acos.setRh0(0.9);
        acos.setQ(0.0004);*/
    }

    public DynamicAntColonyOptimisationSolver() {
        acos = new AntColonyOptimisationSolver();
        acos.setDelayPerStep(0);
    }

    /**
     * Continuously solves the dgraph until @code{running} is changed to false.
     * @param delayPerSolve The time to wait between each solve.
     * @return DynamicSolution A @code{SolverOutput} object containing information about the solution.
     */
    @Override
    public SolverOutput startSolving(int delayPerSolve) {
        dgraph.move();
        running = true;
        stopwatch.clear();
        long totalTime = 0;
        int numSolves = 0;
        while (running) {
            dgraph.stop();
            stopwatch.start();
            // Recalculate edge lengths so the ant has an accurate representation.
            graph.constructEdgeLengthMatrix(); // Reconstruct the edge matrix, since it has changed.
            acos.setDistanceMatrix(graph.getEdgeLengthMatrix());
            // Run the ants
            acos.sendAnts(numAntsPerSolve);
            totalTime += stopwatch.getTimeNs(); // Also stops the stopwatch
            stopwatch.clear(); // Eradicate the values from the watch.
            numSolves++;
            dgraph.move();
            RepeatedFunctions.sleep(delayPerSolve);
        }
        acos.getExecutorService().shutdown();
        return new DynamicSolution(dgraph.getAverageRouteLength(), totalTime/numSolves);
    }

    /**
     * Allows user to specify an amount of time for this solver to repeatedly solve its dgraph.
     * @param runTime The amount of milliseconds for which this solver should repeatedly solve.
     * @param delayPerSolve The time the solver should wait between each solve.
     * @return DynamicSolution The result of the solver running over that period of time.
     */
    @Override
    public SolverOutput solveForTime(int runTime, int delayPerSolve) {
        Thread th = new Thread(() -> {
            new Timer().time(runTime, false); // Makes this thread wait until the timer is up
            running = false;
        });
        th.start();
        return startSolving(delayPerSolve);
    }

    /**
     * Begins the solver solving the @code{dgraph} until it has solved the graph the input number of times.
     * @param numSolves The number of times the graph should be solved.
     * @param delayPerSolve The time delay between each solve.
     * @return DynamicSolution The information about the dynamic solver's solving.
     * @throws IllegalArgumentException
     */
    @Override
    public DynamicSolution calculateSolutions(int numSolves, int delayPerSolve) throws IllegalArgumentException {
        if (numSolves == 0) {
            throw new IllegalArgumentException("Cannot complete 0 or fewer solves.");
        }
        dgraph.move();
        running = true;
        stopwatch.clear();
        long totalTime = 0;
        int completedSolves = 0;
        while (completedSolves < numSolves) {
            dgraph.stop();
            stopwatch.start();
            // Recalculate edge lengths so the ant has an accurate representation.
            graph.constructEdgeLengthMatrix(); // Reconstruct the edge matrix, since it has changed.
            acos.setDistanceMatrix(graph.getEdgeLengthMatrix());
            // Run the ants
            acos.sendAnts(numAntsPerSolve); // Cannot use runSolution due to shutdownNow call.
            totalTime += stopwatch.getTimeNs(); // Also stops the stopwatch
            stopwatch.clear(); // Eradicate the values from the watch.
            completedSolves++;
            dgraph.move();
            RepeatedFunctions.sleep(delayPerSolve);
        }
        return new DynamicSolution(dgraph.getAverageRouteLength(), totalTime/completedSolves);
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
     * Sets the @code{dgraph} attribute to a new value.
     * @param dgraph The new value to assign the @code{dgraph} attribute.
     */
    @Override
    public void setGraph(DynamicGraph dgraph) {
        this.dgraph = dgraph;
        setGraph(dgraph.getUnderlyingGraph());
        getDgraph().wake();
    }

    /**
     * Sets the @code{graph} attribute to a new value.
     * @param graph The new value to assign the @code{graph} attribute.
     */
    public void setGraph(StaticGraph graph) {
        this.graph = graph;
        acos.setGraph(graph);
    }

    /**
     * Stops the solution from running by setting the @code{running} attribute to false. And calls shutdown on the
     * thread pool.
     */
    public void kill() {
        this.running = false;
        acos.getExecutorService().shutdownNow();
    }
}
