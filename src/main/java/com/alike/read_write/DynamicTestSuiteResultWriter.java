package com.alike.read_write;

import com.alike.solvertestsuite.DynamicTestResult;
import com.alike.solvertestsuite.DynamicTestSuiteResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class used to write outputs from dynamic test suites into file.
 * @author alike
 */
public class DynamicTestSuiteResultWriter {
    /**
     * The location at which graphs will be written.
     */
    public static final String FILE_PATH = "dynamicResults.txt";

    /**
     * The buffered writer object used to write into the file.
     */
    private final BufferedWriter bw;

    /**
     * Creates a new instance.
     * @throws IOException Thrown if an I/O error occurs.
     */
    public DynamicTestSuiteResultWriter() throws IOException {
         bw = new BufferedWriter(new FileWriter("dynamicResults.txt", true));
    }

    /**
     * Writes the results stored in the input object into file.
     * @param testSuiteResult The test suite output.
     * @throws IOException Thrown if an I/O error occurs.
     */
    public void writeResults(DynamicTestSuiteResult testSuiteResult) throws IOException {
        ArrayList<DynamicTestResult> results = testSuiteResult.getResults();
        for (DynamicTestResult dtr : results) {
            bw.write(dtr.toStorageFormat() + "\n");
        }
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
