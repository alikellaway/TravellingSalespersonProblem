package com.alike.dynamicsolvers;

import com.alike.dynamicgraphsystem.DynamicGraph;
import com.alike.solvertestsuite.SolverOutput;
import com.alike.staticgraphsystem.Graph;

public interface DynamicSolver {
    Graph getGraph();
    void setGraph(DynamicGraph dgraph);
    SolverOutput startSolving(int delayPerSolve);
    SolverOutput solveForTime(int runTime, int delayPerSolve);
    SolverOutput calculateSolutions(int numSolves, int delayPerSolve) throws IllegalArgumentException;
}
