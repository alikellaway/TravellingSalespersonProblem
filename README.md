# TravellingSalespersonProblem

This is a project containing code able to represent and solve travelling salesperson problems. It also contains code able to repersent and solve dynamic travelling salesman problems - TSPs where nodes are non-stationary.

# How to Use
The Main method (found in src/main/java/com.alike/main.java) has an attribute "mode" - this attribute can be switched to one of the enumerator values.

Depending on the value of "mode", the main function will change the programs behaviours to solve TSPs with a requested method.

# Raw Data
Raw data can be found in the file TravellingSalespersonProblem/dynamicResults.txt

# Test Set
The test graphs can be found in the dynamicTestGraphs.txt file.

The file contains the test dynamic graphs in the format:
node1_x,node1_y;node2_x,node2_y;node3_x,node3_y ... ;; node1StartVelocity_x,node1StartVelocity_y;node2StartVelocity_x,node2StartVelocity_y;node3StartVelocity_x,node3StartVelocity_y ..

A demonstration of how to read the graphs back from file can ge found in DyanmicTestSuite.java