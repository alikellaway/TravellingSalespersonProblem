package com.alike;

import com.alike.customexceptions.EdgeToSelfException;
import com.alike.customexceptions.InvalidGraphException;
import com.alike.customexceptions.NodeSuperimpositionException;
import com.alike.customexceptions.RadiusExceedingBoundaryException;
import com.alike.tspgraphsystem.TSPEdge;
import com.alike.tspgraphsystem.TSPNode;

import java.io.IOException;

public class Main2 {

    public static void main(String[] args) throws IOException, NodeSuperimpositionException, InvalidGraphException, RadiusExceedingBoundaryException, EdgeToSelfException {
        // Uncomment the below to repopulate the graph files.
//        CoordinateListFileWriter cw = new CoordinateListFileWriter();
//        cw.populateFile();
//        cw.close();
////
//        TestSuite suite = new TestSuite(new NearestNeighbourSolver());
//        TestSuiteResult tsr = suite.runTest();
//        System.out.println(tsr.getTestsFailed() + " " + tsr.getTestsPassed());

//        DTSPGraph dG = new DTSPGraph(TSPGraphGenerator.generateRandomGraph(5, false),true, true);
        TSPNode n1 = new TSPNode(0,0);
        TSPNode n2 = new TSPNode(5,5);

        TSPEdge e = new TSPEdge(n1, n2);
        System.out.println(e.containsNode(new TSPNode(0, 0)));


    }
}