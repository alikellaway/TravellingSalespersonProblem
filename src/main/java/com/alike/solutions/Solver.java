package com.alike.solutions;

import com.alike.customexceptions.PermutationFocusException;
import com.alike.tspgraphsystem.TSPGraph;
import javafx.util.Pair;

public interface Solver {
    Pair<TSPGraph, Double> runSolution(int delayPerStep);
}
