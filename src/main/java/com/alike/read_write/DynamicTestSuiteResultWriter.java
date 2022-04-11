package com.alike.read_write;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DynamicTestSuiteResultWriter {

    private final BufferedWriter bw;

    public DynamicTestSuiteResultWriter() throws IOException {
         bw = new BufferedWriter(new FileWriter("dynamicResults.txt"), true);
    }
}
