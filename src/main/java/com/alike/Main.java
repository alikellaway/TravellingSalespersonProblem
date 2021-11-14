package com.alike;

import com.alike.customexceptions.EdgeSuperimpositionException;
import com.alike.customexceptions.NodeSuperimpositionException;
import com.alike.graphical.TSPGraphDrawer;
import com.alike.tspgraphsystem.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    private final String STAGE_TITLE = "TSP";
    private Canvas canvas;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle(STAGE_TITLE);
        Group root = new Group();
        canvas = new Canvas(WIDTH, HEIGHT);
        TSPGraph graph = generateRandomGraph();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.show();


        TSPGraphDrawer drawer = new TSPGraphDrawer(graph, canvas, 1);
        drawer.start();
    }

    public TSPGraph generateRandomGraph() {
        // Create our graph to draw
        TSPNodeContainer nSet = new TSPNodeContainer();
        int numNodes = 5;
        for (int i = 0; i < numNodes; i++) {
            try {
                nSet.add(TSPNode.generateRandomTSPNode());
            } catch (NodeSuperimpositionException e) {
                i--;
            }
        }
        TSPEdgeContainer eSet = new TSPEdgeContainer();

        for (int x = 0; x < numNodes; x++) {
            try {
                eSet.add(new TSPEdge(nSet.getNodeSet().get(x), nSet.getNodeSet().get((x + 1) % numNodes)));
            } catch (EdgeSuperimpositionException e) {
                x--;
            }
        }
        TSPGraph g = new TSPGraph();
        g.setNodeContainer(nSet);
        g.setEdgeContainer(eSet);
        return g;
    }
}
