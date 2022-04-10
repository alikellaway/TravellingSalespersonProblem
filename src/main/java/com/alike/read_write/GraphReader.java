package com.alike.read_write;

import com.alike.customexceptions.NodeSuperimpositionException;
import com.alike.graphsystem.DynamicGraph;
import com.alike.graphsystem.Graph;
import com.alike.graphsystem.StaticGraph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Reads graphs written by the GraphWriter class.
 * @author alike
 */
public class GraphReader {
    /**
     * The @code{BufferedReader} object used to read the file.
     */
    private BufferedReader br;

    /**
     * Creates a new @code{GraphReader} object.
     * @throws FileNotFoundException Thrown if the file is not found.
     */
    public GraphReader() throws FileNotFoundException {
        br = new BufferedReader(new FileReader(GraphWriter.FILE_PATH));
    }

    /**
     * Outputs a Dynamic graph using the information from the next line and the input parameters.
     * @param speed The speed the coordinate mover should be given.
     * @param moveRandomly Whether the nodes should be moved randomly.
     * @param moveVelocity Whether the nodes should move by velocity.
     * @return DG A dynamic graph containing nodes of coordinates found on the line and initial velocities if given.
     * @throws IOException Thrown if an I/O error occurs.
     * @throws NodeSuperimpositionException Thrown if an attempt is made to superimpose nodes.
     */
    public DynamicGraph getNextGraph(int speed, boolean moveRandomly, boolean moveVelocity) throws IOException, NodeSuperimpositionException {
        String graphString = br.readLine();
        return DynamicGraph.fromStorageFormat(graphString, ';', speed, moveRandomly, moveVelocity);
    }

    /**
     * Outputs a @code{StaticGraph} using the information from the next line.
     * @return g A StaticGraph with nodes of coordinates given on the line.
     * @throws IOException Thrown if an I/O error occurs.
     * @throws NodeSuperimpositionException Thrown if an attempt is made to superimpose nodes.
     */
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

    /**
     * Returns the value of the @code{br} attribute.
     * @return br The value of the @code{br} attribute.
     */
    public BufferedReader getBr() {
        return br;
    }
}
