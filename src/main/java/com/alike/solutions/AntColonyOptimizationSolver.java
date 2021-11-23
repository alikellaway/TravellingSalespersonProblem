package com.alike.solutions;

import com.alike.solution_helpers.Ant;
import com.alike.tspgraphsystem.TSPGraph;

import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class uses the Ant colony optimisation algorithm to solve an optimised route through a TSPGraph object.
 * @author alike
 */
public class AntColonyOptimizationSolver {
    /**
     * The graph object this solver will be solving.
     */
    private TSPGraph graph;

    /**
     * The number of ants that will traverse the graph to be solved by this solver class.
     */
    private final int numberOfAnts = 500;

    private ExecutorService executorService;

    private ExecutorCompletionService<Ant> executorCompletionService;

    private int activeAnts = 0;

    public AntColonyOptimizationSolver(TSPGraph graph) {
        setGraph(graph);
        setExecutorService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
        setExecutorCompletionService(new ExecutorCompletionService<>(getExecutorService()));
    }

    public void runSolution() {
        // Activate all ants
        for (int x = 0; x < numberOfAnts; x++) {
            executorCompletionService.submit(new Ant());
            activeAnts++;
            while (activeAnts > 0) {
                try {
                    getExecutorCompletionService().take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                activeAnts--;
            }
        }
        getExecutorService().shutdown();
    }

    public void setGraph(TSPGraph graph) {
        this.graph = graph;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public ExecutorCompletionService<Ant> getExecutorCompletionService() {
        return executorCompletionService;
    }

    public void setExecutorCompletionService(ExecutorCompletionService<Ant> executorCompletionService) {
        this.executorCompletionService = executorCompletionService;
    }
}
