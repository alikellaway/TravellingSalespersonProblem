package com.alike.solutiontestsuite;

import com.alike.customexceptions.CoordinateListException;
import com.alike.customexceptions.NodeSuperimpositionException;
import com.alike.read_write.CoordinateListFileReader;
import com.alike.solution_helpers.TestResult;
import com.alike.solutions.Solver;
import com.alike.tspgraphsystem.TSPGraph;
import com.alike.tspgraphsystem.TSPNodeContainer;

import java.io.IOException;
import java.util.ArrayList;

public class TestSuite {

    /**
     * The reader object we'll be using to obtain the test graphs.
     */
    private final CoordinateListFileReader reader = new CoordinateListFileReader();

    /**
     * The solver object being tested by this test suite.
     */
    private Solver solver;


    public TestSuite(Solver solver) {
        setSolver(solver);
    }


    public ArrayList<TestResult> runTest() {
        ArrayList<TestResult> results = new ArrayList<>();
        while (reader.hasRemainingLines()) {
            try {
                // Create the graph we want to test from file.
                TSPNodeContainer nC = new TSPNodeContainer(reader.getNext());
                solver.setGraph(new TSPGraph(nC));
                // Run the solution on this graph.
                TestResult r = solver.runSolution(0);
                results.add(r);
            } catch (IOException | NodeSuperimpositionException e) {
                e.printStackTrace();
            } catch (NullPointerException e) { // Get next will return null as its last argument
                if (results.isEmpty()) { // If we have no results then something went wrong.
                    try {
                        throw new CoordinateListException("Test could not find coordinate lists to test with.");
                    } catch (CoordinateListException ex) {
                        e.printStackTrace();
                    }
                } // Otherwise, nothing went wrong - we can ignore the null pointer.
            }
        }
        return results;
    }

    /**
     * Sets the value of the @code{solver} attribute to a new value.
     * @param solver The new value to assign to the @code{solver} attribute.
     */
    public void setSolver(Solver solver) {
        this.solver = solver;
    }
}
