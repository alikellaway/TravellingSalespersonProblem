package com.alike;

import com.alike.customexceptions.InvalidGraphException;
import com.alike.graphical.TSPGraphAnimator;
import com.alike.solutions.NearestNeighbourSolver;
import com.alike.tspgraphsystem.TSPGraph;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class Main extends Application {
    /**
     * The maximum value x value that coordinates are allowed to be given.
     */
    public static final int COORDINATE_MAX_WIDTH = 1800;
    /**
     * The maximum value y value that coordinates are allowed to be given.
     */
    public static final int COORDINATE_MAX_HEIGHT = 900;
    /**
     * The maximum width value the window and canvas can be given.
     */
    public static final int WINDOW_MAX_WIDTH = (int) Math.ceil(COORDINATE_MAX_WIDTH + TSPGraphAnimator.NODE_DIAMETER);
    /**
     * The maximum height value the window and canvas can be given.
     */
    public static final int WINDOW_MAX_HEIGHT =(int) Math.ceil(COORDINATE_MAX_HEIGHT + TSPGraphAnimator.NODE_DIAMETER);
    /**
     * The name of the window.
     */
    private final String STAGE_TITLE = "TSP";
    /**
     * The canvas object used by the application.
     */
    private Canvas canvas;

    private static TSPGraph graph = TSPGraph.generateRandomGraph(1000, false);


    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            try {
                NearestNeighbourSolver nns = new NearestNeighbourSolver(graph);
                nns.runSolution(100);
            } catch (InvalidGraphException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle(STAGE_TITLE);
        Group root = new Group();
        canvas = new Canvas(WINDOW_MAX_WIDTH, WINDOW_MAX_HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, WINDOW_MAX_WIDTH, WINDOW_MAX_HEIGHT);
        stage.setScene(scene);
        stage.show();

        TSPGraphAnimator drawer = new TSPGraphAnimator(graph, canvas, 1);
        drawer.start();
    }
}
