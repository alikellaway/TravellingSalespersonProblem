package com.alike.dynamicsolvers;

import com.alike.dynamicgraphsystem.DynamicGraph;
import com.alike.solvers.HilbertFractalCurveSolver;
import com.alike.staticgraphsystem.StaticGraph;

public class DynamicHilbertFractalCurveSolver {
    /**
     * A reference to the @code{HilbertFractalCurveSolver} object that will be repeatedly solving the graph.
     */
    private HilbertFractalCurveSolver hfcs;

    /**
     * The @code{DynamicGraph} that this object will be solving.
     */
    private DynamicGraph dgraph;

    /**
     * A reference to the underlying graph of the @code{dgraph} attribute.
     */
    private StaticGraph graph;

    public DynamicHilbertFractalCurveSolver(DynamicGraph dgraph) {
        setGraph(dgraph.getUnderlyingGraph());
        setDgraph(dgraph);
        setHfcs(new HilbertFractalCurveSolver(getGraph()));
    }

    public HilbertFractalCurveSolver getHfcs() {
        return hfcs;
    }

    public void setHfcs(HilbertFractalCurveSolver hfcs) {
        this.hfcs = hfcs;
    }

    public DynamicGraph getDgraph() {
        return dgraph;
    }

    public void setDgraph(DynamicGraph dgraph) {
        this.dgraph = dgraph;
    }

    public StaticGraph getGraph() {
        return graph;
    }

    public void setGraph(StaticGraph graph) {
        this.graph = graph;
    }
}
