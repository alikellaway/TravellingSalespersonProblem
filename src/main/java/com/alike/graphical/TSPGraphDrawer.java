package com.alike.graphical;

import com.alike.tspgraphsystem.TSPGraph;
import javafx.scene.Scene;

public class TSPGraphDrawer {

    TSPGraph graph;

    public TSPGraphDrawer(TSPGraph graph, Scene scene) {
        setGraph(graph);
    }

    public void draw() {

    }


    public TSPGraph getGraph() {
        return graph;
    }

    public void setGraph(TSPGraph graph) {
        this.graph = graph;
    }
}
