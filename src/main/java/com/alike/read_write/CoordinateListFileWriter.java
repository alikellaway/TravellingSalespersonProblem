package com.alike.read_write;

import com.alike.Main;
import com.alike.customexceptions.InvalidGraphException;
import com.alike.customexceptions.NodeSuperimpositionException;
import com.alike.customexceptions.RadiusExceedingBoundaryException;
import com.alike.graphsystem.Coordinate;
import com.alike.graphsystem.GraphGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class contains functionality to handle writing graphs into file by constructing lists of coordinate objects.
 * @author alike
 */
public class CoordinateListFileWriter {
    /**
     * The location where the coordinate lists are written to.
     */
    protected static final String FILE_PATH = "generatedGraphs.txt";

    /**
     * The buffered writer object used to write to the file.
     */
    private static BufferedWriter bw;

    /**
     * The number of random graphs that will be generated during the population of the list. It is used inside the po-
     * populate list method.
     */
    private static final int NUM_RANDOM_GRAPHS = 10;

    /**
     * Each polygon shape with the number of corners bigger than 2 and lower or equal to this number will be added to
     * the list when populate is called. e.g. if the corner is five then a triangle, square and pentagon will be added.
     */
    private static final int NUM_REGULAR_POLYGON_GRAPHS = 200;

    /**
     * Similar to @code{NUM_REGULAR_POLYGON_GRAPHS}, irregular polygon shapes with corners bigger than 2 and less than
     * or equal to this number will be added to the list when populate is called.
     */
    private static final int NUM_IRREGULAR_POLYGON_GRAPHS = 10;

    /**
     * Constructs a new instance that can be used to edit the coordinate list file.
     * @throws IOException Thrown if io throws an error.
     */
    public CoordinateListFileWriter() throws IOException {
       bw = new BufferedWriter(new FileWriter(FILE_PATH, true));
    }

    /**
     * Used to append a new coordintae list to the bottom of the coordinate list file.
     * @param cL The new coordinate list to append.
     * @param bw The BufferedWriter to write into.
     * @throws IOException Thrown if an io error is thrown.
     */
    int counter = 1;
    public void appendCoordinateList(BufferedWriter bw, ArrayList<Coordinate> cL) throws IOException {
        System.out.println(counter + " " + Coordinate.coordinateListToStorageFormat(cL) + "\n");
        bw.write(Coordinate.coordinateListToStorageFormat(cL) + "\n");
        counter++;
    }

    /**
     * Use to remove all information from the file at file path.
     * @throws IOException Thrown if an io error is thrown.
     */
    public void clearFile() throws IOException {
        BufferedWriter bw2 = new BufferedWriter(new FileWriter(FILE_PATH, false));
        bw2.write("");
        bw = new BufferedWriter(new FileWriter(FILE_PATH, true));
    }

    /**
     * Used to repopulute the graph list with randomized graphs and the regular graphs.
     * @throws IOException Thrown by the buffered reader.
     * @throws InvalidGraphException Thrown if an attempt is made to create a graph with less than 3 nodes.
     * @throws NodeSuperimpositionException Thrown if an attempt is made to create a graph with superimposed nodes.
     * @throws RadiusExceedingBoundaryException Thrown by @code{generateIrregularPolygonGraph()} if an attempt is made
     * to stretch a polygon out of the display area.
     */
    public void populateFile() throws IOException, InvalidGraphException, NodeSuperimpositionException,
            RadiusExceedingBoundaryException {

        clearFile();
        // Writes random graphs and adds them to the file.
        for (int n = 0; n < NUM_RANDOM_GRAPHS; n++) {
            appendCoordinateList(bw,
                GraphGenerator.generateRandomGraph(n + 3, false).getNodeContainer().getNodeCoordinates()
            );
        }
        int xMax = (int) Main.coordinateMaxWidth;
        int yMax = (int) Main.coordinateMaxHeight;
        // Writes the regular polygon graphs to the file
        for (int n = 0; n < NUM_REGULAR_POLYGON_GRAPHS; n++) {
            appendCoordinateList(bw, GraphGenerator.generateRegularPolygonalGraph(n + 3)
                    .getNodeContainer()
                    .getNodeCoordinates()
            );
        }
        // Writes the irregular polygon graphs to the file
        for (int n = 0; n < NUM_IRREGULAR_POLYGON_GRAPHS; n++) {
            appendCoordinateList(bw, GraphGenerator.generateIrregularPolygonalGraph(
                    n + 3, xMax, yMax
                ).getNodeContainer().getNodeCoordinates()
            );
        }
    }

    /**
     * Closes the buffered writer.
     * @throws IOException Thrown if the bw fails to close.
     */
    public void close() throws IOException {
        bw.close();
    }
}
