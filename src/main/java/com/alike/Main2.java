package com.alike;

import com.alike.customexceptions.*;
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
        GraphWriter gw = new GraphWriter();
        gw.clearFile();
        for (int i = 0; i < 10; i++) {
            DynamicGraph g = GraphGenerator.generateRandomDynamicGraph(10, 10, false, false);
            gw.writeGraph(g);
        }
        gw.close();
        int numSolves = 10;
        int delayPerSovle = 30;
        int nodeSpeed = 10;
        boolean randomMovement = false;
        boolean velocityMovement = true;

        DynamicTestSuite dts = new DynamicTestSuite();
        DynamicTestSuiteResult hilbertRestuls = dts.testSolver(new DynamicHilbertFractalCurveSolver(), numSolves, delayPerSovle, nodeSpeed, randomMovement, velocityMovement);
        DynamicAntColonyOptimisationSolver dacos = new DynamicAntColonyOptimisationSolver();
        DynamicTestSuiteResult antResults = dts.testSolver(dacos, numSolves, delayPerSovle, nodeSpeed, randomMovement, velocityMovement);
        DynamicTestSuiteResult nnsResults = dts.testSolver(new DynamicNearestNeighbourSolver(), numSolves, delayPerSovle, nodeSpeed, randomMovement, velocityMovement);
        System.out.println("ACO: " + antResults.getAverageAvgRoute() + " in " + antResults.getAverageAvgTime());
        System.out.println("Hilbert: " + hilbertRestuls.getAverageAvgRoute() + " in " + hilbertRestuls.getAverageAvgTime());
        System.out.println("NNS: " + nnsResults.getAverageAvgRoute() + " in " + nnsResults.getAverageAvgTime());
        dacos.kill();
    }
}