package com.alike.solvers;

import com.alike.dynamicgraphsystem.DynamicGraph;
import com.alike.solvertestsuite.SolverOutput;
import com.alike.staticgraphsystem.Graph;

public interface DynamicSolver {
    Graph getGraph();
    void setGraph(DynamicGraph dgraph);
    SolverOutput runSolution(int solveTime, int delayPerStep);
}
