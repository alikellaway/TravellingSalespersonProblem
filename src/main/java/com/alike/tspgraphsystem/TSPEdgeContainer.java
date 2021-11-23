package com.alike.tspgraphsystem;

import com.alike.customexceptions.EdgeSuperimpositionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Used to mange sets of edges for use in TSPGraph objects.
 * @author alike
 */
public class TSPEdgeContainer {

    public ArrayList<TSPEdge> edgeSet;

    private int editCount;

    public final ReadWriteLock lock = new ReentrantReadWriteLock();

    public TSPEdgeContainer() {
        edgeSet = new ArrayList<>();
        editCount = 0;
    }

    public TSPEdgeContainer(ArrayList<TSPEdge> edgeSet) throws EdgeSuperimpositionException {
        setEdgeSet(edgeSet);
        edgeSet.trimToSize();
        editCount = 0;
    }

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

    public ArrayList<TSPEdge> getEdgeSet() {
        lock.readLock().lock();
        try {
            return this.edgeSet;
        } finally {
            lock.readLock().unlock();
        }
    }

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

    public int getEditCount() {
        lock.readLock().lock();
        try {
            return editCount;
        } finally {
            lock.readLock().unlock();
        }
    }

    public double calculateCurrentRouteLength() {
        double totalLength = 0;
        for (TSPEdge e : edgeSet) {
            Vector v = e.getStartNode().getVectorTo(e.getEndNode());
            totalLength+=v.magnitude();
        }
        return totalLength;
    }

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
}
