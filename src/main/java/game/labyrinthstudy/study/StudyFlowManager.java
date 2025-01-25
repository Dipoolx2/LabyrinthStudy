package game.labyrinthstudy.study;

import game.labyrinthstudy.MainApplication;
import game.labyrinthstudy.TickListener;
import game.labyrinthstudy.game.Maze;
import game.labyrinthstudy.game.PlayerController;
import game.labyrinthstudy.graphics.GameLayerPane;
import game.labyrinthstudy.graphics.GameWindow;
import game.labyrinthstudy.gui.GameSceneWrapper;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import java.util.*;

import static game.labyrinthstudy.MainApplication.HEIGHT;
import static game.labyrinthstudy.MainApplication.WIDTH;

public class StudyFlowManager implements TickListener {

    private final MainApplication app;

    private final Queue<Maze> mazes;
    private final Map<Maze, StatsRecorder> recorders;

    private Maze currentMaze;

    public StudyFlowManager(MainApplication app) {
        this.app = app;

        this.mazes = new ArrayDeque<>();
        this.recorders = new HashMap<>();
        this.currentMaze = null;
    }

    public void start(Collection<Maze> mazes) {
        this.mazes.addAll(mazes);
        if (this.mazes.isEmpty()) {
            this.finishStudy();
        }

        Maze firstMaze = this.mazes.remove();
        StatsRecorder firstRecorder = this.startMazeAndRecordings(firstMaze);
        this.recorders.put(firstMaze, firstRecorder);
    }

    private void finishStudy() {

    }

    public void finishMaze() {
        StatsRecorder statsRecorder = this.recorders.get(currentMaze);
        statsRecorder.stopRecordings();
        statsRecorder.saveRecordings();

        if (!mazes.isEmpty()) {
            Maze nextMaze = this.mazes.remove();
            StatsRecorder newRecorder = this.startMazeAndRecordings(nextMaze);
            this.recorders.put(nextMaze, newRecorder);
            return;
        }

        finishStudy();
    }

    private StatsRecorder startMazeAndRecordings(Maze maze) {
        GameSceneWrapper newGameScene = createGameScene(maze);

        StatsRecorder statsRecorder = new StatsRecorder();

        app.activateGameScene(newGameScene.getGameScene(), newGameScene.getGameWindow(), maze, statsRecorder);
        this.currentMaze = maze;

        statsRecorder.startRecordings();
        return statsRecorder;
    }

    public GameSceneWrapper createGameScene(Maze maze) {
        StackPane root = new StackPane();
        Scene gameScene = new Scene(root, WIDTH, HEIGHT);

        GameWindow gameWindow = new GameWindow(maze);
        GameLayerPane gameLayerPane = new GameLayerPane(gameWindow);
        root.getChildren().add(gameLayerPane);

        return new GameSceneWrapper(gameScene, gameWindow);
    }

    @Override
    public void tick() {
        if (this.currentMaze == null) return;
        StatsRecorder recorder = this.recorders.get(this.currentMaze);
        recorder.updateTime();
    }
}
