package com.alike.read_write;

import com.alike.Main;
import com.alike.customexceptions.InvalidGraphException;
import com.alike.customexceptions.NodeSuperimpositionException;
import com.alike.tspgraphsystem.Coordinate;
import com.alike.tspgraphsystem.TSPGraphGenerator;

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
     * pulate list method.
     */
    private static final int NUM_RANDOM_GRAPHS = 1000;

    /**
     * Each polygon shape with the number of corners bigger than 2 and lower or equal to this number will be added to
     * the list when populate is called. e.g. if the corner is five then a triangle, square and pentagon will be added.
     */
    private static final int NUM_REGULAR_POLYGON_GRAPHS = 100;

    /**
     * Similar to @code{NUM_REGULAR_POLYGON_GRAPHS}, irregular polygon shapes with corners bigger than 2 and less than
     * or equal to this number will be added to the list when populate is called.
     */
    private static final int NUM_IRREGULAR_POLYGON_GRAPHS = 100;

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
     * @throws IOException Thrown if an io error is thrown.
     */
    public void appendCoordinateList(BufferedWriter bw, ArrayList<Coordinate> cL) throws IOException {
        bw.write(Coordinate.coordinateListToStorageFormat(cL) + "\n");
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


    public void populateFile() throws IOException, InvalidGraphException, NodeSuperimpositionException {
        clearFile();
        // Writes 1000 random graphs and adds them to the file.
        for (int n = 3; n <= NUM_RANDOM_GRAPHS; n++) {
            appendCoordinateList(bw,
                TSPGraphGenerator.generateRandomGraph(n, false).getNodeContainer().getNodeCoordinates()
            );
        }
        int xMax = Main.COORDINATE_MAX_WIDTH;
        int yMax = Main.COORDINATE_MAX_HEIGHT;
        // Writes the regular polygon graphs to the file
        for (int n = 3; n <= NUM_REGULAR_POLYGON_GRAPHS; n++) {
            appendCoordinateList(bw, TSPGraphGenerator.generateRegularPolygonalGraph(n, xMax, yMax)
                    .getNodeContainer()
                    .getNodeCoordinates()
            );
        }
        // Writes the irregular polygon graphs to the file
        for (int n = 3; n <= NUM_IRREGULAR_POLYGON_GRAPHS; n++) {
            appendCoordinateList(bw, TSPGraphGenerator.generateIrregularPolygonalGraph(
                    n, xMax, yMax, xMax/2.0, yMax/2.0
                ).getNodeContainer().getNodeCoordinates()
            );
        }
    }
}