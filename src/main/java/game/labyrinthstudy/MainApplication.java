package game.labyrinthstudy;

import game.labyrinthstudy.game.AdjacencyList;
import game.labyrinthstudy.game.GameLoop;
import game.labyrinthstudy.game.Maze;
import game.labyrinthstudy.graphics.GameLayerPane;
import game.labyrinthstudy.graphics.GameWindow;
import game.labyrinthstudy.game.PlayerController;
import game.labyrinthstudy.io.FileManager;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class MainApplication extends Application {

    public FileManager fileManager;
    private PlayerController playerController;
    private AnimationTimer gameLoop;

    public static final int CELL_SIZE = 70;
    public static final int MAZE_SIZE = 30;
    public static final int WIDTH = 1700, HEIGHT = 900;

    @Override
    public void start(Stage stage) {
        this.fileManager = new FileManager();

        // Register canvas to root
        StackPane root = new StackPane();

        // Init scene
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        Maze maze = getMaze("maze2.txt");
        assert(maze != null);

        GameWindow gameWindow = new GameWindow(maze);
        GameLayerPane gameLayerPane = new GameLayerPane(gameWindow);

        root.getChildren().add(gameLayerPane);
        scene.setRoot(root);

        // Set window properties
        stage.setTitle("Labyrinth study");
        stage.setScene(scene);
        stage.setResizable(false);

        // Events and controllers
        this.playerController = new PlayerController(gameWindow, maze.getAdjacencyList().getWallAdjacencyList());
        this.playerController.teleport(maze.getStartLocation());

        registerEvents(scene);

        startMainLoop();

        stage.show();
    }

    private void startMainLoop() {
        // start game
        gameLoop = new GameLoop(this);
        gameLoop.start();
    }

    public void tick() {
        playerController.tick();
    }

    private Maze getMaze(final String fileName) {
        try {
            return fileManager.readMazeFromFile(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Error: File with name is not found.");
            return null;
        }
    }

    private void registerEvents(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, this.playerController.keyPressed());
        scene.addEventHandler(KeyEvent.KEY_RELEASED, this.playerController.keyReleased());
    }

    public static void main(String[] args) {
        launch();
    }
}