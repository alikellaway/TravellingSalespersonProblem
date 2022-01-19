package com.alike.solutions;

import com.alike.solvertestsuite.Solution;
import com.alike.tspgraphsystem.TSPGraph;

public interface Solver {

    Solution runSolution(int delayPerStep);

    void setGraph(TSPGraph graph);

}
