package com.alike;

import com.alike.customexceptions.*;
import com.alike.graphsystem.DynamicGraph;
import com.alike.graphsystem.Graph;
import com.alike.graphsystem.GraphGenerator;
import com.alike.graphsystem.StaticGraph;
import com.alike.read_write.GraphReader;
import com.alike.read_write.GraphWriter;

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
        GraphWriter gw = new GraphWriter();
        gw.clearFile();
        for (int i = 0; i < 100; i++) {
            DynamicGraph g = GraphGenerator.generateRandomDynamicGraph(10, 10, false, false);
            gw.writeGraph(g);
        }
        gw.close();
        GraphReader gr = new GraphReader();
        for (int i = 0; i<100; i++) {
            System.out.println(gr.getNextGraph(10, false, true).toStorageFormat(';'));
        }
        gr.close();

    }
}