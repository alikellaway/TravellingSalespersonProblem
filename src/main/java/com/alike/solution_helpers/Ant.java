package com.alike.solution_helpers;

import com.alike.customexceptions.EdgeSuperimpositionException;
import com.alike.customexceptions.EdgeToSelfException;
import com.alike.customexceptions.NonExistentNodeException;
import com.alike.solutions.AntColonyOptimizationSolver;
import com.alike.tspgraphsystem.TSPEdge;
import com.alike.tspgraphsystem.TSPEdgeContainer;
import com.alike.tspgraphsystem.TSPNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The class represent an Ant for use by the @code{AntColonyOptimisationSolver} class.
 * @author alike
 */
public class Ant implements Callable<Ant> {
    /**
     * A reference to the @code{AntColonyOptimizationSolver} object this ant will be working in.
     */
    private AntColonyOptimizationSolver acos;
    /**
     * The edge container this ant will output.
     */
    private TSPEdgeContainer route;
    /**
     * The unique ID of this ant.
     */
    private int antID;
    /**
     * The number of ants currently initialised.
     */
    private static int numAnts = 0;
    /**
     * A state used to verify when certain events have or haven't happened.
     */
    private static final int invalidNodeIdx = -1;

    /**
     * Storing the number of nodes statically to more efficiently use this value (before we had each ant checking it
     * multiple times).
     */
    private static int numNodes;

    /**
     * Parameter used to adjust the amount of pheromone deposited per traversal (0<Q<1).
     */
    private static final double Q = 0.0005; // tsp = 0.0005 dtsp = 0.0004

    /**
     * A parameter used for adjusting the amount of pheromone evaporation (0<RHO<1).
     */
    private static final double RHO = 0.2; // tsp = 0.2 dtsp = 0.9

    /**
     * A parameter used for controlling the influence of the pheremone trail (ALPHA>=0).
     */
    private static final double ALPHA = 0.01; // tsp = 0.01 dtsp = 0.02

    /**
     * A parameter used for controlling the influence of distance between origin and destination (BETA>=1).
     */
    private static final double BETA = 9.5; // tsp = 9.5 dtsp = 11.5

    /**
     * Constructor initialises a new ant.
     * @param acos A reference to the @code{AntColonyOptimizationSolver} object this ant is working for.
     */
    public Ant(AntColonyOptimizationSolver acos) {
        setAcos(acos); // Set the sovler reference
        assignAntID(); // Give the ant a unique id
        setNumNodes(getAcos().getGraph().getNumNodes());
    }

    /**
     * The code that the Ant runs when the thread pool calls it.
     * @return this Itself so we can access necessary information from the Ant.
     * @throws NonExistentNodeException Thrown when a node is searched for but does not exist.
     * @throws EdgeSuperimpositionException Thrown when an edge is superimposed on another inside an edge container.
     */
    @Override
    public Ant call() throws NonExistentNodeException, EdgeSuperimpositionException, InterruptedException, EdgeToSelfException {
        // Ant chooses a random node to start at.
        int startNodeID = ThreadLocalRandom.current().nextInt(numNodes);
        // Create space to store the nodes that the ant chooses to traverse as its route.
        ArrayList<TSPNode> routeNodes = new ArrayList<>(numNodes);
        // We need to have a unique hashmap for every ant to know which nodes it has and hasn't visited.
        HashMap<TSPNode, Boolean> visitedNodes = new HashMap<>(numNodes);
        // Initialise all nodes as false
        for (TSPNode nd : acos.getGraph().getNodeContainer().getNodeSet()) {
            visitedNodes.put(nd, false);
        }
        // Set initial node to visited
        visitedNodes.replace(acos.getGraph().getNodeContainer().getNodeByID(startNodeID), true);
        double routeLength = 0.0; // Need to actively record the route length to adjust pheromone levels.
        int numVisitedNodes = 0; // Record the number of nodes the ant has visited.
        int x = startNodeID; // Copy start node ID as an index for use in matrices.
        int y = invalidNodeIdx; // Define the current destination node as invalid (we haven't found it yet).
        // Get the new destination node
        if (numNodes != numVisitedNodes) {
            y = getDestinationNode(x, visitedNodes);
        }
        // While destination node is not invalid we can add the next destination node to our route.
        while (y != invalidNodeIdx) {
            routeNodes.add(acos.getGraph().getNodeContainer().getNodeByID(x)); // Add destination node to route.
            numVisitedNodes++;
            routeLength += acos.getDistanceMatrix()[x][y];
            adjustPheremoneLevel(x, y, routeLength); // Adjust pheromone levels of the edge we just traversed.
            visitedNodes.replace(acos.getGraph().getNodeContainer().getNodeByID(y), true); // Set to visited in hashmap
            x = y; // Destination node is now the current node.
            if (numVisitedNodes != numNodes) { // If we haven't visited all nodes
                y = getDestinationNode(x, visitedNodes); // Get the next destination node
            } else {
                y = invalidNodeIdx; // Otherwise reset y to be invalidNodeIdx
            }
            Thread.sleep(acos.getDelayPerStep());
        }
        // routeLength += acos.getDistanceMatrix()[x][startNodeID];
        routeNodes.add(acos.getGraph().getNodeContainer().getNodeByID(x)); // Add the final node to our route
        // Add our startNode again here?
        route = createEdgeContainerFromNodeSet(routeNodes); // Construct an edge container using the routeNodes array.
        return this;
    }

