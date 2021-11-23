module com.alike {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.base;



    exports com.alike.customexceptions;
    exports com.alike to com.fasterxml.jackson.databind, javafx.graphics;
    exports com.alike.graphical to javafx.graphics;
    exports com.alike.tspgraphsystem to com.fasterxml.jackson.databind;
    exports com.alike.solution_helpers to com.fasterxml.jackson.databind, javafx.graphics;


}

