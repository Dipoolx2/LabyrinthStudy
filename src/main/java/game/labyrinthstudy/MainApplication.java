package game.labyrinthstudy;

import game.labyrinthstudy.game.*;
import game.labyrinthstudy.graphics.GameLayerPane;
import game.labyrinthstudy.graphics.GameWindow;
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
    private LocationListenerManager locationListenerManager;
    private ProgramFlowManager programFlowManager;

    public static final int CELL_SIZE = 70;
    public static final int MAZE_SIZE = 30;
    public static final int WIDTH = 1700, HEIGHT = 900;

    @Override
    public void start(Stage stage) {
        this.fileManager = new FileManager();
        this.programFlowManager = new ProgramFlowManager();

        // Register canvas to root
        StackPane root = new StackPane();

        // Init scene
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        Maze maze = getMaze("maze2.txt");
        assert(maze != null);

        this.setGameView(scene, root, maze);

        // Set window properties
        stage.setTitle("Labyrinth study");
        stage.setScene(scene);
        stage.setResizable(false);

        registerGameKeys(scene, this.playerController);
        startMainLoop();

        stage.show();
    }

    public void setGameView(Scene scene, StackPane root, Maze maze) {
        this.clearGameKeys(scene, this.playerController);

        GameWindow gameWindow = new GameWindow(maze);
        GameLayerPane gameLayerPane = new GameLayerPane(gameWindow);

        root.getChildren().clear();
        root.getChildren().add(gameLayerPane);

        this.playerController = new PlayerController(gameWindow, maze.getAdjacencyList().getWallAdjacencyList());
        this.playerController.teleport(maze.getStartLocation());

        this.locationListenerManager = new LocationListenerManager(this.playerController);
        this.locationListenerManager.addListener(maze.getEndLocation(), loc -> programFlowManager.finishMaze(maze));
    }

    private void startMainLoop() {
        // start game
        gameLoop = new GameLoop(this);
        gameLoop.start();
    }

    public void tick() {
        if (playerController != null)
            playerController.tick();
        if (locationListenerManager != null)
            locationListenerManager.tick();
    }

    private Maze getMaze(final String fileName) {
        try {
            return fileManager.readMazeFromFile(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Error: File with name is not found.");
            return null;
        }
    }

    private void registerGameKeys(Scene scene, PlayerController playerController) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, playerController.keyPressed());
        scene.addEventHandler(KeyEvent.KEY_RELEASED, playerController.keyReleased());
    }

    private void clearGameKeys(Scene scene, PlayerController playerController) {
        if (playerController != null && playerController.keyPressed() != null) {
            scene.removeEventHandler(KeyEvent.KEY_PRESSED, playerController.keyPressed());
        }

        if (playerController != null && playerController.keyReleased() != null) {
            scene.removeEventHandler(KeyEvent.KEY_RELEASED, playerController.keyReleased());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}