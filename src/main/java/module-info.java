module com.alike {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.base;
    requires org.junit.jupiter.api;
    requires org.junit.platform.commons;


    exports com.alike.customexceptions;
    exports com.alike to com.fasterxml.jackson.databind, javafx.graphics;
    exports com.alike.graphical to javafx.graphics;
    exports com.alike.solution_helpers to com.fasterxml.jackson.databind, javafx.graphics;
    exports com.alike.solvers;
    exports com.alike.solvertestsuite to com.fasterxml.jackson.databind, javafx.graphics;
    exports com.alike.graphsystem;
    exports com.alike.time to com.fasterxml.jackson.databind, javafx.graphics;
}

