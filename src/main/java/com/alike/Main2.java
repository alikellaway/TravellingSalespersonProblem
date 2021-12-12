package com.alike;

import com.alike.customexceptions.InvalidGraphException;
import com.alike.customexceptions.NodeSuperimpositionException;
import com.alike.tspgraphsystem.TSPGraph;
import com.alike.tspgraphsystem.TSPGraphGenerator;
import com.alike.tspgraphsystem.TSPNodeContainer;

import java.io.IOException;
import java.util.ArrayList;

public class Main2 {

    private static final String filePath = "graphs.txt";
    public static void main(String[] args) throws IOException, NodeSuperimpositionException {
        // Writes 1000 random graphs
//        ArrayList<TSPNodeContainer> ncs = new ArrayList<>();
//        for (int n = 3; n < 1001; n++) {
//            ncs.add(TSPGraphGenerator.generateRandomGraph(n, false).getNodeContainer());
//        }
//        TSPNodeContainer.writeNodeContainers(ncs, filePath, true);
        // Parse graphs
//        TSPNodeContainer.parseNodeContainers(filePath);
//        TSPGraph g = TSPGraphGenerator.generateRandomGraph(5, false);
//        TSPNodeContainer.writeNodeContainer(g.getNodeContainer(), filePath, true);
        TSPNodeContainer nc = TSPNodeContainer.parseNodeContainer(filePath);
    }
}