    /**
     * Adjusts the pheromone level in the matrix of the ACOS object.
     * @param x The start node ID of the edge which we are adjusting the pheromone levels of.
     * @param y The end node ID of the edge which we are adjusting the pheromone levels of.
     * @param distance The distance between these two nodes.
     */
    private void adjustPheremoneLevel(int x, int y, double distance) {
        boolean flag = false;
        while (!flag) {
            double currentPheremoneLevel = acos.getPheromoneLevelMatrix()[x][y].doubleValue();
            double updatedPheremoneLevel = (1 - RHO) * currentPheremoneLevel + Q/distance;
            if (updatedPheremoneLevel < 0) {
                flag = acos.getPheromoneLevelMatrix()[x][y].compareAndSet(0);
            } else {
                flag = acos.getPheromoneLevelMatrix()[x][y].compareAndSet(updatedPheremoneLevel);
            }
        }
    }

    /**
     * Chooses the next destination node.
     * @param x The current node.
     * @param visitedNodes A hashmap containing whether we have or haven't visited nodes.
     * @return @code{destinationNode} The node that has been chosen to visit next.
     * @throws NonExistentNodeException Thrown if a node ID is referenced but does not exist.
     */
    private int getDestinationNode(int x, HashMap<TSPNode, Boolean> visitedNodes) throws NonExistentNodeException {
        int destinationNode = invalidNodeIdx;
        double random = ThreadLocalRandom.current().nextDouble();
        ArrayList<Double> transitionProbabilities = getTransitionProbabilities(x, visitedNodes);
        for (int y = 0; y < acos.getGraph().getNumNodes(); y++) {
            if (transitionProbabilities.get(y) > random) {
                destinationNode = y;
                break;
            } else {
                random -= transitionProbabilities.get(y);
            }
        }
        return destinationNode;
    }

    /**
     * Gets the transition probabilities to each node from the current node.
     * @param x The index of the current node.
     * @param visitedNodes The hashmap containing whether we have visited each node.
     * @return @code{transitionProbabilities} An ArrayList containing transitional probabilities for each node.
     * @throws NonExistentNodeException Thrown when a node ID is referenced but does not exist.
     */
    private ArrayList<Double> getTransitionProbabilities(int x, HashMap<TSPNode, Boolean> visitedNodes) throws NonExistentNodeException {
        // Create space for output
        ArrayList<Double> transitionProbabilities = new ArrayList<>(acos.getGraph().getNumNodes());
        // Populate with 0s
        for (int i = 0; i < acos.getGraph().getNumNodes(); i++) {
            transitionProbabilities.add(0.0);
        }
        // Get the denominator of the function
        double denominator = getTPDenominator(transitionProbabilities, x, visitedNodes);
        // Set the transition probability values to new values using the denominator.
        for (int y = 0; y < acos.getGraph().getNumNodes(); y++) {
            transitionProbabilities.set(y, transitionProbabilities.get(y)/denominator);
        }
        return transitionProbabilities;
    }

