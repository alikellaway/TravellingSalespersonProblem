package com.alike.solvers;

import com.alike.graphsystem.DynamicGraph;
import com.alike.solvertestsuite.DynamicSolution;
import com.alike.solvertestsuite.SolverOutput;
import com.alike.graphsystem.Graph;

public interface DynamicSolver {
    Graph getGraph();
    void setGraph(DynamicGraph dgraph);

    /**
     * Sets a dynamic solver into a state where it will continuously solve it's DTSP until it's running attribute is
     * set to false.
     * @param delayPerSolve The time the solver should wait before starting the next solve.
     * @return DSolution A @code{DSolution} containing information about the solver's solving.
     */
    SolverOutput startSolving(int delayPerSolve);

    /**
     * Sets a dynamic solver into a state where it will solve a DTSP until a certain time has elapsed.
     * @param runTime The time for which the solver should solve.
     * @param delayPerSolve The time the solver should wait between each solve.
     * @return DSolution A @code{DSolution} containing information about the solver's solving.
     */
    SolverOutput solveForTime(int runTime, int delayPerSolve);

    /**
     * Sets a dynamic solver into a state where it will solve a DTSP a certain number of times after which it will
     * terminate.
     * @param numSolves The number of solves the solver should compute before termintation.
     * @param delayPerSolve The time the solver should wait between each solve.
     * @return DSolution A @code{DSolution} object that contains information about the solver's solving.
     * @throws IllegalArgumentException Thrown if an 0 or less is input as the numSolve parameter's value.
     */
    DynamicSolution calculateSolutions(int numSolves, int delayPerSolve) throws IllegalArgumentException;
}
