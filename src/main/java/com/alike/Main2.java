package com.alike;

import com.alike.customexceptions.InvalidGraphException;
import com.alike.customexceptions.NodeSuperimpositionException;
import com.alike.solutions.BruteForceSolver;
import com.alike.solvertestsuite.TestSuite;

import java.io.IOException;

public class Main2 {

    private static final String filePath = "graphs.txt";
    public static void main(String[] args) throws IOException, NodeSuperimpositionException, InvalidGraphException {
        TestSuite suite = new TestSuite(new BruteForceSolver());
        System.out.println(suite.runTest());
    }
}