package com.alike;

import com.alike.customexceptions.*;
import com.alike.graphical.TSPGraphAnimator;
import com.alike.solutions.AntColonyOptimizationSolver;
import com.alike.solutions.BruteForceSolver;
import com.alike.solutions.NearestNeighbourSolver;
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
    public static final int COORDINATE_MAX_WIDTH = 1800;

    /**
     * The maximum value y value that coordinates are allowed to be given.
     */
    public static final int COORDINATE_MAX_HEIGHT = 900;

    /**
     * The maximum width value the window and canvas can be given.
     */
    public static final int WINDOW_MAX_WIDTH = (int) Math.ceil(COORDINATE_MAX_WIDTH + TSPGraphAnimator.NODE_RADIUS*2);

    /**
     * The maximum height value the window and canvas can be given.
     */
    public static final int WINDOW_MAX_HEIGHT = (int) Math.ceil(COORDINATE_MAX_HEIGHT + TSPGraphAnimator.NODE_RADIUS*2);

    /**
     * The name of the window.
     */
    private final String STAGE_TITLE = "TSP";

    private static TSPGraph nnsGraph = TSPGraph.generateRandomGraph(1000, false);
    private static TSPGraph bsGraph = TSPGraph.generateRandomGraph(6, false);
    private static TSPGraph acosGraph = TSPGraph.generateRandomGraph(9, false);


    public static void main(String[] args) {

        // Nearest neighbour solver
//        Thread nnsT = new Thread(() -> {
//            try {
//                NearestNeighbourSolver nns = new NearestNeighbourSolver(nnsGraph);
//                nns.runSolution(100);
//            } catch (InvalidGraphException | InterruptedException | EdgeSuperimpositionException e) {
//                e.printStackTrace();
//            }
//        });
//        nnsT.start();
        // Brute force solver
//        Thread bsT = new Thread(() -> {
//           try {
//               BruteForceSolver bs = new BruteForceSolver(bsGraph);
//               Pair<TSPGraph, Double> solutionOutput = bs.runSolution(100);
//               System.out.println(solutionOutput.getKey());
//               System.out.println(solutionOutput.getValue());
//
//           } catch (PermutationExhaustionException | EdgeSuperimpositionException | PermutationFocusException | NonExistentNodeException | InterruptedException e) {
//               e.printStackTrace();
//           }
//        });
//        bsT.start();
        // Ant Colony Optimisation Solver
        Thread acosT = new Thread(() -> {
            AntColonyOptimizationSolver acos = new AntColonyOptimizationSolver(acosGraph);
            Pair<TSPGraph, Double> solOutput = acos.runSolution();
            System.out.println(solOutput.getKey());
            System.out.println(solOutput.getValue());
        });
        acosT.start();
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

        TSPGraphAnimator drawer = new TSPGraphAnimator(scene, canvas, acosGraph,1);
        drawer.start();
    }
}
