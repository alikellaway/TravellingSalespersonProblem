package com.alike.solvers;

import com.alike.solution_helpers.Ant;
import com.alike.solution_helpers.AtomicDouble;
import com.alike.solvertestsuite.Solution;
import com.alike.tspgraphsystem.TSPEdgeContainer;
import com.alike.tspgraphsystem.TSPGraph;
import com.alike.tspgraphsystem.TSPNode;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class uses the Ant colony optimisation algorithm to solve an optimised route through a TSPGraph object.
 * @author alike
 */
public class AntColonyOptimizationSolver implements Solver {

    /**
     * The graph object this solver will be solving.
     */
    private TSPGraph graph;

    private static final double PROCESSING_CYCLE_PROBABILITY = 0.8;

    /**
     * An edge container in which the shortest found route is stored.
     */
    private TSPEdgeContainer shortestRoute;

    /**
     * A matrix used to store the pheromones currently deposited on each edge between each node.
     */
    private AtomicDouble[][] pheromoneLevelMatrix;

    /**
     * A matrix used to store all the distances between each node in the graph.
     */
    private double[][] distanceMatrix;

    /**
     * A thread pool that will allow us to manage many Ant threads simultaneously.
     */
    private ExecutorService executorService;

    /**
     * The class used to register when the Ants have completed their journey. It places completed threads onto a queue
     * accessible through @code{take}.
     */
    private ExecutorCompletionService<Ant> executorCompletionService;

    /**
     * The number of Ant threads currently active (traversing the graph).
     */
    private int activeAnts = 0;

    /**
     * The time each ant will wait between moving from their current node to the next node (ms).
     */
    private int delayPerStep;

    /**
     * The number of Ants the solver will simulate.
     */
    private int  numAnts = 900;

    /**
     * Constructs a new Solver object which can run an ant colony optimisation solution implementation to find a route
     * through a TSP graph.
     * @param graph The graph the solver will solve when @code{runSolution} is called.
     */
    public AntColonyOptimizationSolver(TSPGraph graph) {
        setGraph(graph);
        setExecutorService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
        setExecutorCompletionService(new ExecutorCompletionService<>(getExecutorService()));
        initialiseDistances();
        initialisePheromoneLevels();
    }

    /**
     * An empty constructor so that we can set the graph at a later time.
     */
    public AntColonyOptimizationSolver() {
        setExecutorService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
        setExecutorCompletionService(new ExecutorCompletionService<>(getExecutorService()));
        initialiseDistances();
        initialisePheromoneLevels();
    }

    /**
     * Starts this solver object running the logic to find a route through the TSPGraph in the @code{graph} attribute.
     * @param delayPerStep The delay each Ant will take before moving nodes.
     * @return output The results of the solution attempt.
     */
    public Solution runSolution(int delayPerStep) {
        long startTime = System.nanoTime();
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
        getExecutorService().shutdownNow();
        System.out.println("All " + numAnts + " Ants have finished traversing!");
        long finishTime = System.nanoTime();
        return new Solution(graph, graph.getEdgeContainer().getTotalLength(), finishTime - startTime);
    }

    /**
     * While Ants are still traversing, this method will check if they are completed and gets the result to see if it
     * is smaller than the current shortest route.
     */
    private void processAnts() {
        while (activeAnts > 0) {
            try {
                Ant ant = executorCompletionService.take().get();
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

    /**
     * Sets the @code{graph} attribute to a new value.
     * @param graph The new value ot assign to the @code{graph} attribute.
     */
    public void setGraph(TSPGraph graph) {
        this.graph = graph;
    }

    /**
     * Returns the value of the @code{graph} attribute.
     * @return graph The value of the @code{graph} attribute.
     */
    public TSPGraph getGraph() {
        return this.graph;
    }

    /**
     * Returns the value of the @code{executorService} attribute.
     * @return executorService The value of the @code{executorService} attribute.
     */
    public ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * Sets the value of the @code{executorService} attribute to a new value.
     * @param executorService The new value to assign to the @code{executorService}.
     */
    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * Sets the value of the @code{executorCompletionService} to a new value.
     * @param executorCompletionService The new value to assign the @code{executorCompletionService}.
     */
    public void setExecutorCompletionService(ExecutorCompletionService<Ant> executorCompletionService) {
        this.executorCompletionService = executorCompletionService;
    }

    /**
     * Fills the distance matrix with the distance of each node to each other node.
     */
    private void initialiseDistances() {
        int numNodes = graph.getNumNodes(); // Find out how many nodes there are.
        distanceMatrix = new double[numNodes][numNodes]; // Create a square matrix using the numNodes as side length
        // For each node, check the distance to every other node.
        for (int x = 0; x < numNodes; x++) {
            TSPNode n1 = graph.getNodeContainer().getNodeSet().get(x);
            for (int y = 0; y < numNodes; y++) {
                TSPNode n2 = graph.getNodeContainer().getNodeSet().get(y);
                distanceMatrix[x][y] = n1.getVectorTo(n2).magnitude();
            }
        }
    }

    /**
     * Initialises each edge to have a random pheromone level.
     */
    private void initialisePheromoneLevels() {
        int numNodes = graph.getNumNodes();
        pheromoneLevelMatrix = new AtomicDouble[numNodes][numNodes];
        Random r = new Random();
        for (int x = 0; x < numNodes; x++) {
            for (int y = 0; y < numNodes; y++) {
                pheromoneLevelMatrix[x][y] = new AtomicDouble(r.nextDouble());
            }
        }
    }

    /**
     * Returns the value of the @code{distanceMatrix} attribute.
     * @return distanceMatrix The value of the @code{distanceMatrix} attribute.
     */
    public double[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    /**
     * Returns the value of the @code{pheromoneLevelMatrix} attribute.
     * @return pheromoneLevelMatrix The value of the @code{pheromoneLevelMatrix} attribute.
     */
    public AtomicDouble[][] getPheromoneLevelMatrix() {
        return pheromoneLevelMatrix;
    }

    /**
     * Returns the value of the @code{delayPerStep} attribute.
     * @return delayPerStep The value of the @code{delayPerStep} attribute.
     */
    public int getDelayPerStep() {
        return delayPerStep;
    }

    /**
     * Sets the value of the @code{delayPerStep} attribute.
     * @param delayPerStep The new value to assign to the @code{delayPerStep} attribute.
     */
    public void setDelayPerStep(int delayPerStep) {
        this.delayPerStep = delayPerStep;
    }

    /**
     * Sets the @code{numAnts} attribute to a new value.
     * @param numAnts The new value to assign to the @code{numAnts} attribute.
     */
    public void setNumAnts(int numAnts) {
        this.numAnts = numAnts;
    }
}
