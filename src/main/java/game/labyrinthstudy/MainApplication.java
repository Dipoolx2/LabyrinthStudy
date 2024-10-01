package game.labyrinthstudy;

import game.labyrinthstudy.game.AdjacencyList;
import game.labyrinthstudy.game.GameWindow;
import game.labyrinthstudy.io.FileManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class MainApplication extends Application {

    public FileManager fileManager;

    public static final int CELL_SIZE = 40;
    public static final int MAZE_SIZE = 30;
    public static final int WIDTH = MAZE_SIZE * CELL_SIZE, HEIGHT = MAZE_SIZE * CELL_SIZE;

    @Override
    public void start(Stage stage) {
        this.fileManager = new FileManager();

        // Register canvas to root
        StackPane root = new StackPane();

        // Init scene
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        StackPane pane = new StackPane();
        pane.setBackground(Background.fill(Color.LIGHTGRAY));

        AdjacencyList maze1 = null;
        try {
            maze1 = fileManager.readAdjacencyListFromFile("maze1.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Error: File with name is not found.");
        }

        GameWindow gameWindow = new GameWindow(maze1);
        pane.getChildren().add(gameWindow);

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