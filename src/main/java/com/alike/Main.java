package com.alike;

import com.alike.customexceptions.*;
import com.alike.dynamicgraphsystem.DynamicGraph;
import com.alike.graphical.TSPGraphAnimator;
import com.alike.solvers.*;
import com.alike.staticgraphsystem.GraphGenerator;
import com.alike.staticgraphsystem.StaticGraph;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    /**
     * The maximum value x value that coordinates are allowed to be given.
     */
    public static final int COORDINATE_MAX_WIDTH = 900;

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

    /**
     * The color of the window background.
     */
    private static final Color BACK_GROUND_COLOR = Color.rgb(35,35,35);

    private static StaticGraph nnsGraph;
    private static StaticGraph bsGraph;
    private static StaticGraph acosGraph;
    private static StaticGraph hfcsGraph;
    private static StaticGraph csGraph;
    private static StaticGraph polygonGraph;
    private static DynamicGraph dnnsGraph;

    private static HilbertFractalCurveSolver hfcs; // Need to be able to see for graph animator
    private static DynamicNearestNeighbourSolver dnns;

    private static StaticGraph currentG = new StaticGraph(); // This graph was used for cylcing through node sets.

    public static void main(String[] args) throws InvalidGraphException, NodeSuperimpositionException, IOException, RadiusExceedingBoundaryException {
        // Generate some graphs for testing and general use
        nnsGraph = GraphGenerator.generateRandomGraph(10, false);
        bsGraph = GraphGenerator.generateRandomGraph(8, false);
        acosGraph = GraphGenerator.generateRandomGraph(600, false);
        hfcsGraph = GraphGenerator.generateRandomGraph(50, false);
        csGraph = GraphGenerator.generateRandomGraph(100, false);
        polygonGraph = GraphGenerator.generateIrregularPolygonalGraph(9, 150, 200);
        dnnsGraph = new DynamicGraph(nnsGraph, false, true);


        // Point the mover to the appropriate graph

        /* Here is a list of example use cases of the solver methods. */
//         Nearest neighbour solver
//        Thread nnsT = new Thread(() -> {
//            NearestNeighbourSolver nns = new NearestNeighbourSolver(nnsGraph);
//            nns.runSolution(100);
//        });
//        nnsT.start();
        // Brute force solver
//        Thread bsT = new Thread(() -> {
//           try {
//               BruteForceSolver bs = new BruteForceSolver(bsGraph);
//               Pair<StaticGraph, Double> solutionOutput = bs.runSolution(0);
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
//            Solution solOutput = acos.runSolution(0);
////            System.out.println(solOutput.getKey());
////            System.out.println(solOutput.getValue());
//        });
//        acosT.start();

        // Hilbert fractal curve solver
//        Thread hfcsT = new Thread(() -> {
//            try {
//                hfcs = new HilbertFractalCurveSolver(hfcsGraph);
//                hfcs.runSolution(10);
//            } catch (NonSquareCanvasException | EdgeSuperimpositionException | NodeMissedException | InterruptedException | FractalDensityException e) {
//                e.printStackTrace();
//            }
//        });
//        hfcsT.start();
        // Christofide's algorithm solver
//        Thread csT = new Thread(() -> {
//            ChristofidesSolver cs = new ChristofidesSolver(csGraph);
//            cs.runSolution(0);
//        });
//        csT.start();
        // DNearset Neighbour Solver
        Thread dnnsT = new Thread(() -> {
            dnns = new DynamicNearestNeighbourSolver(dnnsGraph);
            dnns.runSolution(100);
        });
        dnnsT.start();
        // Populate our graph file with the test graphs incl. random graphs, polygon graphs and irregular polygon graphs
//        Thread test = new Thread(() -> {
//            try {
//                // Use this code to repopulate the generated graphs file.
////                CoordinateListFileWriter clfw = new CoordinateListFileWriter();
////                clfw.populateFile();
////                clfw.close();
//                CoordinateListFileReader clfr = new CoordinateListFileReader();
//                while (true) {
//                    RepeatedFunctions.sleep(0);
//                    ArrayList<Coordinate> cL = clfr.getNext();
//                    Coordinate[] cA = cL.toArray(Coordinate[]::new);
//                    currentG.setNodeContainer(new NodeContainer(cA));
//                    currentG.getEdgeContainer().clear();
//                    NearestNeighbourSolver nns2 = new NearestNeighbourSolver(currentG);
//                    nns2.runSolution(0);
//                }
//            } catch(CoordinateListException ignored) {
//
//            } catch ( NodeSuperimpositionException | IOException | InvalidGraphException | InterruptedException | EdgeSuperimpositionException | EdgeToSelfException e) {
//                e.printStackTrace();
//            }
//        });
//        test.start();
//        DynamicGraph dg = new DynamicGraph(acosGraph, true, true);
//        dg.move();
        launch(args);
        dnns.kill();
        dnnsGraph.kill();

    }

    @Override
    public void start(Stage stage) throws InterruptedException, NonSquareCanvasException, InvalidGraphException, NodeSuperimpositionException {
        stage.setTitle(STAGE_TITLE);
        Group root = new Group();

        Scene scene = new Scene(root, WINDOW_MAX_WIDTH, WINDOW_MAX_HEIGHT);
        scene.setFill(BACK_GROUND_COLOR);

        Canvas canvas = new Canvas(WINDOW_MAX_WIDTH, WINDOW_MAX_HEIGHT);
        Canvas canvas1 = new Canvas(WINDOW_MAX_WIDTH, WINDOW_MAX_HEIGHT);
        // Wait for the curve to be created - there is a waiting time while the path is being generated 1st time
//        while (hfcs == null) {
//        RepeatedFunctions.sleep(10);
//        }
//        HilbertFractalCurveAnimator curveDrawer = new HilbertFractalCurveAnimator(canvas, hfcs);
        TSPGraphAnimator graphDrawer = new TSPGraphAnimator(stage, canvas1, dnnsGraph.getUnderlyingGraph(),1, false);

        root.getChildren().add(canvas);
        root.getChildren().add(canvas1);
        graphDrawer.start();
//        curveDrawer.start();
        stage.setScene(scene);
        stage.show();
    }
}
