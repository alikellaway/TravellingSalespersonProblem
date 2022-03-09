package com.alike.staticgraphsystem;

import com.alike.customexceptions.EdgeSuperimpositionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Used to manage a set of Edge objects for use in StaticGraph objects.
 * @author alike
 */
public class EdgeContainer {
    /**
     * The collection of edges that are managed by this @code{EdgeContainer} object.
     */
    private CopyOnWriteArrayList<Edge> edgeSet;

    /**
     * The number of times this edge container has been edited (for use during heuristic and optimisation algorithms)
     */
    private int editCount;

    /**
     * Used to initialise a new empty @code{EdgeContainer} object.
     */
    public EdgeContainer() {
        edgeSet = new CopyOnWriteArrayList<>();
        editCount = 0;
    }

    /**
     * Used to add to the edge set in this edge container object.
     * @param e A new Edge object to add to this container.
     * @throws EdgeSuperimpositionException Thrown if the edg already exists inside this edge object.
     */
    public void add(Edge e) throws EdgeSuperimpositionException {
        if (edgeExists(e)) {
            throw new EdgeSuperimpositionException("Tried to add an edge that already exists.");
        } else {
            edgeSet.add(e);
            editCount++;
        }
    }


    /**
     * Used to remove edges from this container.
     * @param e The edge to remove from this container.
     */
    public void remove(Edge e) {
        edgeSet.remove(e);
        editCount++;
    }

    /**
     * Used to check that an input edgeSet doesn't have any superimposed edges.
     * @param edgeSet The edge set to check.
     * @throws EdgeSuperimpositionException Thrown if the edge set has superimposed edges.
     */
    private void checkEdgeSetForSuperimposition(CopyOnWriteArrayList<Edge> edgeSet) throws EdgeSuperimpositionException {
        for (Edge e : edgeSet) {
            for (int i = edgeSet.indexOf(e) + 1; i < edgeSet.size(); i++) {
                if (e.equals(edgeSet.get(i))) {
                    throw new EdgeSuperimpositionException("Tried to initialise edge set with input array " +
                            "containing superimposed edges.");
                }
            }
        }
    }

    /**
     * Used to set the edgeSet attribute of this container to a new edge set.
     * @param edgeSet The new edge set to become the edge set of this container.
     * @throws EdgeSuperimpositionException Thrown if the input edge set has superimposed edges.
     */
    public void setEdgeSet(CopyOnWriteArrayList<Edge> edgeSet) throws EdgeSuperimpositionException {
            checkEdgeSetForSuperimposition(edgeSet);
            this.edgeSet = edgeSet;
            editCount += edgeSet.size();
    }

    /**
     * Returns the @code{edgeSet} attribute of this EdgeContainer object.
     * @return edgeSet The @code{edgeSet} attribute.
     */
    public CopyOnWriteArrayList<Edge> getEdgeSet() {
        return this.edgeSet;
    }

    /**
     * Used to check if an edge currently exists inside this container.
     * @param e The edge to check existance for.
     * @return boolean: true if the edge already exists in this container, false if it does not.
     */
    private boolean edgeExists(Edge e) {
        for (Edge edge : getEdgeSet()) {
            if (edge.equals(e)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the value of the @code{editCount} attribute.
     * @return @code{editCount} The value of the @code{editCount} attribute.
     */
    public int getEditCount() {
        return editCount;
    }

    /**
     * Calculates the total length of the edges currently within the container.
     * @return totalLenght The total length of the edges in the container.
     */
    public double getTotalLength() {
        double totalLength = 0;
        for (Edge e : edgeSet) {
            totalLength += e.getLength();
        }
        return totalLength;
    }

    /**
     * Outputs this EdgeContainer into a JSON format string.
     * @return string The container as a string in json format.
     */
    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "Failed to load NodeContainer object into JSON format.";
    }

    /**
     * Used to empty the edge set.
     */
    public void clear() {
        getEdgeSet().clear();
        editCount = 0;
    }

    /**
     * Used to add all the edges from the parameter container into this container
     * @param otherContainer The container to absorb.
     */
    public void absorb(EdgeContainer otherContainer) {
        for (Edge e : otherContainer.getEdgeSet()) {
            try {
                this.add(e);
            } catch (EdgeSuperimpositionException ignored) {

            }
        }
    }

    /**
     * Constructs a new Edge container that is a copy of this container.
     * @return copy The copy of this edge container.
     * @throws EdgeSuperimpositionException Thrown if an edge superimpostion occurs when copying.
     */
    public EdgeContainer copy() throws EdgeSuperimpositionException {
        EdgeContainer copy = new EdgeContainer();
        for (Edge e : getEdgeSet()) {
            copy.add(e);
        }
        return copy;
    }
}
