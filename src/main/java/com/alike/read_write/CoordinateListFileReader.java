package com.alike.read_write;

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
     * Whether we have lines remaining in the file or not (true if we don't currently know which means we may get a
     * null output from @code{getNext()}).
     */
    private boolean hasRemainingLines = true;


    /**
     * Constructs a new reader object.
     */
    public CoordinateListFileReader() {
    }

    /**
     * Returns the coordinate list stored on the line numbered equal to the focusLine variable.
     * @return CoorinateList The coordinate list stored on the focusLine.
     * @throws IOException Thrown if an io exception occurs.
     */
    public ArrayList<Coordinate> getNext() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(CoordinateListFileWriter.FILE_PATH)); // Grab the file
        // Construct the coordinate list on the currentLine
        int currentLine = 0;
        String line;
        while ((line = br.readLine()) != null) {
            if (currentLine == focusLine) {
                br.close();
                focusLine++;
                return Coordinate.parseCoordinateList(line);
            }
            currentLine++;
        }
        if (currentLine == 0) { // If the file was empty
            br.close();
            hasRemainingLines = false;
        }
        br.close();
        hasRemainingLines = false;
        return null;
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

    /**
     * Returns the value of the @code{hasRemainingLines} attribute.
     * @return hasRemainingLines The value of the @code{hasRemainingLines} attribute.
     */
    public boolean hasRemainingLines() {
        return hasRemainingLines;
    }
}
