package com.alike;

import com.alike.customexceptions.*;
import com.alike.graphical.HilbertFractalCurveAnimator;
import com.alike.graphical.TSPGraphAnimator;
import com.alike.read_write.CoordinateListFileReader;
import com.alike.read_write.CoordinateListFileWriter;
import com.alike.solutions.*;
import com.alike.tspgraphsystem.Coordinate;
import com.alike.tspgraphsystem.TSPGraph;
import com.alike.tspgraphsystem.TSPGraphGenerator;
import com.alike.tspgraphsystem.TSPNodeContainer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {
    /**
     * The maximum value x value that coordinates are allowed to be given.
     */
    public static final int COORDINATE_MAX_WIDTH = 1000;

    /**
     * The maximum value y value that coordinates are allowed to be given.
     */
    public static final int COORDINATE_MAX_HEIGHT = 512;

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

    /**
     * The color of the window background.
     */
    private static final Color BACK_GROUND_COLOR = Color.rgb(35,35,35);

    private static TSPGraph nnsGraph = TSPGraphGenerator.generateRandomGraph(500, false);
    private static TSPGraph bsGraph = TSPGraphGenerator.generateRandomGraph(12, false);
    private static TSPGraph acosGraph = TSPGraphGenerator.generateRandomGraph(100, false);
    private static TSPGraph hfcsGraph = TSPGraphGenerator.generateRandomGraph(50, false);
    private static TSPGraph csGraph = TSPGraphGenerator.generateRandomGraph(100, false);

    private static HilbertFractalCurveSolver hfcs; // Need to be able to see for graph animator

    private static TSPGraph currentG = new TSPGraph();

    public static void main(String[] args) throws InvalidGraphException, NodeSuperimpositionException, IOException {

        // Nearest neighbour solver
//        Thread nnsT = new Thread(() -> {
//            try {
//                NearestNeighbourSolver nns = new NearestNeighbourSolver(nnsGraph);
//                nns.runSolution(0);
//            } catch (InvalidGraphException | InterruptedException | EdgeSuperimpositionException | EdgeToSelfException e) {
//                e.printStackTrace();
//            }
//        });
//        nnsT.start();
        // Brute force solver
//        Thread bsT = new Thread(() -> {
//           try {
//               BruteForceSolver bs = new BruteForceSolver(bsGraph);
//               Pair<TSPGraph, Double> solutionOutput = bs.runSolution(50);
//               System.out.println(solutionOutput.getKey());
//               System.out.println(solutionOutput.getValue());
//
//           } catch (PermutationExhaustionException | EdgeSuperimpositionException | PermutationFocusException | NonExistentNodeException | InterruptedException | EdgeToSelfException e) {
//               e.printStackTrace();
//           }
//        });
//        bsT.start();
        // Ant Colony Optimisation Solver
//        Thread acosT = new Thread(() -> {
//            AntColonyOptimizationSolver acos = new AntColonyOptimizationSolver(acosGraph);
//            Pair<TSPGraph, Double> solOutput = acos.runSolution(1000, 10);
//            System.out.println(solOutput.getKey());
//            System.out.println(solOutput.getValue());
//        });
//        acosT.start();

        // Hilbert fractal curve solver
//        Thread hfcsT = new Thread(() -> {
//            try {
//                hfcs = new HilbertFractalCurveSolver(hfcsGraph);
//                hfcs.runSolution(10);
//            } catch (NonSquareCanvasException | EdgeSuperimpositionException | NodeMissedException | InterruptedException | FractalDensityFailure e) {
//                e.printStackTrace();
//            }
//        });
//        hfcsT.start();
        // Christofide's algorithm solver
//        Thread csT = new Thread(() -> {
//            try {
//                ChristofidesSolver cs = new ChristofidesSolver(csGraph);
//                cs.runSolution(0);
//            } catch (EdgeSuperimpositionException | EdgeToSelfException | NonExistentNodeException | InterruptedException | NodeSuperimpositionException | NoClosestNodeException e) {
//                e.printStackTrace();
//            }
//        });
//        csT.start();
        // Populate our graph file with the test graphs incl. random graphs, polygon graphs and irregular polygon graphs
        Thread test = new Thread(() -> {
            try {
                // Use this code to repopulate the generated graphs file.
//                CoordinateListFileWriter clfw = new CoordinateListFileWriter();
//                clfw.populateFile();
//                clfw.close();
                CoordinateListFileReader clfr = new CoordinateListFileReader();
                while (true) {
                    Thread.sleep(0);
                    ArrayList<Coordinate> cL = clfr.getNext();
                    Coordinate[] cA = cL.toArray(Coordinate[]::new);
                    currentG.setNodeContainer(new TSPNodeContainer(cA));
                    currentG.getEdgeContainer().clear();
                    NearestNeighbourSolver nns2 = new NearestNeighbourSolver(currentG);
                    nns2.runSolution(0);
                }
            } catch(CoordinateListExhaustionException ignored) {

            } catch ( NodeSuperimpositionException | IOException | InvalidGraphException | InterruptedException | EdgeSuperimpositionException | EdgeToSelfException e) {
                e.printStackTrace();
            }
        });
        test.start();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws InterruptedException, NonSquareCanvasException, InvalidGraphException, NodeSuperimpositionException {
        stage.setTitle(STAGE_TITLE);
        Group root = new Group();

        Scene scene = new Scene(root, WINDOW_MAX_WIDTH, WINDOW_MAX_HEIGHT);
        scene.setFill(BACK_GROUND_COLOR);

        Canvas canvas = new Canvas(WINDOW_MAX_WIDTH, WINDOW_MAX_HEIGHT);
        Canvas canvas1 = new Canvas(WINDOW_MAX_WIDTH, WINDOW_MAX_HEIGHT);
//        while (hfcs == null) {
//            Thread.sleep(10);
//        }
//        HilbertFractalCurveAnimator curveDrawer = new HilbertFractalCurveAnimator(canvas, hfcs);

        TSPGraphAnimator graphDrawer = new TSPGraphAnimator(canvas1, currentG,1, false);

        root.getChildren().add(canvas);
        root.getChildren().add(canvas1);
        graphDrawer.start();
//        curveDrawer.start();
        stage.setScene(scene);
        stage.show();
    }
}
