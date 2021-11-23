package com.alike.solutions;

import com.alike.tspgraphsystem.TSPEdge;
import com.alike.tspgraphsystem.TSPGraph;


import java.util.ArrayList;

public class GreedySolver {

    TSPGraph graph;
    ArrayList<TSPEdge> sortedEdges = new ArrayList<>();

    public GreedySolver(TSPGraph graph) {
        setGraph(graph);

    }

    public void setGraph(TSPGraph graph) {
        this.graph = graph;
    }
}
