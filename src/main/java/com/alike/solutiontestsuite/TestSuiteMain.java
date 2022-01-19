package com.alike.solutiontestsuite;

import com.alike.customexceptions.CoordinateListExhaustionException;
import com.alike.read_write.CoordinateListFileReader;
import com.alike.solutions.Solver;
import com.alike.tspgraphsystem.Coordinate;
import com.alike.tspgraphsystem.TSPGraph;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;


//public class TestSuiteMain {
//    private final CoordinateListFileReader reader = new CoordinateListFileReader();
//
//    public ArrayList<Pair<TSPGraph, Double>> runTestSuite(Solver solver) {
//        while (true) {
//            try {
//                ArrayList<Coordinate> currentList = reader.getNext();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (CoordinateListExhaustionException e) {
//                e.printStackTrace();
//            }
//        }
//        return solver.runSolution(0);
//    }
//}
