package com.alike.test;

import com.alike.customexceptions.EdgeToSelfException;
import com.alike.graphsystem.Coordinate;
import com.alike.graphsystem.Edge;
import com.alike.graphsystem.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {
    @Test
    public void verifyEdgeIDFunctionality() throws EdgeToSelfException {
        Node n1 = new Node(Coordinate.generateRandomCoordinate(50,50));
        Node n2 = new Node(Coordinate.generateRandomCoordinate(50, 50));
        Edge e1 = new Edge(n1,n2);
        Edge e2 = new Edge(n2, n1);
        Assertions.assertSame(e1.getEdgeID(), e2.getEdgeID());
    }

    @Test
    public void verifyEdgesSymmetrical() throws EdgeToSelfException {
        Node n1 = new Node(Coordinate.generateRandomCoordinate(50,50));
        Node n2 = new Node(Coordinate.generateRandomCoordinate(50, 50));
        Edge e1 = new Edge(n1,n2);
        Edge e2 = new Edge(n2, n1);
        Assertions.assertSame(e1.getLength(), e2.getLength());
    }
}