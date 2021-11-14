package com.alike;

import com.alike.customexceptions.EdgeSuperimpositionException;
import com.alike.customexceptions.NodeSuperimpositionException;
import com.alike.graphical.TSPGraphAnimator;
import com.alike.tspgraphsystem.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class Main extends Application {

    public static final int COORDINATE_MAX_WIDTH = 500;
    public static final int COORDINATE_MAX_HEIGHT = 500;
    public static final int WINDOW_MAX_WIDTH = (int) Math.ceil(COORDINATE_MAX_WIDTH + TSPGraphAnimator.NODE_DIAMETER);
    public static final int WINDOW_MAX_HEIGHT =(int) Math.ceil(COORDINATE_MAX_HEIGHT + TSPGraphAnimator.NODE_DIAMETER);
    private final String STAGE_TITLE = "TSP";
    private Canvas canvas;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle(STAGE_TITLE);
        Group root = new Group();
        canvas = new Canvas(WINDOW_MAX_WIDTH, WINDOW_MAX_HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, WINDOW_MAX_WIDTH, WINDOW_MAX_HEIGHT);
        stage.setScene(scene);
        stage.show();

        AnimationTimer drawer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                clearCanvas(canvas.getGraphicsContext2D());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TSPGraphAnimator.drawRandomGraph(canvas.getGraphicsContext2D());
            }
        };
//        TSPGraphAnimator drawer = new TSPGraphAnimator(graph, canvas, 1);
        drawer.start();
    }


    private void clearCanvas(GraphicsContext gc) {
        gc.clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());
    }
}
