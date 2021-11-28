package com.alike;

import com.alike.tspgraphsystem.TSPGraph;

public class Main2 {
    public static void main(String[] args) {
        TSPGraph g = TSPGraph.generateRandomGraph(11, false);
        g.makeComplete();
        System.out.println(g.getEdgeContainer().getEdgeSet().size());
    }
}