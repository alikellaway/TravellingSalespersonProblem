package com.alike.tspgraphsystem;

import com.alike.customexceptions.EdgeSuperimpositionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Used to manage a set of TSPEdge objects for use in TSPGraph objects.
 * @author alike
 */
public class TSPEdgeContainer {
    /**
     * The collection of edges that are managed by this @code{TSPEdgeContainer} object.
     */
    private ArrayList<TSPEdge> edgeSet;

    /**
     * The number of times this edge container has been edited (for use during heuristic and optimisation algorithms)
     */
    private int editCount;

    /**
     * Used to lock the object to avoid concurrency issues.
     */
    public final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Used to initialise a new empty @code{TSPEdgeContainer} object.
     */
    public TSPEdgeContainer() {
        edgeSet = new ArrayList<>();
        editCount = 0;
    }

    /**
     * Used to add to the edge set in this edge container object.
     * @param e A new TSPEdge object to add to this container.
     * @throws EdgeSuperimpositionException Thrown if the edg already exists inside this edge object.
     */
    public void add(TSPEdge e) throws EdgeSuperimpositionException {
        lock.writeLock().lock();
        try {
            if (edgeExists(e)) {
                throw new EdgeSuperimpositionException("Tried to add an edge that already exists.");
            } else {
                edgeSet.add(e);
                edgeSet.trimToSize();
                editCount++;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Used to remove edges from this container.
     * @param e The edge to remove from this container.
     */
    public void remove(TSPEdge e) {
        lock.writeLock().lock();
        try {
            edgeSet.remove(e);
            edgeSet.trimToSize();
            editCount++;
        } finally {
            lock.writeLock().unlock();
        }

    }

    /**
     * Used to check that an input edgeSet doesn't have any superimposed edges.
     * @param edgeSet The edge set to check.
     * @throws EdgeSuperimpositionException Thrown if the edge set has superimposed edges.
     */
    private void checkEdgeSetForSuperimposition(ArrayList<TSPEdge> edgeSet) throws EdgeSuperimpositionException {
        lock.readLock().lock();
        try {
            for (TSPEdge e : edgeSet) {
                for (int i = edgeSet.indexOf(e) + 1; i < edgeSet.size(); i++) {
                    if (e.equals(edgeSet.get(i))) {
                        throw new EdgeSuperimpositionException("Tried to initialise edge set with input array " +
                                "containing superimposed edges.");
                    }
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Used to set the edgeSet attribute of this container to a new edge set.
     * @param edgeSet The new edge set to become the edge set of this container.
     * @throws EdgeSuperimpositionException Thrown if the input edge set has superimposed edges.
     */
    public void setEdgeSet(ArrayList<TSPEdge> edgeSet) throws EdgeSuperimpositionException {
        lock.writeLock().lock();
        try {
            checkEdgeSetForSuperimposition(edgeSet);
            this.edgeSet = edgeSet;
            editCount += edgeSet.size();
        } finally {
            lock.writeLock().lock();
        }
    }

    /**
     * Returns the @code{edgeSet} attribute of this TSPEdgeContainer object.
     * @return edgeSet The @code{edgeSet} attribute.
     */
    public ArrayList<TSPEdge> getEdgeSet() {
        lock.readLock().lock();
        try {
            return this.edgeSet;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Used to check if an edge currently exists inside this container.
     * @param e The edge to check existance for.
     * @return boolean: true if the edge already exists in this container, false if it does not.
     */
    private boolean edgeExists(TSPEdge e) {
        lock.readLock().lock();
        try {
            for (TSPEdge edge : getEdgeSet()) {
                if (edge.equals(e)) {
                    return true;
                }
            }
            return false;
        } finally {
            lock.readLock().unlock();
        }

    }

    /**
     * Returns the value of the @code{editCount} attribute.
     * @return @code{editCount} The value of the @code{editCount} attribute.
     */
    public int getEditCount() {
        lock.readLock().lock();
        try {
            return editCount;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Calculates the total length of the edges currently within the container.
     * @return totalLenght The total length of the edges in the container.
     */
    public double getTotalLength() {
        double totalLength = 0;
        for (TSPEdge e : edgeSet) {
            Vector v = e.getStartNode().getVectorTo(e.getEndNode());
            totalLength+=v.magnitude();
        }
        return totalLength;
    }

    /**
     * Outputs this TSPEdgeContainer into a JSON format string.
     * @return
     */
    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "Failed to load TSPNodeContainer object into JSON format.";
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
    public void absorb(TSPEdgeContainer otherContainer) {
        for (TSPEdge e : otherContainer.getEdgeSet()) {
            try {
                this.add(e);
            } catch (EdgeSuperimpositionException ignored) {

            }
        }
    }
}
