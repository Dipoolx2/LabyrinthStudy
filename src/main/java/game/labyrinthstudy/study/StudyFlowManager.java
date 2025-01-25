package game.labyrinthstudy.study;

import game.labyrinthstudy.MainApplication;
import game.labyrinthstudy.TickListener;
import game.labyrinthstudy.game.Maze;
import game.labyrinthstudy.graphics.GameWindow;
import game.labyrinthstudy.gui.GameHudPane;
import game.labyrinthstudy.gui.GameScene;

import java.util.*;

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

    public void finishMaze(boolean gaveUp) {
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
        StatsRecorder statsRecorder = new StatsRecorder();
        GameScene newGameScene = createGameScene(maze, statsRecorder);

        app.activateGameScene(newGameScene, newGameScene.getGameWindow(), maze, statsRecorder);
        this.currentMaze = maze;

        statsRecorder.startRecordings();
        return statsRecorder;
    }

    public GameScene createGameScene(Maze maze, StatsRecorder statsRecorder) {
        GameWindow gameWindow = new GameWindow(maze);
        return new GameScene( gameWindow, new GameHudPane(statsRecorder, this));
    }

    @Override
    public void tick() {
        if (this.currentMaze == null) return;
        StatsRecorder recorder = this.recorders.get(this.currentMaze);
        recorder.updateTime();
    }
}
