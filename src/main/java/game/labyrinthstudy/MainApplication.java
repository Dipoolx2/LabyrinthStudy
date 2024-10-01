package game.labyrinthstudy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainApplication extends Application {

    public static final int WIDTH = 1400, HEIGHT = 800;

    @Override
    public void start(Stage stage) {
        // Register canvas to root
        StackPane root = new StackPane();

        // Init scene
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        Pane pane = new Pane();
        pane.setBackground(Background.fill(Color.BLACK));
        scene.setRoot(pane);

        // Set window properties
        stage.setTitle("Labyrinth study");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}