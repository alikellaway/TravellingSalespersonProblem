package com.alike.dynamicsolvers;

import com.alike.customexceptions.NoNodeContainerException;
import com.alike.dynamicgraphsystem.DynamicGraph;
import com.alike.solution_helpers.Ant;
import com.alike.solution_helpers.RepeatedFunctions;
import com.alike.solution_helpers.Timer;
import com.alike.solvers.AntColonyOptimizationSolver;
import com.alike.solvers.DynamicSolver;
import com.alike.solvertestsuite.DynamicSolution;
import com.alike.solvertestsuite.SolverOutput;
import com.alike.staticgraphsystem.StaticGraph;

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
     * A reference to an @code{AntColonyOptimizationSolver} that will be used to repeatedly solve the @code{dgraph}.
     */
    private final AntColonyOptimizationSolver acos;

    /**
     * A boolean describing whether this solver is currently solving a @code{dgraph} or not. (true if solving)
     */
    private volatile boolean running;

    /**
     * Initialises a new @code{DynamicAntColonyOptimisationSolver} object.
     * @param dgraph The @code{DynamicGraph} object this object will be used to solve.
     */
    public DynamicAntColonyOptimisationSolver(DynamicGraph dgraph) {
        setDgraph(dgraph);
        setGraph(dgraph.getUnderlyingGraph());
        getDgraph().wake();
        acos = new AntColonyOptimizationSolver(graph);
        // Set you values to allow the path to react quickly.
        acos.setAlpha(0.02);
        acos.setBeta(11.5);
        acos.setRh0(0.9);
        acos.setQ(0.0004);
    }

    /**
     * Method called to make this solver object solve it's dgraph until the @code{running} attribute is turned to false.
     * @param delayPerSolve The delay between each solve.
     */
//    public void runSolution(int delayPerSolve) {
//        acos.setDelayPerStep(0);
//        dgraph.move();
//        running = true;
//        while (running) {
//            // Recalculate edge lengths so the ant has an accurate representation.
//            dgraph.stop();
//            try {
//                graph.constructEdgeLengthMatrix();
//                acos.setDistanceMatrix(graph.getEdgeLengthMatrix());
//            } catch (NoNodeContainerException e) {
//                e.printStackTrace();
//            }
//            dgraph.move();
//            // Add the new ant
//            acos.getExecutorCompletionService().submit(new Ant(acos));
//            acos.setActiveAnts(acos.getActiveAnts() + 1);
//            // See if the ant found a shorter route.
//            acos.processAnts();
//            RepeatedFunctions.sleep(delayPerSolve);
//        }
//    }
    public void runSolution(int delayPerSolve) {
        acos.setDelayPerStep(0);
        dgraph.move();
        running = true;
        while (running) {
            try {
                dgraph.stop(); // Stop the graph moving
                // Update the distances for the ant about to go.
                graph.constructEdgeLengthMatrix();
                acos.setDistanceMatrix(graph.getEdgeLengthMatrix());
                // Send the ant
                acos.sendAnt();
                dgraph.move(); // Resume movement
                RepeatedFunctions.sleep(delayPerSolve);
            } catch (NoNodeContainerException e) {
                e.printStackTrace();
            }
        }
    }
//    public void runSolution(int delayPerSolve) {
//        acos.setDelayPerStep(0);
//        dgraph.move();
//        running = true;
//        while (running) {
//            // Recalculate edge lengths so the ant has an accurate representation.
//            dgraph.stop();
//            solve();
//            dgraph.move();
//
//        }
//    }

    /**
     * Used to actually run solution.
     */
    private void solve() {
        try {
            graph.constructEdgeLengthMatrix();
            acos.setDistanceMatrix(graph.getEdgeLengthMatrix());
            acos.getExecutorCompletionService().submit(new Ant(acos));
            acos.setActiveAnts(acos.getActiveAnts() + 1);
            // See if the ant found a shorter route.
            acos.processAnts();
        } catch (NoNodeContainerException e) {
            e.printStackTrace();
        }
    }

    public SolverOutput runSolution(int runTime, int delayPerStep) {
        acos.setDelayPerStep(delayPerStep);
        dgraph.move();
        Timer t = new Timer();
        t.time(runTime);
        while (t.isTiming()) {
            // Recalculate edge lengths so the ant has an accurate representation.
            dgraph.stop();
            try {
                graph.constructEdgeLengthMatrix();
                acos.setDistanceMatrix(graph.getEdgeLengthMatrix());
            } catch (NoNodeContainerException e) {
                e.printStackTrace();
            }
            dgraph.move();
            // Add the new ant
            acos.getExecutorCompletionService().submit(new Ant(acos));
            acos.setActiveAnts(acos.getActiveAnts() + 1);
            // See if the ant found a shorter route.
            acos.processAnts();
            RepeatedFunctions.sleep(delayPerStep);
        }
        return new DynamicSolution(dgraph.getAverageRouteLength(), runTime);
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
    }

    /**
     * Sets the @code{graph} attribute to a new value.
     * @param graph The new value to assign the @code{graph} attribute.
     */
    public void setGraph(StaticGraph graph) {
        this.graph = graph;
    }

    /**
     * Stops the solution from running by setting the @code{running} attribute to false. And calls shutdown on the
     * thread pool.
     */
    public void kill() {
        acos.getExecutorService().shutdown();
        this.running = false;
    }
}
