package com.alike.solvertestsuite;

import com.alike.customexceptions.CoordinateListException;
import com.alike.customexceptions.NodeSuperimpositionException;
import com.alike.read_write.CoordinateListFileReader;
import com.alike.solvers.Solver;
import com.alike.staticgraphsystem.NodeContainer;
import com.alike.staticgraphsystem.StaticGraph;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class used to test Static travelling salesman solutions against a set of pre-constructed graph problems.
 * TestSuite.java
 * @author alike
 */
public class TestSuite {

    /**
     * The reader object we'll be using to obtain the test graphs.
     */
    private final CoordinateListFileReader reader = new CoordinateListFileReader();

    /**
     * The solver object being tested by this test suite.
     */
    private Solver solver;

    /**
     * Need to keep a reference to the current graph incase a test was failed while we were trying to solve it.
     */
    private StaticGraph currentGraph = null;

    /**
     * The number of the test or test identifier.
     */
    private int testNumber = 0;

    /**
     * Used to create an instance of the TestSuite class.
     * @param solver The solver object we will be testing.
     */
    public TestSuite(Solver solver) {
        setSolver(solver);
    }

    /**
     * Executes the test.
     * @return testSuiteResult A new @code{TestSuiteResult} object containing the information about the test.
     */
    public TestSuiteResult runTest() {
        int testsPassed = 0;
        int testsFailed = 0;
        ArrayList<Solution> solutions = new ArrayList<>();
        while (reader.hasRemainingLines()) {
            testNumber++;
            try {
                // Create the graph we want to test from file.
                NodeContainer nC = new NodeContainer(reader.getNext());
                StaticGraph graph = new StaticGraph(nC);
                currentGraph = graph;
                solver.setGraph(graph);
                // Run the solution on this graph.
                Solution s = solver.runSolution(0);
                s.setTestNumber(testNumber);
                solutions.add(s);
                currentGraph = null;
                if (!s.testFailed()) {
                    testsPassed++;
                    System.out.println(s);
                } else {
                    testsFailed++;
                    System.out.println(s);
                }

            } catch (IOException | NodeSuperimpositionException e) { // Thrown during file reading or graph creation.
                e.printStackTrace();
            } catch (NullPointerException e) { // Get next will return null as its last argument
                if (solutions.isEmpty()) { // If we have no solutions then something went wrong.
                    try {
                        throw new CoordinateListException("Test could not find coordinate lists to test with.");
                    } catch (CoordinateListException ex) {
                        e.printStackTrace();
                    }
                } // Otherwise, nothing went wrong - we can ignore the null pointer.
            } catch (Exception e) {
                Solution s = Solution.createFailedSolution(currentGraph, e);
                s.setTestNumber(testNumber);
                solutions.add(s);
                testsFailed++;
                System.out.println(s);
//                e.printStackTrace();
            }
        }
        return new TestSuiteResult(solutions, testsPassed, testsFailed);
    }

    /**
     * Sets the value of the @code{solver} attribute to a new value.
     * @param solver The new value to assign to the @code{solver} attribute.
     */
    public void setSolver(Solver solver) {
        this.solver = solver;
    }
}
