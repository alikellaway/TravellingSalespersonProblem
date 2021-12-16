package com.alike;

import com.alike.customexceptions.InvalidGraphException;
import com.alike.customexceptions.NodeSuperimpositionException;
import com.alike.dtspgraphsystem.CoordinateMover;
import com.alike.read_write.CoordinateListFileWriter;
import com.alike.tspgraphsystem.Coordinate;
import com.alike.tspgraphsystem.TSPGraph;
import com.alike.tspgraphsystem.TSPGraphGenerator;
import com.alike.tspgraphsystem.TSPNodeContainer;

import java.io.IOException;
import java.util.ArrayList;

public class Main2 {

    private static final String filePath = "graphs.txt";
    public static void main(String[] args) throws IOException, NodeSuperimpositionException {
        ArrayList<Coordinate> cL = new ArrayList<>();
        cL.add(new Coordinate(100, 200));
        CoordinateMover cm = new CoordinateMover(cL, 2);
        cm.stepByVelocity();
    }
}