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
        ArrayList<TestOutput> testResults = new ArrayList<>();
        while (reader.hasRemainingLines()) {
            testNumber++;
            try {
                // Create the graph we want to test from file.
                NodeContainer nC = new NodeContainer(reader.getNext());
                StaticGraph graph = new StaticGraph(nC);
                currentGraph = graph;
                solver.setGraph(graph);
                // Try to run the solution on this graph and output TestPass object.
                Solution s = solver.runSolution(0);
                testResults.add(new TestPass(s, testNumber));
                currentGraph = null; // Reset the graph to null (in the case we are running this object b2b).
            } catch (IOException | NodeSuperimpositionException e) { // Thrown during file reading or graph creation.
                e.printStackTrace();
            } catch (NullPointerException e) { // reader.getNext() returns null at end of file
                if (testResults.isEmpty()) { // If we have no test results then something went wrong.
                    new CoordinateListException("Test could not find coordinate lists to test with.")
                            .printStackTrace();
                } // Otherwise, nothing went wrong - we can ignore the null pointer.
            } catch (Exception e) { // Test failed due to an exception that was none of the above.
                testResults.add(new TestFail(e, testNumber));
            } catch (Error e) { // Test failed due to an error, e.g., ran out of memory.
                testResults.add(new TestFail(e, testNumber));
            }
        }
        return new TestSuiteResult(testResults);
    }

    /**
     * Sets the value of the @code{solver} attribute to a new value.
     * @param solver The new value to assign to the @code{solver} attribute.
     */
    public void setSolver(Solver solver) {
        this.solver = solver;
    }
}
