package com.alike.read_write;

import com.alike.graphsystem.Graph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Writes graphs in storage format into text files at the path FILE_PATH.
 * @author alike
 */
public class GraphWriter {
    /**
     * The location at which graphs will be written.
     */
    public static final String FILE_PATH = "graphs.txt";

    /**
     * The @code{BufferedWriter} object used to write into the file.
     */
    private final BufferedWriter bw;

    /**
     * Creates a new GraphWriter object.
     * @throws IOException Thrown if the file does not exist.
     */
    public GraphWriter() throws IOException {
        bw = new BufferedWriter(new FileWriter(FILE_PATH, true));
    }

    /**
     * Used to append a new coordintae list to the bottom of the coordinate list file.
     * @param graph The graph to be written.
     * @throws IOException Thrown if an I/O error is thrown.
     */
    public void writeGraph(Graph graph) throws IOException {
        bw.write(graph.toStorageFormat(';') + "\n");
    }

    /**
     * Use to remove all information from the file at file path.
     * @throws IOException Thrown if an io error is thrown.
     */
    public void clearFile() throws IOException {
        BufferedWriter bw2 = new BufferedWriter(new FileWriter(FILE_PATH, false));
        bw2.write("");
        bw2.close();
    }

    /**
     * Closes the buffered writer.
     * @throws IOException Thrown if the bw fails to close.
     */
    public void close() throws IOException {
        bw.close();
    }

    /**
     * Flushes the @code{bw}'s stream.
     * @throws IOException Throws if an I/O error occurs.
     */
    public void flush() throws IOException {
        bw.flush();
    }
}
