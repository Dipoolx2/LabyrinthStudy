package game.labyrinthstudy.gui;

import game.labyrinthstudy.MainApplication;
import game.labyrinthstudy.game.Maze;
import game.labyrinthstudy.graphics.GameLayerPane;
import game.labyrinthstudy.graphics.GameWindow;
import game.labyrinthstudy.program.Recorder;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import java.util.*;

import static game.labyrinthstudy.MainApplication.HEIGHT;
import static game.labyrinthstudy.MainApplication.WIDTH;

public class StudyFlowManager {

    private final MainApplication app;
    private final Queue<Maze> mazes;
    private final Map<Maze, Recorder> recorders;

    private Maze currentMaze;

    public StudyFlowManager(MainApplication app) {
        this.app = app;

        this.mazes = new ArrayDeque<>();
        this.recorders = new HashMap<>();
        this.currentMaze = null;
    }

    public void start(Collection<Maze> mazes) {
        System.out.println("Starting with mazes: " + mazes);

        this.mazes.addAll(mazes);
        mazes.forEach(m -> this.recorders.put(m, new Recorder(m)));

        System.out.println("After adding (queue): " + mazes);
        System.out.println("After adding (map): " + this.recorders);

        if (this.mazes.isEmpty()) {
            this.finishStudy();
        }

        Maze firstMaze = this.mazes.remove();

        System.out.println("After removing first maze (" + firstMaze + "): " + this.mazes);
        Recorder recorder = this.recorders.get(firstMaze);

        this.startMazeAndRecordings(firstMaze, recorder);
    }

    private void finishStudy() {
        System.out.println("No more mazes left, finish study");
    }

    public void finishMaze() {
        System.out.println("Finished maze");

        Recorder recorder = this.recorders.get(currentMaze);
        recorder.stopRecordings();
        recorder.saveRecordings();

        if (!mazes.isEmpty()) {
            System.out.println("Mazes is not empty");
            Maze nextMaze = this.mazes.remove();
            System.out.println("Next maze is ");
            Recorder nextMazeRecorder = this.recorders.get(nextMaze);
            this.startMazeAndRecordings(nextMaze, nextMazeRecorder);
        }

        finishStudy();
    }

    private void startMazeAndRecordings(Maze maze, Recorder recorder) {
        GameSceneWrapper newGameScene = createGameScene(maze);

        app.activateGameScene(newGameScene.getGameScene(), newGameScene.getGameWindow(), maze);
        this.currentMaze = maze;

        recorder.startRecordings();
        System.out.println("Currently playing maze: " + maze);
    }

    public GameSceneWrapper createGameScene(Maze maze) {
        StackPane root = new StackPane();
        Scene gameScene = new Scene(root, WIDTH, HEIGHT);

        GameWindow gameWindow = new GameWindow(maze);
        GameLayerPane gameLayerPane = new GameLayerPane(gameWindow);
        root.getChildren().add(gameLayerPane);

        return new GameSceneWrapper(gameScene, gameWindow);
    }

}
