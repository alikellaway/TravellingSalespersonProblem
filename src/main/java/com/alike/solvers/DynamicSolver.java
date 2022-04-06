package com.alike.solvers;

import com.alike.solvertestsuite.SolverOutput;
import com.alike.staticgraphsystem.Graph;

public interface DynamicSolver {
    Graph getGraph();
    SolverOutput runSolution(int solveTime, int delayPerStep);
}
