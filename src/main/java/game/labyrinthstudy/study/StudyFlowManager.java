package game.labyrinthstudy.study;

import game.labyrinthstudy.MainApplication;
import game.labyrinthstudy.TickListener;
import game.labyrinthstudy.game.Maze;
import game.labyrinthstudy.game.PlayerController;
import game.labyrinthstudy.graphics.GameWindow;
import game.labyrinthstudy.gui.GameHudPane;
import game.labyrinthstudy.gui.GameScene;
import game.labyrinthstudy.gui.LandingPageScene;

import java.util.*;

public class StudyFlowManager implements TickListener {

    private final MainApplication app;

    private final Queue<Maze> mazes;
    private final Map<Maze, StatsRecorder> recorders;
    private final Map<Maze, MazeResults> results;
    private final Map<Maze, PlayerController> playerControllers;

    private LandingPageScene landingPageScene;
    private Maze currentMaze;

    public StudyFlowManager(MainApplication app) {
        this.app = app;

        this.mazes = new ArrayDeque<>();
        this.recorders = new HashMap<>();
        this.results = new HashMap<>();
        this.playerControllers = new HashMap<>();

        this.currentMaze = null;
    }

    public void start(Collection<Maze> mazes, Maze practiceMaze) {
        this.landingPageScene = new LandingPageScene(this, mazes, practiceMaze);
        this.app.triviallySetScene(landingPageScene);
    }

    public void startStudy(Collection<Maze> mazes) {
        this.mazes.addAll(mazes);
        if (this.mazes.isEmpty()) {
            this.finishStudy();
        }

        Maze firstMaze = this.mazes.remove();
        StatsRecorder firstRecorder = this.startMazeAndRecordings(firstMaze);
        this.recorders.put(firstMaze, firstRecorder);
    }

    private void finishStudy() {
        for (Map.Entry<Maze, MazeResults> result : this.results.entrySet()) {
            Maze maze = result.getKey();
            result.getValue().computeAllResults(maze, playerControllers.get(maze));
        }
    }

    public void finishMaze(boolean gaveUp, boolean practiceMaze) {
        StatsRecorder statsRecorder = this.recorders.get(currentMaze);
        statsRecorder.stopRecordings();
        if (practiceMaze) {
            this.recorders.remove(currentMaze);
            this.app.triviallySetScene(this.landingPageScene);
            currentMaze = null;
            return;
        }

        MazeResults results = statsRecorder.saveRecordings(gaveUp);
        this.results.put(currentMaze, results);

        if (!mazes.isEmpty()) {
            Maze nextMaze = this.mazes.remove();
            StatsRecorder newRecorder = this.startMazeAndRecordings(nextMaze);
            this.recorders.put(nextMaze, newRecorder);

            return;
        }

        finishStudy();
    }

    public void playPracticeMaze(Maze practiceMaze) {
        this.currentMaze = practiceMaze;
        StatsRecorder statsRecorder = new StatsRecorder();
        this.recorders.put(practiceMaze, statsRecorder);
        GameScene gameScene = createGameScene(practiceMaze, statsRecorder, true);
        app.activateGameScene(gameScene, gameScene.getGameWindow(), practiceMaze, statsRecorder, true);
        statsRecorder.startRecordings();
    }

    private StatsRecorder startMazeAndRecordings(Maze maze) {
        StatsRecorder statsRecorder = new StatsRecorder();
        this.currentMaze = maze;

        GameScene newGameScene = createGameScene(maze, statsRecorder, false);
        PlayerController playerController = app.activateGameScene(newGameScene, newGameScene.getGameWindow(), maze, statsRecorder, false);
        this.playerControllers.put(maze, playerController);

        statsRecorder.startRecordings();
        return statsRecorder;
    }

    public GameScene createGameScene(Maze maze, StatsRecorder statsRecorder, boolean practiceMaze) {
        GameWindow gameWindow = new GameWindow(maze);
        return new GameScene( gameWindow, new GameHudPane(statsRecorder, this, practiceMaze));
    }

    public String getCurrentMazeName() {
        if (this.currentMaze == null) return "None";
        return this.currentMaze.toString();
    }

    @Override
    public void tick() {
        if (this.currentMaze == null) return;
        StatsRecorder recorder = this.recorders.get(this.currentMaze);
        recorder.updateTime();
    }
}
