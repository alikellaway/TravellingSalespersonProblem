package com.alike;

import com.alike.customexceptions.*;
import com.alike.graphsystem.DynamicGraph;
import com.alike.graphsystem.GraphGenerator;
import com.alike.graphsystem.StaticGraph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    }
}