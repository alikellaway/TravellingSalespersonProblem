package com.alike.solvers;

import com.alike.solvertestsuite.Solution;
import com.alike.solvertestsuite.SolverOutput;
import com.alike.staticgraphsystem.StaticGraph;

/**
 * Interface is used to ensure that Solver implementations can all successfully interact with the TestSuite.
 */
public interface Solver {
    /**
     * Method begins the Solver object constructing a Solution to the graph currently stored in it's graph attribute
     * (set with @code{setGraph}.
     * @param delayPerStep The delay between algorithmic decisions in the solution - used to slow a solver down.
     * @return solution A solution object - a collation of data about a test run.
     */
    SolverOutput runSolution(int delayPerStep);

    /**
     * Used to set the Solver's graph attribute - the graph the Solver would currently solver if @code{runSolution} was
     * called.
     * @param graph The graph to assign the Solver's @code{graph} attribute.
     */
    void setGraph(StaticGraph graph);

}
