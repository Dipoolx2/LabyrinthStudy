package game.labyrinthstudy;

import game.labyrinthstudy.game.*;
import game.labyrinthstudy.graphics.GameWindow;
import game.labyrinthstudy.study.StatsRecorder;
import game.labyrinthstudy.study.StudyFlowManager;
import game.labyrinthstudy.io.FileManager;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainApplication extends Application {

    public FileManager fileManager;
    private PlayerController playerController;
    private AnimationTimer gameLoop;
    private LocationListenerManager locationListenerManager;
    private StudyFlowManager studyFlowManager;

    private Set<TickListener> tickListeners;

    public static final int CELL_SIZE = 100;
    public static final int MAZE_SIZE = 30;
    public static final int WIDTH = 1700, HEIGHT = 900;

    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.tickListeners = new HashSet<>();

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

    public PlayerController activateGameScene(Scene scene, GameWindow gameWindow, Maze maze, StatsRecorder statsRecorder) {
        this.clearGameKeys(scene, this.playerController);
        this.deregisterTickListener(this.playerController);
        this.playerController = new PlayerController(gameWindow, maze.getAdjacencyList().getWallAdjacencyList(), statsRecorder);
        this.playerController.teleport(maze.getStartLocation());
        this.registerGameKeys(scene, this.playerController);

        this.locationListenerManager = new LocationListenerManager(this.playerController);
        this.locationListenerManager.addListener(maze.getEndLocation(), loc -> studyFlowManager.finishMaze());

        this.registerTickListener(this.playerController);

        this.stage.setScene(scene);

        return this.playerController;
    }

    private void deregisterTickListener(TickListener tickListener) {
        if (tickListener != null) {
            this.tickListeners.remove(tickListener);
        }
    }

    private void startMainLoop() {
        // start game
        gameLoop = new GameLoop(this);
        gameLoop.start();
    }

    public void tick() {
        this.tickListeners.forEach(TickListener::tick);

        // Location listener manager removes itself from the list of tick listeners in some occasions.
        // It is not supposed to be in tick listeners for this reason, as it can cause concurrent modification exceptions.
        if (this.locationListenerManager != null) {
            this.locationListenerManager.tick();
        }
    }

    private Maze getMaze(final String fileName) {
        try {
            return fileManager.readMazeFromFile(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Error: File with name is not found.");
            return null;
        }
    }

    public void registerTickListener(TickListener tickListener) {
        this.tickListeners.add(tickListener);
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