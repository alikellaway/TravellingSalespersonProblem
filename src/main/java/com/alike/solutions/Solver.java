package com.alike.solutions;

import com.alike.customexceptions.PermutationFocusException;
import com.alike.solution_helpers.TestResult;
import com.alike.tspgraphsystem.TSPGraph;
import javafx.util.Pair;

public interface Solver {
    TestResult runSolution(int delayPerStep);
}
