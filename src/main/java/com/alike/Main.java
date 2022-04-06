package com.alike;

import com.alike.customexceptions.*;
import com.alike.dynamicgraphsystem.DynamicGraph;
import com.alike.dynamicsolvers.DynamicAntColonyOptimisationSolver;
import com.alike.dynamicsolvers.DynamicHilbertFractalCurveSolver;
import com.alike.dynamicsolvers.DynamicNearestNeighbourSolver;
import com.alike.graphical.HilbertFractalCurveAnimator;
import com.alike.graphical.TSPGraphAnimator;
import com.alike.solution_helpers.RepeatedFunctions;
import com.alike.solvers.*;
import com.alike.solvertestsuite.DynamicSolution;
import com.alike.solvertestsuite.SolverOutput;
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
    public static final int COORDINATE_MAX_WIDTH = 1024;

    /**
     * The maximum value y value that coordinates are allowed to be given.
     */
    public static final int COORDINATE_MAX_HEIGHT = 1024;

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

    /* Each of these is a graph that's the subject of a solver in the use case examples. */
    private static StaticGraph nnsGraph; // Nearest Neighbour graph
    private static StaticGraph bsGraph; // Brute force search graph
    private static StaticGraph acosGraph; // Ant colony optimisation graph
    private static StaticGraph hfcsGraph; // Hilbert fractal curve graph
    private static StaticGraph csGraph; // Christofies solver graph
    private static DynamicGraph dnnsGraph; // Dynamic nearest neighbour
    private static DynamicGraph dacosGraph; // Dynamic ant colony optimisation
    private static DynamicGraph dhfcsGraph; // Dynamic hilbert fractal curve.

    private static HilbertFractalCurveSolver hfcs; // Need to be able to see for graph animator
    private static DynamicNearestNeighbourSolver dnns;
    private static DynamicAntColonyOptimisationSolver dacos;
    private static DynamicHilbertFractalCurveSolver dhfcs;

    private static StaticGraph currentG = new StaticGraph(); // This graph was used for cylcing through node sets.

    public static void main(String[] args) throws InvalidGraphException, NodeSuperimpositionException, IOException, RadiusExceedingBoundaryException {
        // Generate some graphs for testing and general use
        nnsGraph = GraphGenerator.generateRandomGraph(10, false);
        bsGraph = GraphGenerator.generateRandomGraph(8, false);
        acosGraph = GraphGenerator.generateRandomGraph(500, false);
        hfcsGraph = GraphGenerator.generateRandomGraph(100, false);
        csGraph = GraphGenerator.generateRandomGraph(100, false);
        dnnsGraph = new DynamicGraph(nnsGraph, false, true);
        dacosGraph = new DynamicGraph(acosGraph,
                false, true);
        dhfcsGraph = new DynamicGraph(hfcsGraph, true, true);

        /* Here is a list of example use cases of the solver methods. */

         /* Nearest neighbour solver. */
//        Thread nnsT = new Thread(() -> {
//            NearestNeighbourSolver nns = new NearestNeighbourSolver(nnsGraph);
//            nns.runSolution(100);
//        });
//        nnsT.start();

        /* Brute force solver. */
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

        /* Ant Colony Optimisation StaticSolver. */
//        Thread acosT = new Thread(() -> {
//            AntColonyOptimizationSolver acos = new AntColonyOptimizationSolver(acosGraph);
//            Solution solOutput = acos.runSolution(0);
////            System.out.println(solOutput.getKey());
////            System.out.println(solOutput.getValue());
//        });
//        acosT.start();

        /* Hilbert fractal curve solver. */
//        Thread hfcsT = new Thread(() -> {
//            hfcs = new HilbertFractalCurveSolver(hfcsGraph);
//            SolverOutput pf = hfcs.runSolution(10);
//        });
//        hfcsT.start();

        /* Christofide's algorithm solver. */
//        Thread csT = new Thread(() -> {
//            ChristofidesSolver cs = new ChristofidesSolver(csGraph);
//            cs.runSolution(0);
//        });
//        csT.start();

        /* Dynamic Nearest Neighbour StaticSolver. */
//        Thread dnnsT = new Thread(() -> {
//            dnns = new DynamicNearestNeighbourSolver(dnnsGraph);
//            dnns.runSolution(100);
//        });
//        dnnsT.start();

        /* Dynamic Ant Colony Optimisation StaticSolver. */
        Thread dacosT = new Thread(() -> {
            dacos = new DynamicAntColonyOptimisationSolver(dacosGraph);
            dacos.runSolution(10);
        });
        dacosT.start();

        /* Dynamic Hilbert Curve StaticSolver. */
//        Thread dhcsT = new Thread(() -> {
//            dhfcs = new DynamicHilbertFractalCurveSolver(dhfcsGraph);
//            SolverOutput ds = dhfcs.runSolution(4000, 0);
//            System.out.println(ds.toString());
//        });
//        dhcsT.start();

        /* Example of using the test suite to test */
//        Thread test = new Thread(() -> {
//            try {
//                // Use this code to repopulate the generated graphs file.
//                CoordinateListFileWriter clfw = new CoordinateListFileWriter();
//                clfw.populateFile();
//                clfw.close();
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
        //        hfcs = new HilbertFractalCurveSolver(); // Generates a curve
//        while (hfcs == null) {
//            RepeatedFunctions.sleep(10);
//        }
//        while (dhfcs == null) {
//            RepeatedFunctions.sleep(1);
//        }
        launch(args);
        dacos.kill();
        dacosGraph.kill();
//        dhfcs.kill();
//        dhfcsGraph.kill();

    }

    @Override
    public void start(Stage stage) throws InterruptedException, NonSquareCanvasException, InvalidGraphException, NodeSuperimpositionException {
        // Give the name to the window.
        stage.setTitle(STAGE_TITLE);

        Group root = new Group();
        Scene scene = new Scene(root, WINDOW_MAX_WIDTH, WINDOW_MAX_HEIGHT);
        scene.setFill(BACK_GROUND_COLOR);

        // We need a canvas to display the graphs.
        Canvas canvas = new Canvas(WINDOW_MAX_WIDTH, WINDOW_MAX_HEIGHT);
        // If displaying hilbert curves, we need a canvas for that too.
        Canvas canvas1 = new Canvas(WINDOW_MAX_WIDTH, WINDOW_MAX_HEIGHT);
//        HilbertFractalCurveAnimator curveDrawer = new HilbertFractalCurveAnimator(canvas, dhfcs.getHfcs());
        TSPGraphAnimator graphDrawer = new TSPGraphAnimator(stage, canvas1, acosGraph,1, false);
        root.getChildren().add(canvas);
        root.getChildren().add(canvas1);
        graphDrawer.start();
//        curveDrawer.start();
        stage.setScene(scene);
        stage.show();
    }
}
