package com.alike.tspgraphsystem;

import com.alike.customexceptions.EdgeSuperimpositionException;
import com.alike.customexceptions.NodeSuperimpositionException;

public class TSPGraph {

    private TSPNodeContainer nodeContainer;
    private TSPEdgeContainer edgeContainer;

    public TSPGraph(TSPNodeContainer nodeContainer, TSPEdgeContainer edgeContainer) {
        setNodeContainer(nodeContainer);
        setEdgeContainer(edgeContainer);
    }

    public TSPGraph() {
        setNodeContainer(new TSPNodeContainer());
        setEdgeContainer(new TSPEdgeContainer());
    }

    public TSPNodeContainer getNodeContainer() {
        return nodeContainer;
    }

    public void setNodeContainer(TSPNodeContainer nodeContainer) {
        this.nodeContainer = nodeContainer;
    }

    public TSPEdgeContainer getEdgeContainer() {
        return edgeContainer;
    }

    public void setEdgeContainer(TSPEdgeContainer edgeContainer) {
        this.edgeContainer = edgeContainer;
    }

    public static TSPGraph generateRandomGraph(int numNodes, boolean addEdges) {
        // Create our graph to draw
        TSPNodeContainer nSet = new TSPNodeContainer();
        for (int i = 0; i < numNodes; i++) {
            try {
                nSet.add(TSPNode.generateRandomTSPNode());
            } catch (NodeSuperimpositionException e) {
                i--;
            }
        }
        TSPEdgeContainer eSet = new TSPEdgeContainer();
        if (addEdges) {

            for (int x = 0; x < numNodes; x++) {
                try {
                    eSet.add(new TSPEdge(nSet.getNodeSet().get(x), nSet.getNodeSet().get((x + 1) % numNodes)));
                } catch (EdgeSuperimpositionException e) {
                    x--;
                }
            }
        }
        TSPGraph g = new TSPGraph();
        g.setNodeContainer(nSet);
        g.setEdgeContainer(eSet);
        return g;
    }
}
