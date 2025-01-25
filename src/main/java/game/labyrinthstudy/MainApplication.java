package game.labyrinthstudy;

import game.labyrinthstudy.game.*;
import game.labyrinthstudy.graphics.GameWindow;
import game.labyrinthstudy.hud.GameSceneWrapper;
import game.labyrinthstudy.hud.StudyFlowManager;
import game.labyrinthstudy.io.FileManager;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

public class MainApplication extends Application {

    public FileManager fileManager;
    private PlayerController playerController;
    private AnimationTimer gameLoop;
    private LocationListenerManager locationListenerManager;
    private StudyFlowManager studyFlowManager;

    public static final int CELL_SIZE = 100;
    public static final int MAZE_SIZE = 30;
    public static final int WIDTH = 1700, HEIGHT = 900;

    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        this.fileManager = new FileManager();
        this.studyFlowManager = new StudyFlowManager(this);

        Maze maze1 = getMaze("maze1.txt");
        Maze maze2 = getMaze("maze2.txt");
        assert(maze1 != null && maze2 != null);

        List<Maze> mazes = Arrays.asList(maze2, maze1);
        studyFlowManager.start(mazes);

        // Set window properties
        stage.setTitle("Labyrinth study");
        stage.setResizable(false);

        startMainLoop();
        stage.show();
    }

    public void activateGameScene(Scene scene, GameWindow gameWindow, Maze maze) {
        this.clearGameKeys(scene, this.playerController);

        this.playerController = new PlayerController(gameWindow, maze.getAdjacencyList().getWallAdjacencyList());
        this.playerController.teleport(maze.getStartLocation());

        this.locationListenerManager = new LocationListenerManager(this.playerController);
        this.locationListenerManager.addListener(maze.getEndLocation(), loc -> studyFlowManager.finishMaze());
        this.registerGameKeys(scene, this.playerController);

        this.stage.setScene(scene);
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