package com.alike.dynamicsolvers;

import com.alike.dynamicgraphsystem.DynamicGraph;
import com.alike.solution_helpers.RepeatedFunctions;
import com.alike.solution_helpers.Timer;
import com.alike.solvers.DynamicSolver;
import com.alike.solvers.HilbertFractalCurveSolver;
import com.alike.solvertestsuite.DynamicSolution;
import com.alike.solvertestsuite.SolverOutput;
import com.alike.staticgraphsystem.StaticGraph;

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

    public DynamicHilbertFractalCurveSolver(DynamicGraph dgraph) {
        setGraph(dgraph.getUnderlyingGraph());
        setGraph(dgraph);
        setHfcs(new HilbertFractalCurveSolver(getGraph()));
    }

    public void runSolution(int delayPerSolve) {
        dgraph.wake(); // Listen for start stop commands.
        setRunning(true); // Set this object into the 'solving' state.
        while (running) {
            try {
                dgraph.stop();
                hfcs.runSolution(0);
                dgraph.move();
                RepeatedFunctions.sleep(delayPerSolve);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public SolverOutput runSolution(int runTime, int delayPerSolve) {
        dgraph.wake(); // Listen for start stop commands.
        Timer t = new Timer();
        t.time(runTime);
        while (t.isTiming()) {
            try {
                dgraph.stop();
                hfcs.runSolution(0);
                dgraph.move();
                RepeatedFunctions.sleep(delayPerSolve);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new DynamicSolution(dgraph.getAverageRouteLength(), runTime);
    }

    public HilbertFractalCurveSolver getHfcs() {
        return hfcs;
    }

    public void setHfcs(HilbertFractalCurveSolver hfcs) {
        this.hfcs = hfcs;
    }

    public DynamicGraph getDgraph() {
        return dgraph;
    }

    public StaticGraph getGraph() {
        return graph;
    }

    @Override
    public void setGraph(DynamicGraph dgraph) {
        this.dgraph = dgraph;
    }

    public void setGraph(StaticGraph graph) {
        this.graph = graph;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void kill() {
        setRunning(false);
    }
}
