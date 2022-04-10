package com.alike.solvers;

import com.alike.graphsystem.DynamicGraph;
import com.alike.solvertestsuite.DynamicSolution;
import com.alike.solvertestsuite.SolverOutput;
import com.alike.graphsystem.Graph;

public interface DynamicSolver {
    Graph getGraph();
    void setGraph(DynamicGraph dgraph);
    SolverOutput startSolving(int delayPerSolve);
    SolverOutput solveForTime(int runTime, int delayPerSolve);
    DynamicSolution calculateSolutions(int numSolves, int delayPerSolve) throws IllegalArgumentException;
}
