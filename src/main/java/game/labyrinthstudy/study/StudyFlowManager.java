package game.labyrinthstudy.study;

import game.labyrinthstudy.MainApplication;
import game.labyrinthstudy.TickListener;
import game.labyrinthstudy.game.Maze;
import game.labyrinthstudy.game.PlayerController;
import game.labyrinthstudy.graphics.GameWindow;
import game.labyrinthstudy.gui.EndPageScene;
import game.labyrinthstudy.gui.GameHudPane;
import game.labyrinthstudy.gui.GameScene;
import game.labyrinthstudy.gui.LandingPageScene;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class StudyFlowManager implements TickListener {

    private final MainApplication app;

    private final boolean positive;

    private final List<Maze> allMazes;

    private final Queue<Maze> mazes;
    private final Queue<FeedbackType> feedbackTypes;
    private final List<FeedbackType> allFeedbackTypes;

    private final Map<Maze, StatsRecorder> recorders;
    private final Map<Maze, MazeResults> results;
    private final Map<Maze, PlayerController> playerControllers;
    private final Map<Maze, FeedbackController> feedbackControllers;

    private Maze practiceMaze;

    private LandingPageScene landingPageScene;
    private Maze currentMaze;

    public StudyFlowManager(MainApplication app, boolean positive) {
        this.app = app;
        this.positive = positive;

        this.allMazes = new ArrayList<>();
        this.allFeedbackTypes = new ArrayList<>();
        this.feedbackTypes = new LinkedList<>();
        this.mazes = new LinkedList<>();
        this.recorders = new HashMap<>();
        this.results = new HashMap<>();
        this.playerControllers = new HashMap<>();
        this.feedbackControllers = new HashMap<>();

        this.currentMaze = null;
    }

    public void restartStudy() {
        this.app.restartProgram();
    }

    public void start(List<Maze> mazes, List<FeedbackType> feedbackTypes, Maze practiceMaze) {
        this.landingPageScene = new LandingPageScene(this, mazes, feedbackTypes, practiceMaze);
        this.app.triviallySetScene(landingPageScene);
        this.practiceMaze = practiceMaze;
    }

    public void startStudy(List<Maze> mazes, List<FeedbackType> feedbackTypes) {
        this.allMazes.addAll(mazes);
        this.mazes.addAll(this.getShuffledMazes(mazes));

        this.feedbackTypes.addAll(feedbackTypes);
        this.allFeedbackTypes.addAll(feedbackTypes);

        if (this.mazes.isEmpty()) {
            this.finishStudy();
        }

        Maze firstMaze = this.mazes.remove();
        StatsRecorder firstRecorder = this.startMazeAndRecordings(firstMaze);
        this.recorders.put(firstMaze, firstRecorder);
    }

    private void finishStudy() {
        for (Maze maze : this.allMazes) {
            MazeResults result = this.results.get(maze);
            result.computeAllResults(maze, playerControllers.get(maze));
        }

        String textualResults = getTextualStudyResults();

        Scene endScene = generateEndScene(textualResults);
        this.app.triviallySetScene(endScene);
    }

    private String getTextualStudyResults() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("-------- RESULTS START --------").append("\n");

        stringBuilder.append("Group: ").append(this.positive ? 1 : 2).append("\n\n");
        for (Maze maze : this.allMazes) {
            MazeResults result = results.get(maze);
            stringBuilder.append(result.toString()).append("\n");
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1); // Get rid of last newline
        stringBuilder.append("-------- RESULTS END --------");
        return stringBuilder.toString();
    }

    private Scene generateEndScene(String textualResults) {
        return new EndPageScene(this.allMazes, this.results, textualResults, this);
    }

    public void finishMaze(boolean gaveUp, boolean practiceMaze) {
        if (practiceMaze) {
            this.recorders.remove(this.practiceMaze);
            this.app.triviallySetScene(this.landingPageScene);
            return;
        }

        app.clearLocationListeners();

        StatsRecorder statsRecorder = this.recorders.get(this.currentMaze);
        statsRecorder.stopRecordings();

        FeedbackController feedbackController = this.feedbackControllers.get(this.currentMaze);
        feedbackController.stop();

        MazeResults results = statsRecorder.saveRecordings(gaveUp);
        this.results.put(currentMaze, results);

        if (app.handleNextMaze(this.recorders, this.mazes)) return;

        finishStudy();
    }

    private List<Maze> getShuffledMazes(List<Maze> allMazes) {
        List<Maze> shuffledMazes = new ArrayList<>(allMazes);
        Collections.shuffle(shuffledMazes);
        return shuffledMazes;
    }

    public void playPracticeMaze() {
        StatsRecorder statsRecorder = new StatsRecorder(FeedbackType.COMPARATIVE_POSITIVE);
        this.recorders.put(this.practiceMaze, statsRecorder);

        GameScene gameScene = createGameScene(practiceMaze, statsRecorder, true);
        app.activateGameScene(gameScene, gameScene.getGameWindow(), practiceMaze, statsRecorder, true);

        statsRecorder.startRecordings();
    }

    public StatsRecorder startMazeAndRecordings(Maze maze) {
        if (this.feedbackTypes.isEmpty()) refillFeedbackTypes();

        FeedbackType feedbackType = this.feedbackTypes.poll();
        StatsRecorder statsRecorder = new StatsRecorder(feedbackType);
        assert feedbackType != null;

        this.currentMaze = maze;

        GameScene newGameScene = createGameScene(maze, statsRecorder, false);
        PlayerController playerController = app.activateGameScene(newGameScene, newGameScene.getGameWindow(), maze, statsRecorder, false);

        Collection<String> feedbacks = this.app.fileManager.readFeedbackSentences(feedbackType);
        FeedbackController feedbackController = new FeedbackController(newGameScene.getFeedbackHudPane(), feedbacks);

        this.feedbackControllers.put(maze, feedbackController);
        this.playerControllers.put(maze, playerController);

        statsRecorder.startRecordings();
        feedbackController.start();
        return statsRecorder;
    }

    public GameScene createGameScene(Maze maze, StatsRecorder statsRecorder, boolean practiceMaze) {
        GameWindow gameWindow = new GameWindow(maze);
        return new GameScene(gameWindow, new GameHudPane(statsRecorder, this, practiceMaze), practiceMaze);
    }

    public String getCurrentMazeName() {
        if (this.currentMaze == null) return "None";
        return this.currentMaze.toString();
    }

    private void refillFeedbackTypes() {
        this.feedbackTypes.clear();

        List<FeedbackType> shuffledFeedbackTypes = new ArrayList<>(this.allFeedbackTypes);
        Collections.shuffle(shuffledFeedbackTypes);

        this.feedbackTypes.addAll(shuffledFeedbackTypes);
    }

    @Override
    public void tick() {
        if (this.currentMaze == null) return;
        if (!this.recorders.containsKey(this.currentMaze)) return;
        StatsRecorder recorder = this.recorders.get(this.currentMaze);
        recorder.updateTime();
    }
}
