package com.alike;

import com.alike.customexceptions.*;
import com.alike.graphsystem.DynamicGraph;
import com.alike.solvers.DynamicAntColonyOptimisationSolver;
import com.alike.solvers.DynamicHilbertFractalCurveSolver;
import com.alike.solvers.DynamicNearestNeighbourSolver;
import com.alike.graphical.HilbertFractalCurveAnimator;
import com.alike.graphical.TSPGraphAnimator;
import com.alike.solution_helpers.RepeatedFunctions;
import com.alike.solvers.*;
import com.alike.graphsystem.GraphGenerator;
import com.alike.graphsystem.StaticGraph;
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
    public static final int COORDINATE_MAX_WIDTH = 512;

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
    private static String stageTitle = "TSP";

    /**
     * The color of the window background.
     */
    private static final Color BACK_GROUND_COLOR = Color.rgb(35,35,35);

    /**
     * A reference to the graph that is currently being solved.
     */
    private static StaticGraph activeGraph;

    /**
     * A reference to the dynamic graph that might currently be being solved.
     */
    private static DynamicGraph dactiveGraph;

    /**
     * References to solvers that need to be accessed from the main start method.
     */
    private static HilbertFractalCurveSolver hfcs;
    private static DynamicNearestNeighbourSolver dnns;
    private static DynamicAntColonyOptimisationSolver dacos;
    private static DynamicHilbertFractalCurveSolver dhfcs;

    /**
     * enum used to determine which solver will be used during the program run.
     */
    private enum Mode {
        NNS, // Solve statically with nearest neighbour algorithm.
        BF, // Solve statically with brute force algorithm.
        ACO, // Solve statically with ant colony optimisation algorithm.
        HFC, // Solve statically with hilbert fractal curve heuristic.
        CA, // Solve statically with Christofide's algorithm (non-functional).
        DNNS, // Solve dynamically with nearest neighbour algorithm.
        DACO, // Solve dynamically with ant colony optimisation algorithm.
        DHFC // Solve dynamically with hilbert fractal curve heuristic.
    }

    /**
     * This value switches which 'mode' the program is in i.e. which solver is used to solve the graph.
     */
    private static final Mode mode = Mode.DACO;

    public static void main(String[] args) throws InvalidGraphException, NodeSuperimpositionException, IOException, RadiusExceedingBoundaryException {

        // Generate the graph - NB: This code is suboptimal and an adaptation of prior spaghettis
        switch (mode) {
            case NNS -> activeGraph = GraphGenerator.generateRandomGraph(10, false);
            case BF -> activeGraph = GraphGenerator.generateRandomGraph(8, false);
            case ACO -> activeGraph = GraphGenerator.generateRandomGraph(1000, false);
            case HFC -> activeGraph = GraphGenerator.generateRandomGraph(100, false);
            case CA -> activeGraph = GraphGenerator.generateRandomGraph(101, false);
            case DNNS -> {
                activeGraph = GraphGenerator.generateRandomGraph(50, false);
                dactiveGraph = new DynamicGraph(activeGraph, false, true);
            }
            case DACO -> {
                activeGraph = GraphGenerator.generateRandomGraph(5, false);
                dactiveGraph = new DynamicGraph(activeGraph,false, true);
            }
            case DHFC -> {
                activeGraph = GraphGenerator.generateRandomGraph(310, false);
                dactiveGraph = new DynamicGraph(activeGraph, false, true);
            }
        }
        /* I am making a change */

        /* Here is a list of example use cases of the solver methods. */
        switch (mode) {
            /* Nearest neighbour solver. */
            case NNS -> {
                Thread nnsT = new Thread(() -> {
                    NearestNeighbourSolver nns = new NearestNeighbourSolver(activeGraph);
                    nns.runSolution(100);
                });
                nnsT.start();
            }
            /* Brute force solver. */
            case BF -> {
                Thread bsT = new Thread(() -> {
                    BruteForceSolver bs = new BruteForceSolver(activeGraph);
                    bs.runSolution(0);
                });
                bsT.start();
            }
            /* Ant Colony Optimisation StaticSolver. */
            case ACO -> {
                Thread acosT = new Thread(() -> {
                    AntColonyOptimisationSolver acos = new AntColonyOptimisationSolver(activeGraph);
                    acos.runSolution(0);
                });
                acosT.start();
            }
            /* Hilbert fractal curve solver. */
            case HFC -> {
                Thread hfcsT = new Thread(() -> {
                    hfcs = new HilbertFractalCurveSolver(activeGraph);
                    hfcs.runSolution(10);
                });
                hfcsT.start();
            }
            /* Christofide's algorithm solver. */
            case CA -> {
                Thread csT = new Thread(() -> {
                    ChristofidesSolver cs = new ChristofidesSolver(activeGraph);
                    cs.runSolution(0);
                });
                csT.start();
            }
            /* Dynamic Nearest Neighbour solver. */
            case DNNS -> {
                Thread dnnsT = new Thread(() -> {
                    dnns = new DynamicNearestNeighbourSolver(dactiveGraph);
                    dnns.startSolving(10);
                });
                dnnsT.start();
            }
            /* Dynamic Ant Colony Optimisation solver. */
            case DACO -> {
                Thread dacosT = new Thread(() -> {
                    dacos = new DynamicAntColonyOptimisationSolver(dactiveGraph);
                    dacos.calculateSolutions(1000000, 0);
                });
                dacosT.start();
            }
            /* Dynamic Hilbert Curve solver. */
            case DHFC -> {
                Thread dhcsT = new Thread(() -> {
                    dhfcs = new DynamicHilbertFractalCurveSolver(dactiveGraph);
                    dhfcs.calculateSolutions(10,1000);
                });
                dhcsT.start();
            }
        }

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

        // We have to wait for the hilbert curve to be constructed before we can do anything else.
        switch (mode) {
            case DHFC -> {
                while (dhfcs == null) {
                    RepeatedFunctions.sleep(1);
                }
            }
            case HFC -> {
                while (hfcs == null) {
                    RepeatedFunctions.sleep(1);
                }
            }
        }
        // Start the window
        launch(args);
        // Kill the graph and the solver after we close the program window.
        switch (mode) {
            case DACO -> {
                dacos.kill();
                dactiveGraph.kill();
            }
            case DHFC -> {
                dhfcs.kill();
                dactiveGraph.kill();
            }
            case DNNS -> {
                dnns.kill();
                dactiveGraph.kill();
            }
        }
    }

    @Override
    public void start(Stage stage) throws InterruptedException, NonSquareCanvasException, InvalidGraphException, NodeSuperimpositionException {
        // Give the name to the window.
        stage.setTitle(stageTitle);
        Group root = new Group();
        Scene scene = new Scene(root, WINDOW_MAX_WIDTH, WINDOW_MAX_HEIGHT);
        scene.setFill(BACK_GROUND_COLOR);
        // We need a canvas to display the graphs.
        Canvas graphCanvas = new Canvas(WINDOW_MAX_WIDTH, WINDOW_MAX_HEIGHT);
        // If displaying hilbert curves, we need a canvas for that too.
        Canvas curveCanvas = new Canvas(WINDOW_MAX_WIDTH, WINDOW_MAX_HEIGHT);
        // Add the canvases to the group
        root.getChildren().add(curveCanvas);
        root.getChildren().add(graphCanvas);
        // Start the graph drawer thread.
        TSPGraphAnimator graphDrawer = new TSPGraphAnimator(stage, graphCanvas, activeGraph,1, false);
        graphDrawer.start();
        // Start the curve drawer thread (if needed).
        if (mode == Mode.DHFC) {
            HilbertFractalCurveAnimator curveDrawer = new HilbertFractalCurveAnimator(curveCanvas, dhfcs.getHfcs());
            curveDrawer.start();
        } else if (mode == Mode.HFC) {
            HilbertFractalCurveAnimator curveDrawer = new HilbertFractalCurveAnimator(curveCanvas, hfcs);
            curveDrawer.start();
        }
        // Display
        stage.setScene(scene);
        stage.show();
    }
}
