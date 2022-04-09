package com.alike.dynamicsolvers;

import com.alike.graphsystem.DynamicGraph;
import com.alike.solvertestsuite.SolverOutput;
import com.alike.graphsystem.Graph;

public interface DynamicSolver {
    Graph getGraph();
    void setGraph(DynamicGraph dgraph);
    SolverOutput startSolving(int delayPerSolve);
    SolverOutput solveForTime(int runTime, int delayPerSolve);
    SolverOutput calculateSolutions(int numSolves, int delayPerSolve) throws IllegalArgumentException;
}
