package com.alike.read_write;

import com.alike.customexceptions.CoordinateListExhaustionException;
import com.alike.tspgraphsystem.Coordinate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class contains the functionality to load graphs from file.
 */
public class CoordinateListFileReader {

    /**
     * The coordinate lists that have been read from file and could be loaded into a graph.
     */
    private ArrayList<ArrayList<Coordinate>> coordinateLists;

    /**
     * The line the class is currently looking at i.e. the line number that is retrieved if getNext is called.
     */
    private int focusLine = 0;

    /**
     * Constructs a new reader object.
     */
    public CoordinateListFileReader() {
    }

    /**
     * Returns the coordinate list stored on the line numbered equal to the focusLine variable.
     * @return CoorinateList The coordinate list stored on the focusLine.
     * @throws IOException Thrown if an io exception occurs.
     * @throws CoordinateListExhaustionException Thrown if the list is empty or if there are no more lines.
     */
    public ArrayList<Coordinate> getNext() throws IOException, CoordinateListExhaustionException {
        BufferedReader br = new BufferedReader(new FileReader(CoordinateListFileWriter.FILE_PATH)); // Grab the file
        // Construct the coordinate list on the currentLine
        int currentLine = 0;
        String line = null;
        while (currentLine <= focusLine && (line = br.readLine()) != null) {
            currentLine++;
        }
        if (line == null) { // If we didnt find one
            if (focusLine == 0) {
                throw new CoordinateListExhaustionException("Coordinate list file empty.");
            }
            throw new CoordinateListExhaustionException("End of file reached.");
        }
        focusLine++;
        return Coordinate.parseCoordinateList(line);
    }

    /**
     * Gets all coordinate lists currently in the file and loads them into ram.
     * @throws IOException Thrown if an io exception occurs.
     */
    private void getAll() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(CoordinateListFileWriter.FILE_PATH)); // Grab the file
        // Construct the coordinate lists from the file.
        String line;
        while ((line = br.readLine()) != null) {
            ArrayList<Coordinate> cL = Coordinate.parseCoordinateList(line);
            coordinateLists.add(cL);
        }
    }
}