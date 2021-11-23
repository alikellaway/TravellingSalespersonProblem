package com.alike;

import com.alike.customexceptions.*;
import com.alike.graphical.TSPGraphAnimator;
import com.alike.solutions.BruteForceSolver;
import com.alike.tspgraphsystem.TSPGraph;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import javafx.util.Pair;

public class Main extends Application {
    /**
     * The maximum value x value that coordinates are allowed to be given.
     */
    public static final int COORDINATE_MAX_WIDTH = 400;

    /**
     * The maximum value y value that coordinates are allowed to be given.
     */
    public static final int COORDINATE_MAX_HEIGHT = 400;

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

    private static TSPGraph nnsGraph = TSPGraph.generateRandomGraph(100000, false);
    private static TSPGraph bsGraph = TSPGraph.generateRandomGraph(4, false);


    public static void main(String[] args) {

        // Nearest neighbour solver
//        Thread nnsT = new Thread(() -> {
//            try {
//                NearestNeighbourSolver nns = new NearestNeighbourSolver(nnsGraph);
//                nns.runSolution(0);
//            } catch (InvalidGraphException | InterruptedException | EdgeSuperimpositionException e) {
//                e.printStackTrace();
//            }
//        });
//        nnsT.start();
        // Brute force solver
        Thread bsT = new Thread(() -> {
           try {
               BruteForceSolver bs = new BruteForceSolver(bsGraph);
               Pair<TSPGraph, Double> solutionOutput = bs.runSolution(1);
               System.out.println(solutionOutput.getKey());
               System.out.println(solutionOutput.getValue());

           } catch (PermutationExhaustionException | EdgeSuperimpositionException | PermutationFocusException | NonExistentNodeException | InterruptedException e) {
               e.printStackTrace();
           }
        });
        bsT.start();
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle(STAGE_TITLE);
        Group root = new Group();
        Canvas canvas = new Canvas(WINDOW_MAX_WIDTH, WINDOW_MAX_HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, WINDOW_MAX_WIDTH, WINDOW_MAX_HEIGHT);

        stage.setScene(scene);
        stage.show();

        TSPGraphAnimator drawer = new TSPGraphAnimator(scene, canvas, bsGraph,1);
        drawer.start();
    }
}
