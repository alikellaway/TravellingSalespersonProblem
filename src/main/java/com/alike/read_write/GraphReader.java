package com.alike.read_write;

import com.alike.customexceptions.NodeSuperimpositionException;
import com.alike.graphsystem.DynamicGraph;
import com.alike.graphsystem.Graph;
import com.alike.graphsystem.StaticGraph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GraphReader {
    private BufferedReader br;

    public GraphReader() throws FileNotFoundException {
        br = new BufferedReader(new FileReader(GraphWriter.FILE_PATH));
    }

    public DynamicGraph getNextGraph(int speed, boolean moveRandomly, boolean moveVelocity) throws IOException, NodeSuperimpositionException {
        String graphString = br.readLine();
        return DynamicGraph.fromStorageFormat(graphString, ';', speed, moveRandomly, moveVelocity);
    }

    public StaticGraph getNextGraph() throws IOException, NodeSuperimpositionException {
        return getNextGraph(0,false,false).getUnderlyingGraph();
    }


    /**
     * Closes the reader object.
     * @throws IOException Thrown if an I/O error occurs.
     */
    public void close() throws IOException {
        br.close();
    }
}
