package com.alike.solutions;

import com.alike.tspgraphsystem.TSPGraph;
import com.alike.tspgraphsystem.TSPNode;
import javafx.util.Pair;


import java.util.ArrayList;

public class BruteSolver {
    TSPGraph graph;


    public BruteSolver(TSPGraph graph) {
        setGraph(graph);

    }


    public void setGraph(TSPGraph graph) {
        this.graph = graph;
    }


//    private ArrayList<ArrayList<TSPNode>> findPermutations() {
//        ArrayList<TSPNode> nodes = graph.getNodeContainer().getNodeSet();
//
//    }
}
