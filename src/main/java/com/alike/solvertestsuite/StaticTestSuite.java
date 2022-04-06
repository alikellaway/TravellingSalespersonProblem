package com.alike.solvertestsuite;

import com.alike.customexceptions.CoordinateListException;
import com.alike.customexceptions.NodeSuperimpositionException;
import com.alike.read_write.CoordinateListFileReader;
import com.alike.solvers.StaticSolver;
import com.alike.staticgraphsystem.NodeContainer;
import com.alike.staticgraphsystem.StaticGraph;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class used to test Static travelling salesman solutions against a set of pre-constructed graph problems.
 * StaticTestSuite.java
 * @author alike
 */
public class StaticTestSuite {

    /**
     * The reader object we'll be using to obtain the test graphs.
     */
    private final CoordinateListFileReader reader = new CoordinateListFileReader();

    /**
     * The solver object being tested by this test suite.
     */
    private StaticSolver solver;

    /**
     * Need to keep a reference to the current graph in-case a test was failed while we were trying to solve it.
     * Note: we don't need to keep this for passed tests, since solutions output by solvers already have a reference to
     * the graph they were solved on.
     */
    private StaticGraph currentGraph = null;

    /**
     * The number of the test or test identifier.
     */
    private int testNumber = 0;

    /**
     * Used to create an instance of the StaticTestSuite class.
     * @param solver The solver object we will be testing.
     */
    public StaticTestSuite(StaticSolver solver) {
        setSolver(solver);
    }

    /**
     * Executes the test.
     * @return testSuiteResult A new @code{TestSuiteResult} object containing the information about the test.
     */
    public TestSuiteResult runTest() {
        ArrayList<TestResult> testResults = new ArrayList<>();
        while (reader.hasRemainingLines()) {
            testNumber++;
            try {
                // Create the graph we want to test from file.
                NodeContainer nC = new NodeContainer(reader.getNext());
                StaticGraph graph = new StaticGraph(nC);
                currentGraph = graph;
                solver.setGraph(graph);
                // Try to find a solution for the current graph using the solver; outputs: Solution||Fail
                SolverOutput s = solver.runSolution(0);
                testResults.add(new TestResult(s, testNumber));
                currentGraph = null; // Reset the graph to null (in the case we are running this object b2b) & destroys.
            } catch (IOException | NodeSuperimpositionException e) { // Thrown during file reading or graph creation.
                e.printStackTrace();
            } catch (NullPointerException e) { // reader.getNext() returns null at end of file
                if (testResults.isEmpty()) { // If we have no test results then something went wrong.
                    new CoordinateListException("Test could not find coordinate lists to test with.")
                            .printStackTrace();
                } // Otherwise, nothing went wrong - we can ignore the null pointer.
            }
        }
        return new TestSuiteResult(testResults);
    }

    /**
     * Sets the value of the @code{solver} attribute to a new value.
     * @param solver The new value to assign to the @code{solver} attribute.
     */
    public void setSolver(StaticSolver solver) {
        this.solver = solver;
    }
}
