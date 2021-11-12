package com.alike;

import com.alike.customexceptions.EdgeSuperimpositionException;
import com.alike.customexceptions.NodeSuperimpositionException;
import com.alike.tspgraphsystem.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class Main extends Application {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    private final String STAGE_TITLE = "TSP";


    public static void main(String[] args) {

//        try {
//            TSPNodeContainer nSet = new TSPNodeContainer();
//            for (int x = 0; x < 20; x++) {
//                TSPNode n = new TSPNode(Coordinate.generateRandomCoordinate(WIDTH, HEIGHT));
//                nSet.add(n);
//            }
//            System.out.println(nSet);
//        } catch (NodeSuperimpositionException e) {
//            e.printStackTrace();
//        }
//
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Name the window
        stage.setTitle(STAGE_TITLE);

        // Create our graph to draw
        TSPNodeContainer nSet = new TSPNodeContainer();
        int numNodes = 50;
        for (int i = 0; i < numNodes; i++) {
            try {
                nSet.add(TSPNode.generateRandomTSPNode());
            } catch (NodeSuperimpositionException e) {
                i--;
            }
        }
        TSPEdgeContainer eSet = new TSPEdgeContainer();
        int numEdges = numNodes - 1;
        for (int x = 0; x < numEdges; x++) {
            try {
                eSet.add(new TSPEdge(nSet.getNodeSet().get(x), nSet.getNodeSet().get(x + 1)));
            } catch (EdgeSuperimpositionException e) {
                x--;
            }
        }
        TSPGraph g = new TSPGraph();
        g.setNodeContainer(nSet);
        g.setEdgeContainer(eSet);

        // Create canvas to draw our nodes onto
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        g.drawGraph(gc);
        root.getChildren().add(canvas);

        stage.setScene(new Scene(root));
        stage.show();
    }
}
