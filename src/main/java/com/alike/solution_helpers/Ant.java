package com.alike.solution_helpers;

import com.alike.customexceptions.EdgeSuperimpositionException;
import com.alike.customexceptions.EdgeToSelfException;
import com.alike.customexceptions.NonExistentNodeException;
import com.alike.solvers.AntColonyOptimisationSolver;
import com.alike.staticgraphsystem.Edge;
import com.alike.staticgraphsystem.EdgeContainer;
import com.alike.staticgraphsystem.Node;

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
     * A reference to the @code{AntColonyOptimisationSolver} object this ant will be working in.
     */
    private AntColonyOptimisationSolver acos;

    /**
     * The edge container this ant will output.
     */
    private EdgeContainer route;

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
    private static final int INVALID_NODE_IDX = -1;

    /**
     * Storing the number of nodes statically to more efficiently use this value (before we had each ant checking it
     * multiple times).
     */
    private static int numNodes;

    /**
     * Constructor initialises a new ant.
     * @param acos A reference to the @code{AntColonyOptimisationSolver} object this ant is working for.
     */
    public Ant(AntColonyOptimisationSolver acos) {
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
    public Ant call() {
        // Ant chooses a random node to start at.
        int startNodeID = ThreadLocalRandom.current().nextInt(numNodes);
        // Create space to store the nodes that the ant chooses to traverse as its route.
        ArrayList<Node> orderedNodes = new ArrayList<>(numNodes);
        // We need to have a unique hashmap for every ant to know which nodes it has and hasn't visited.
        HashMap<Integer, Boolean> nodesVisitationLog = new HashMap<>(numNodes);
        // Initialise all nodes as false
        for (Node nd : acos.getGraph().getNodeContainer().getNodeSet()) {
            nodesVisitationLog.put(nd.getNodeID(), false);
        }
        // Set initial node to visited
        nodesVisitationLog.put(startNodeID, true);
        double routeLength = 0.0; // Need to actively record the route length to adjust pheromone levels.
        int numVisitedNodes = 0; // Record the number of nodes the ant has visited.
        int currentNodeId = startNodeID; // Copy start node ID as an index for use in matrices.
        int destinationNodeId = INVALID_NODE_IDX; // Define the current destination node as invalid (we haven't found it yet).
        // Get the new destination node
        if (numVisitedNodes != numNodes) {
            try {
                destinationNodeId = getDestinationNode(currentNodeId, nodesVisitationLog);
            } catch (NonExistentNodeException e) {
                e.printStackTrace();
            }
        }
        // While destination node is not invalid we can add the next destination node to our route.
        while (destinationNodeId != INVALID_NODE_IDX) {
            try {
                orderedNodes.add(acos.getGraph().getNodeContainer().getNodeByID(currentNodeId)); // Add destination node to route.
                numVisitedNodes++;
                routeLength += acos.getDistanceMatrix()[currentNodeId][destinationNodeId];
                adjustPheremoneLevel(currentNodeId, destinationNodeId, routeLength); // Adjust pheromone levels of the edge we just traversed.
                nodesVisitationLog.put(destinationNodeId, true); // Set to visited in hashmap
                currentNodeId = destinationNodeId; // Destination node is now the current node.
                if (numVisitedNodes != numNodes) { // If we haven't visited all nodes
                    destinationNodeId = getDestinationNode(currentNodeId, nodesVisitationLog); // Get the next destination node
                } else {
                    destinationNodeId = INVALID_NODE_IDX; // Otherwise reset destination to be INVALID_NODE_IDX
                }
            } catch (NonExistentNodeException e) {
                e.printStackTrace();
            }
            RepeatedFunctions.sleep(acos.getDelayPerStep());
        }
        try {
            orderedNodes.add(acos.getGraph().getNodeContainer().getNodeByID(currentNodeId)); // Add the final node to our route
        } catch (NonExistentNodeException e) {

        }
        // Add our startNode again here?
        route = createRouteFromNodeSet(orderedNodes); // Construct an edge container using the routeNodes array.
        return this;
    }

    /**
     * Adjusts the pheromone level in the matrix of the ACOS object.
     * @param currentNodeId The start node ID of the edge which we are adjusting the pheromone levels of.
     * @param destinationNodeId The end node ID of the edge which we are adjusting the pheromone levels of.
     * @param distance The distance between these two nodes.
     */
    private void adjustPheremoneLevel(int currentNodeId, int destinationNodeId, double distance) {
        boolean done = false;
        while (!done) {
            double currentPheromoneLevel = acos.getPheromoneLevelMatrix()[currentNodeId][destinationNodeId].doubleValue();
            double updatedPheromoneLevel = (1 - acos.getRh0()) * currentPheromoneLevel + (acos.getQ()/distance);
            if (updatedPheromoneLevel < 0.0) { // If all the pheromone has evaporated between nodes x and y.
                done = acos.getPheromoneLevelMatrix()[currentNodeId][destinationNodeId].compareAndSet(0); // Reset to 0
            } else { // Otherwise set the pheromone level to the newly updated level.
                done = acos.getPheromoneLevelMatrix()[currentNodeId][destinationNodeId].compareAndSet(updatedPheromoneLevel);
            }
        }
    }

    /**
     * Chooses the next destination node.
     * @param currNode The current node.
     * @param visitedNodes A hashmap containing whether we have or haven't visited nodes.
     * @return @code{destinationNode} The node that has been chosen to visit next.
     * @throws NonExistentNodeException Thrown if a node ID is referenced but does not exist.
     */
    private int getDestinationNode(int currNode, HashMap<Integer, Boolean> visitedNodes) throws NonExistentNodeException {
        int destinationNode = INVALID_NODE_IDX;
        // A list containing probabilities of moving to other nodes from the current node.
        ArrayList<Double> transitionProbabilities = getTransitionProbabilities(currNode, visitedNodes);
        /* Try to find a next node by choosing a random number and selecting a node with a transitional probability
           higher than it. If we didnt find one, lower the random number by some amount (note this is unpredictable and
           will not always return the highest probability node.
         */
        double random = ThreadLocalRandom.current().nextDouble();
        for (int y = 0; y < numNodes; y++) {
            if (transitionProbabilities.get(y) > random && !visitedNodes.get(y)) {
                destinationNode = y;
                break;
            } else {
                random -= transitionProbabilities.get(y);
            }
        }
        // If we didnt select one, but still havent visited all, visit the first one we can.
        if (!allVisited(visitedNodes) && destinationNode == INVALID_NODE_IDX) {
            for (int y = 0; y < numNodes; y++) {
                if (!visitedNodes.get(y)) {
                    destinationNode = y;
                }
            }
        }
        return destinationNode;
    }

    /**
     * Gets the transition probabilities to each node from the current node.
     * @param currentNodeID The index of the current node.
     * @param visitedNodes The hashmap containing whether we have visited each node.
     * @return @code{transitionProbabilities} An ArrayList containing transitional probabilities for each node.
     * @throws NonExistentNodeException Thrown when a node ID is referenced but does not exist.
     */
    private ArrayList<Double> getTransitionProbabilities(int currentNodeID, HashMap<Integer, Boolean> visitedNodes) throws NonExistentNodeException {
        // Create space for output
        ArrayList<Double> transitionProbabilities = new ArrayList<>(acos.getGraph().getNumNodes());
        // Populate with 0s
        for (int i = 0; i < acos.getGraph().getNumNodes(); i++) {
            transitionProbabilities.add(0.0);
        }
        // Get the denominator of the function
        double denominator = getTPDenominator(transitionProbabilities, currentNodeID, visitedNodes);
        // Set the transition probability values to new values using the denominator.
        for (int y = 0; y < numNodes; y++) {
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
     */
    private double getTPDenominator(ArrayList<Double> transitionProbabilities, int x, HashMap<Integer, Boolean> visitedCities) {
        double denominator = 0.0;
        for (int y = 0; y < numNodes; y++) { // Loop through the nodes
            if (!visitedCities.get(y)) { // If the node is not visited
                if (x == y) { // If it's the node we are currently on, set the probability to 0 (or get stuck).
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
        double pheromoneLevel = acos.getPheromoneLevelMatrix()[x][y].doubleValue();
        if (pheromoneLevel != 0.0) { // If pheromone level not 0
            numerator = Math.pow(pheromoneLevel, acos.getAlpha()) * Math.pow(1/acos.getDistanceMatrix()[x][y], acos.getBeta());
        }
        return numerator;
    }

    /**
     * Creates a @code{EdgeContainer} object containing a complete route given an arraylist of nodes.
     * @param routeNodes The ArrayList of nodes from which to construct a @code{EdgeContainer}.
     * @return @code{edgeContainer} The new @code{EdgeContainer}.
     */
    private EdgeContainer createRouteFromNodeSet(ArrayList<Node> routeNodes) {
        EdgeContainer edgeContainer = new EdgeContainer();
        try {
            for (int idx = 0; idx < routeNodes.size(); idx++) {
                Node startNode = routeNodes.get(idx);
                Node endNode = routeNodes.get((idx + 1) % routeNodes.size());
                edgeContainer.add(new Edge(startNode, endNode));
            }
        } catch (EdgeToSelfException | EdgeSuperimpositionException e) {
            e.printStackTrace();
        }
        return edgeContainer;
    }

    /**
     * Returns the route the Ant took.
     * @return route The value of the @code{route} attribute.
     */
    public EdgeContainer getRoute() {
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
    public AntColonyOptimisationSolver getAcos() {
        return acos;
    }

    /**
     * Sets the value of the @code{acos} to a new value.
     * @param acos The new value to become the @code{acos} attribute.
     */
    public void setAcos(AntColonyOptimisationSolver acos) {
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

    /**
     * Checks if all the nodes have been visited.
     * @param visitationLog The hashmap containing whether nodes have been visited.
     * @return boolean true if all nodes have been visited, false if not.
     */
    private boolean allVisited(HashMap<Integer, Boolean> visitationLog) {
        for (int i = 0; i <= numNodes-1; i++) {
            if (!visitationLog.get(i)) {
                return false;
            }
        }
        return true;
    }
}
