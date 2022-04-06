package com.alike.solvers;

import com.alike.solution_helpers.Ant;
import com.alike.solution_helpers.AtomicDouble;
import com.alike.solvertestsuite.Fail;
import com.alike.solvertestsuite.Solution;
import com.alike.solvertestsuite.SolverOutput;
import com.alike.staticgraphsystem.EdgeContainer;
import com.alike.staticgraphsystem.Node;
import com.alike.staticgraphsystem.StaticGraph;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Class uses the Ant colony optimisation algorithm to solve an optimised route through a StaticGraph object.
 * @author alike
 */
public class AntColonyOptimizationSolver implements StaticSolver {

    /**
     * The graph object this solver will be solving.
     */
    private StaticGraph graph;

    /**
     * This number is used to dictate when we wait for all ants to finish before sending new ones.
     */
    public static final double PROCESSING_CYCLE_PROBABILITY = 0.8;

    /**
     * An edge container in which the shortest found route is stored.
     */
    private EdgeContainer shortestRoute;

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
     * Parameter used to adjust the amount of pheromone deposited per traversal (0<Q<1).
     */
    private double q = 0.0005; // tsp = 0.0005 dtsp = 0.0004

    /**
     * A parameter used for adjusting the amount of pheromone evaporation (0<RHO<1).
     */
    private double rh0 = 0.2; // tsp = 0.2 dtsp = 0.9

    /**
     * A parameter used for controlling the influence of the pheremone trail (ALPHA>=0).
     */
    private double alpha = 0.01; // tsp = 0.01 dtsp = 0.02

    /**
     * A parameter used for controlling the influence of distance between origin and destination (BETA>=1).
     */
    private double beta = 9.5; // tsp = 9.5 dtsp = 11.5

    /**
     * Constructs a new StaticSolver object which can run an ant colony optimisation solution implementation to find a route
     * through a TSP graph.
     * @param graph The graph the solver will solve when @code{runSolution} is called.
     */
    public AntColonyOptimizationSolver(StaticGraph graph) {
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
     * Starts this solver object running the logic to find a route through the StaticGraph in the @code{graph} attribute.
     * @param delayPerStep The delay each Ant will take before moving nodes.
     * @return output The results of the solution attempt.
     */
    public SolverOutput runSolution(int delayPerStep) {
        try {
            long startTime = System.nanoTime();
            setDelayPerStep(delayPerStep);
            // Activate all ants
            for (int x = 0; x < numAnts; x++) {
                executorCompletionService.submit(new Ant(this));
                activeAnts++;
                if (ThreadLocalRandom.current().nextDouble() > PROCESSING_CYCLE_PROBABILITY) {
                    processAnts();
                }
            }
            processAnts(); // Finish all ants that haven't been processed yet.
            getExecutorService().shutdownNow();
            long finishTime = System.nanoTime();
            return new Solution(graph, graph.getEdgeContainer().getTotalLength(), finishTime - startTime);
        } catch (Exception e) {
            return new Fail(e, graph);
        } catch (Error e) {
            return new Fail(e, graph);
        }
    }

    /**
     * Sends a single ant to the completion service makes it complete its task.
     */
    public void sendAnt() {
        executorCompletionService.submit(new Ant(this));
        activeAnts++;
        processAnts();
    }


    /**
     * While Ants are still traversing, this method will check if they are completed and gets the result to see if it
     * is smaller than the current shortest route.
     */
    public void processAnts() {
        while (activeAnts > 0) {
            try {
                Ant ant = executorCompletionService.take().get(); // Pick up an ant
                EdgeContainer currentRoute = ant.getRoute();
                // Check if the route found was shorter than the shortest thus far.
                if (shortestRoute == null || currentRoute.getTotalLength()
                                                        < shortestRoute.getTotalLength()) {
                    shortestRoute = currentRoute;
//                    System.out.println(shortestRoute.getTotalLength() + " : " + ant.getAntID());
                    graph.setEdgeContainer(shortestRoute);
                }
                activeAnts--;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the @code{graph} attribute to a new value.
     * @param graph The new value ot assign to the @code{graph} attribute.
     */
    public void setGraph(StaticGraph graph) {
        this.graph = graph;
    }

    /**
     * Returns the value of the @code{graph} attribute.
     * @return graph The value of the @code{graph} attribute.
     */
    public StaticGraph getGraph() {
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
     * Returns the value of the @code{executorCompletionService} attribute.
     * @return executorCompletionService The value of the @code{executorCompletionService} attribute.
     */
    public ExecutorCompletionService<Ant> getExecutorCompletionService() {
        return executorCompletionService;
    }

    /**
     * Returns the value of the @code{activeAnts} attribute.
     * @return activeAnts The value of the @code{activeAnts} attribute.
     */
    public int getActiveAnts() {
        return activeAnts;
    }

    /**
     * Sets the @code{activeAnts} attribute to a new value.
     * @param activeAnts The new value to assign the @code{activeAnts} attribute.
     */
    public void setActiveAnts(int activeAnts) {
        this.activeAnts = activeAnts;
    }

    /**
     * Fills the distance matrix with the distance of each node to each other node.
     */
    private void initialiseDistances() {
        int numNodes = graph.getNumNodes(); // Find out how many nodes there are.
        distanceMatrix = new double[numNodes][numNodes]; // Create a square matrix using the numNodes as side length
        // For each node, check the distance to every other node.
        for (int x = 0; x < numNodes; x++) {
            Node n1 = graph.getNodeContainer().getNodeSet().get(x);
            for (int y = 0; y < numNodes; y++) {
                Node n2 = graph.getNodeContainer().getNodeSet().get(y);
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
     * Sets the value of the @code{distanceMatrix} attribute to a new value.
     * @param newMatrix The new value to assign to the @code{distanceMatrix} attribute.
     */
    public void setDistanceMatrix(double[][] newMatrix) {
        this.distanceMatrix = newMatrix;
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

    /**
     * Returns the value of the @code{numAnts} attribute.
     * @return numAnts The value of the @code{numAnts} attribute.
     */
    public int getNumAnts() {
        return this.numAnts;
    }

    /**
     * Returns the value of the @code{q} attribute.
     * @return q The value of the @code{q} attribute.
     */
    public double getQ() {
        return q;
    }

    /**
     * Assigns a new value to the @code{q} attribute.
     * @param q The new value to assign the @code{q} attribute.
     */
    public void setQ(double q) {
        this.q = q;
    }

    /**
     * Returns the value of the @code{rh0} attribute.
     * @return rh0 The value of the @code{rh0} attribute.
     */
    public double getRh0() {
        return rh0;
    }

    /**
     * Assigns a new value to the @code{rh0} attribute.
     * @param rh0 The new value to assign the @code{rh0} attribute.
     */
    public void setRh0(double rh0) {
        this.rh0 = rh0;
    }

    /**
     * Returns the value of the @code{alpha} attribute.
     * @return alpha The value of the @code{alpha} attribute.
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * Sets the @code{alpha} attribute to a new value.
     * @param alpha The value to assign the @code{alpha} attribute.
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
     * Returns the value of the @code{beta} attribute.
     * @return beta The value of the @code{beta} attribute.
     */
    public double getBeta() {
        return beta;
    }

    /**
     * Sets the @code{beta} attribute to a new value.
     * @param beta The new value to assign the @code{beta} attribute.
     */
    public void setBeta(double beta) {
        this.beta = beta;
    }
}
