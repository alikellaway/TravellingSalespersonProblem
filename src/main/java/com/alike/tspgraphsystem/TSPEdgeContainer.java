package com.alike.tspgraphsystem;

import com.alike.customexceptions.EdgeSuperimpositionException;

import java.util.ArrayList;

/**
 * Used to mange sets of edges for use in TSPGraph objects.
 * @author alike
 */
public class TSPEdgeContainer {

    public ArrayList<TSPEdge> edgeSet;

    private int editCount;

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
        if (edgeExists(e)) {
            throw new EdgeSuperimpositionException("Tried to add an edge that already exists.");
        } else {
            edgeSet.add(e);
            edgeSet.trimToSize();
            editCount++;
        }
    }

    public void remove(TSPEdge e) {
        edgeSet.remove(e);
        edgeSet.trimToSize();
        editCount++;
    }

    private void checkEdgeSetForSuperimposition(ArrayList<TSPEdge> edgeSet) throws EdgeSuperimpositionException {
        for (TSPEdge e : edgeSet) {
            for (int i = edgeSet.indexOf(e) + 1; i < edgeSet.size(); i++) {
                if (e.equals(edgeSet.get(i))) {
                    throw new EdgeSuperimpositionException("Tried to initialise edge set with input array " +
                            "containing superimposed edges.");
                }
            }
        }
    }

    public void setEdgeSet(ArrayList<TSPEdge> edgeSet) throws EdgeSuperimpositionException {
        checkEdgeSetForSuperimposition(edgeSet);
        this.edgeSet = edgeSet;
        editCount += edgeSet.size();
    }

    public ArrayList<TSPEdge> getEdgeSet() {
        return this.edgeSet;
    }

    private boolean edgeExists(TSPEdge e) {
        for (TSPEdge edge : getEdgeSet()) {
            if (edge.equals(e)) {
                return true;
            }
        }
        return false;
    }

    public int getEditCount() {
        return editCount;
    }
}
