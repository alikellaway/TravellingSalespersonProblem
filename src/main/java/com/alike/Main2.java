package com.alike;

import com.alike.customexceptions.*;
import com.alike.read_write.DynamicTestSuiteResultWriter;
import com.alike.solvers.DynamicAntColonyOptimisationSolver;
import com.alike.graphsystem.DynamicGraph;
import com.alike.graphsystem.GraphGenerator;
import com.alike.read_write.GraphWriter;
import com.alike.solvers.DynamicHilbertFractalCurveSolver;
import com.alike.solvers.DynamicNearestNeighbourSolver;
import com.alike.solvers.HilbertFractalCurveSolver;
import com.alike.solvertestsuite.DynamicTestResult;
import com.alike.solvertestsuite.DynamicTestSuite;
import com.alike.solvertestsuite.DynamicTestSuiteResult;

import java.io.IOException;

/**
 * Another space for program entry - used for testing and such.
 */
public class Main2 {
    public static void main(String[] args) throws IOException, NodeSuperimpositionException, InvalidGraphException, RadiusExceedingBoundaryException, EdgeToSelfException {
        // Uncomment the below to repopulate the graph files.
//        CoordinateListFileWriter cw = new CoordinateListFileWriter();
//        cw.populateFile();
//        cw.close();
////
//        StaticTestSuite suite = new StaticTestSuite(new NearestNeighbourSolver());
//        TestSuiteResult tsr = suite.runTest();
//        System.out.println(tsr.getTestsFailed() + " " + tsr.getTestsPassed());

//        DynamicGraph dG = new DynamicGraph(GraphGenerator.generateRandomGraph(5, false),true, true);
//        Node n1 = new Node(0,0);
//        Node n2 = new Node(5,5);
//
//        Edge e = new Edge(n1, n2);
////        System.out.println(e.containsNode(new Node(0, 0)));
//        boolean[] arr = new boolean[5];
//        for (boolean b : arr) {
//            System.out.println(b);
//        }
        // Create some new graphs and write them in.
//        GraphWriter gw = new GraphWriter();
//        gw.clearFile();
//        for (int i = 3; i <= 1003; i++) {
//            DynamicGraph g = GraphGenerator.generateRandomDynamicGraph(i, 10, false, false);
//            gw.writeGraph(g);
//        }
//        gw.close();

        // Set some test parameters.
        int numSolves = 100;
        int delayPerSolve = 20;
        int nodeSpeed = 6; // Original speed was 3 over 30ms, however 9 over 10ms is the same result but in less time.
        boolean randomMovement = false;
        boolean velocityMovement = true;

        // Initialise test tools.
        DynamicTestSuite dts = new DynamicTestSuite();
        DynamicTestSuiteResultWriter dtsrw = new DynamicTestSuiteResultWriter();
//        dtsrw.clearFile();

        // Test and write in hilbert curve.
        System.out.println("Commencing hilbert curve test.");
        DynamicTestSuiteResult hilbertRestuls = dts.testSolver(new DynamicHilbertFractalCurveSolver(), numSolves, delayPerSolve, nodeSpeed, randomMovement, velocityMovement);
        System.out.println("Hilbert: " + hilbertRestuls.getAverageAvgRoute() + " in " + hilbertRestuls.getAverageAvgTime());
        dtsrw.writeResults(hilbertRestuls);
        dtsrw.close(); // Write the results in so progress is saved.
        Runtime.getRuntime().gc();

        // Test and write in Acos
        dtsrw = new DynamicTestSuiteResultWriter();
        System.out.println("Commencing ACO test.");
        DynamicAntColonyOptimisationSolver dacos = new DynamicAntColonyOptimisationSolver();
        DynamicTestSuiteResult antResults = dts.testSolver(dacos, numSolves, delayPerSolve, nodeSpeed, randomMovement, velocityMovement);
        System.out.println("ACO: " + antResults.getAverageAvgRoute() + " in " + antResults.getAverageAvgTime());
        dacos.kill(); // Needs to be killed to shutdown the thread pool
        dtsrw.writeResults(antResults);
        dtsrw.close();
        Runtime.getRuntime().gc();

        // Test and write in nns
        dtsrw = new DynamicTestSuiteResultWriter();
        System.out.println("Commencing NNS test.");
        DynamicTestSuiteResult nnsResults = dts.testSolver(new DynamicNearestNeighbourSolver(), numSolves, delayPerSolve, nodeSpeed, randomMovement, velocityMovement);
        System.out.println("NNS: " + nnsResults.getAverageAvgRoute() + " in " + nnsResults.getAverageAvgTime());
        dtsrw.writeResults(nnsResults);
        dtsrw.close();
        Runtime.getRuntime().gc();
    }
}