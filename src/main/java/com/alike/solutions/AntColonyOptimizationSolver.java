package com.alike.solutions;

import com.alike.solution_helpers.Ant;
import com.alike.solution_helpers.AtomicDouble;
import com.alike.tspgraphsystem.TSPEdgeContainer;
import com.alike.tspgraphsystem.TSPGraph;
import com.alike.tspgraphsystem.TSPNode;
import javafx.util.Pair;

import java.util.Random;
import java.util.concurrent.ExecutionException;
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

    private static final double PROCESSING_CYCLE_PROBABILITY = 0.8;

    private TSPEdgeContainer shortestRoute;

    private AtomicDouble[][] pheromoneLevelMatrix;
    private double[][] distanceMatrix;

    private ExecutorService executorService;

    private ExecutorCompletionService<Ant> executorCompletionService;

    private int activeAnts = 0;

    private int delayPerStep;

    public AntColonyOptimizationSolver(TSPGraph graph) {
        setGraph(graph);
        setExecutorService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
        setExecutorCompletionService(new ExecutorCompletionService<>(getExecutorService()));
        initialiseDistances();
        initialisePheremoneLevels();
    }

    public Pair<TSPGraph, Double> runSolution(int numAnts, int delayPerStep) {
        setDelayPerStep(delayPerStep);
        // Activate all ants
        for (int x = 0; x < numAnts; x++) {
            executorCompletionService.submit(new Ant(this));
            activeAnts++;
            if (Math.random() > PROCESSING_CYCLE_PROBABILITY) {
                processAnts();
            }
        }
        processAnts();
        getExecutorService().shutdown();
        System.out.println("All " + numAnts + " Ants have finished traversing!");
        return new Pair<>(graph, graph.getEdgeContainer().getTotalLength());
    }

    private void processAnts() {
        while (activeAnts > 0) {
            try {
                Ant ant = getExecutorCompletionService().take().get();
                TSPEdgeContainer currentRoute = ant.getRoute();
                if (shortestRoute == null || currentRoute.getTotalLength()
                                                        < shortestRoute.getTotalLength()) {
                    shortestRoute = currentRoute;
                    System.out.println(shortestRoute.getTotalLength() + " : " + ant.getAntID());
                    graph.setEdgeContainer(shortestRoute);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            activeAnts--;
        }
    }

    public void setGraph(TSPGraph graph) {
        this.graph = graph;
    }

    public TSPGraph getGraph() {
        return this.graph;
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

    private void initialiseDistances() {
        int numNodes = graph.getNumNodes();
        distanceMatrix = new double[numNodes][numNodes];
        for (int x = 0; x < numNodes; x++) {
            TSPNode n1 = graph.getNodeContainer().getNodeSet().get(x);
            for (int y = 0; y < numNodes; y++) {
                TSPNode n2 = graph.getNodeContainer().getNodeSet().get(y);
                distanceMatrix[x][y] = n1.getVectorTo(n2).magnitude();
            }
        }
    }

    private void initialisePheremoneLevels() {
        int numNodes = graph.getNumNodes();
        pheromoneLevelMatrix = new AtomicDouble[numNodes][numNodes];
        Random r = new Random();
        for (int x = 0; x < numNodes; x++) {
            for (int y = 0; y < numNodes; y++) {
                pheromoneLevelMatrix[x][y] = new AtomicDouble(r.nextDouble());
            }
        }
    }

    public double[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    public AtomicDouble[][] getPheromoneLevelMatrix() {
        return pheromoneLevelMatrix;
    }

    public int getDelayPerStep() {
        return delayPerStep;
    }

    public void setDelayPerStep(int delayPerStep) {
        this.delayPerStep = delayPerStep;
    }
}
