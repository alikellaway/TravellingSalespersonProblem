package com.alike.solutiontestsuite;

import com.alike.read_write.CoordinateListFileReader;
import com.alike.solutions.Solver;

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

    /**
     * Sets the value of the @code{solver} attribute to a new value.
     * @param solver The new value to assign to the @code{solver} attribute.
     */
    public void setSolver(Solver solver) {
        this.solver = solver;
    }
}