    /**
     * Returns the value of the denominator of the transitional probability function.
     * @param transitionProbabilities The array list from which we are calculating the denominator.
     * @param x The ID of the current node.
     * @param visitedCities The hashmap containing information on the nodes whether they've been visited or not.
     * @return @code{denominator} The denominator given these parameters.
     * @throws NonExistentNodeException Thrown if a node ID is referenced that does not exist.
     */
    private double getTPDenominator(ArrayList<Double> transitionProbabilities, int x, HashMap<TSPNode, Boolean> visitedCities) throws NonExistentNodeException {
        double denominator = 0.0;
        for (int y = 0; y < acos.getGraph().getNumNodes(); y++) { // Loop through the nodes
            if (!visitedCities.get(acos.getGraph().getNodeContainer().getNodeByID(y))) { // If the node is not visited
                if (x == y) { // If its the node we are currently on, set the probability to 0 (or get stuck).
                    transitionProbabilities.set(y, 0.0);
                } else { // If not, then set the probability to the output of getTPNumerator().
                    transitionProbabilities.set(y, getTPNumerator(x, y));
                }
                denominator += transitionProbabilities.get(y); // Add the value of the whatever we just did.
            }
        }
        return denominator;
    }

    /**
     * Gets the value of the transitional probabilty function numerator.
     * @param x The position in the x direction of this edge in the pheromone and distance matrices.
     * @param y The position in the y direction of the current edge in the pheromone and distance matrices.
     * @return numerator The value of the transitional probability function numerator.
     */
    private double getTPNumerator(int x, int y) {
        double numerator = 0.0;
        double pheromoneLevel = acos.getPheromoneLevelMatrix()[y][x].doubleValue();
        if (pheromoneLevel != 0.0) { // If pheromone level not 0
            // Recalculate pheromone level using the formula.
            numerator = Math.pow(pheromoneLevel, ALPHA) * Math.pow(1/acos.getDistanceMatrix()[x][y], BETA);
        }
        return numerator;
    }

    /**
     * Creates a @code{TSPEdgeContainer} object given an arraylist of nodes.
     * @param routeNodes The ArrayList of nodes from which to construct a @code{TSPEdgeContainer}.
     * @return @code{edgeContainer} The new @code{TSPEdgeContainer}.
     * @throws EdgeSuperimpositionException Thrown if the @code{TSPEdgeContainer} object finds edges superimposed
     * in the input set.
     */
    private TSPEdgeContainer createEdgeContainerFromNodeSet(ArrayList<TSPNode> routeNodes) throws
                                                                EdgeSuperimpositionException, EdgeToSelfException {
        TSPEdgeContainer edgeContainer = new TSPEdgeContainer();
        for (int idx = 0; idx < routeNodes.size(); idx++) {
            TSPNode startNode = routeNodes.get(idx);
            TSPNode endNode = routeNodes.get((idx + 1) % routeNodes.size());
            edgeContainer.add(new TSPEdge(startNode, endNode));
        }
        return edgeContainer;
    }

    /**
     * Returns the route the Ant took.
     * @return route The value of the @code{route} attribute.
     */
    public TSPEdgeContainer getRoute() {
        return route;
    }

    /**
     * Assigns the ant a new ID during the constructor.
     */
    private void assignAntID() {
        setAntID(numAnts);
        numAnts++;
    }

    /**
     * Returns the value of the @code{acos} attribute.
     * @return acos The value of the @code{acod} attribute.
     */
    public AntColonyOptimizationSolver getAcos() {
        return acos;
    }

    /**
     * Sets the value of the @code{acos} to a new value.
     * @param acos The new value to become the @code{acos} attribute.
     */
    public void setAcos(AntColonyOptimizationSolver acos) {
        this.acos = acos;
    }

    /**
     * Returns the value of the @code{antID} attribute.
     * @return antID The value of the @code{antID} attribute.
     */
    public int getAntID() {
        return antID;
    }

    /**
     * Sets the value of the @code{antID} to a new value.
     * @param antID The new value to become the @code{antID} attribute.
     */
    public void setAntID(int antID) {
        this.antID = antID;
    }

    /**
     * Sets the static variable @code{numNodes} to a new value.
     * @param numNodes The new value to become the @code{numNodes} attribute.
     */
    public static void setNumNodes(int numNodes) {
        Ant.numNodes = numNodes;
    }
}
