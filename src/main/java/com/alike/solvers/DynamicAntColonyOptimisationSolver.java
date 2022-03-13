package com.alike.solvers;

import com.alike.customexceptions.NoNodeContainerException;
import com.alike.customexceptions.NonExistentNodeException;
import com.alike.dynamicgraphsystem.DynamicGraph;
import com.alike.solution_helpers.Ant;
import com.alike.solution_helpers.RepeatedFunctions;
import com.alike.staticgraphsystem.StaticGraph;

import java.lang.reflect.ReflectPermission;

public class DynamicAntColonyOptimisationSolver {

    private DynamicGraph dgraph;
    private StaticGraph graph;

    private final AntColonyOptimizationSolver acos;

    private volatile boolean running;

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

    public void runSolution(int delayPerStep) {
        acos.setDelayPerStep(delayPerStep);
        dgraph.move();
        running = true;
        while (running) {
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
    }

    /**
     * Returns the value of the @code{dgraph} attribute.
     * @return dgraph The value of the @code{dgraph} attribute.
     */
    public DynamicGraph getDgraph() {
        return dgraph;
    }

    /**
     * Sets the @code{dgraph} attribute to a new value.
     * @param dgraph The new value to assign the @code{dgraph} attribute.
     */
    public void setDgraph(DynamicGraph dgraph) {
        this.dgraph = dgraph;
    }

    /**
     * Returns the value of the @code{graph} attribute.
     * @return graph The value of the @code{graph} attribute.
     */
    public StaticGraph getGraph() {
        return graph;
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
