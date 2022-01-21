package com.alike;

import com.alike.customexceptions.InvalidGraphException;
import com.alike.customexceptions.NodeSuperimpositionException;
import com.alike.customexceptions.RadiusExceedingBoundaryException;
import com.alike.dtspgraphsystem.DTSPGraph;
import com.alike.dtspgraphsystem.EdgeStateManager;
import com.alike.read_write.CoordinateListFileWriter;
import com.alike.solutions.BruteForceSolver;
import com.alike.solutions.HilbertFractalCurveSolver;
import com.alike.solutions.NearestNeighbourSolver;
import com.alike.solvertestsuite.TestSuite;
import com.alike.solvertestsuite.TestSuiteResult;
import com.alike.tspgraphsystem.TSPGraphGenerator;

import java.io.IOException;
import java.util.ArrayList;

public class Main2 {

    public static void main(String[] args) throws IOException, NodeSuperimpositionException, InvalidGraphException, RadiusExceedingBoundaryException {
        // Uncomment the below to repopulate the graph files.
//        CoordinateListFileWriter cw = new CoordinateListFileWriter();
//        cw.populateFile();
//        cw.close();
////
//        TestSuite suite = new TestSuite(new NearestNeighbourSolver());
//        TestSuiteResult tsr = suite.runTest();
//        System.out.println(tsr.getTestsFailed() + " " + tsr.getTestsPassed());

        DTSPGraph dG = new DTSPGraph(TSPGraphGenerator.generateRandomGraph(5, false),true, true);



    }
}