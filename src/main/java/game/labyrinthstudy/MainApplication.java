package game.labyrinthstudy;

import game.labyrinthstudy.game.*;
import game.labyrinthstudy.graphics.GameWindow;
import game.labyrinthstudy.gui.TransitionScene;
import game.labyrinthstudy.study.FeedbackType;
import game.labyrinthstudy.study.StatsRecorder;
import game.labyrinthstudy.study.StudyFlowManager;
import game.labyrinthstudy.io.FileManager;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainApplication extends Application {

    public FileManager fileManager;
    private PlayerController playerController;
    private AnimationTimer gameLoop;
    private LocationListenerManager locationListenerManager;
    private StudyFlowManager studyFlowManager;

    private Set<TickListener> tickListeners;

    public static final int CELL_SIZE = 100;
    public static final int MAZE_SIZE = 30;
    public static final int WIDTH = 1600, HEIGHT = 900;

    private Stage stage;

    @Override
    public void start(Stage stage) {
        Random random = new Random();
        boolean positive = random.nextBoolean(); // Randomize feedback type

        this.stage = stage;
        this.tickListeners = new HashSet<>();

        this.fileManager = new FileManager();
        this.studyFlowManager = new StudyFlowManager(this, positive);
        this.registerTickListener(studyFlowManager);

        String[] solvableMazes = {"maze1_solvable", "maze2_solvable", "maze3_solvable", "maze4_solvable", "maze5_solvable"};
        String[] unsolvableMazes = {"maze1_nosolve", "maze2_nosolve", "maze3_nosolve", "maze4_nosolve", "maze5_nosolve"};
//        String[] solvableMazes = {"practice", "practice", "practice", "practice", "practice"};
//        String[] unsolvableMazes = {"practice", "practice", "practice", "practice", "practice"};

        int[] mazeIndices = getRandomIndices();
        String[] mazeNames = getEqualMazesOfEach(mazeIndices, solvableMazes, unsolvableMazes);
        System.out.println(Arrays.toString(mazeNames));

        List<Maze> mazes = new ArrayList<>();
        for (String mazeName : mazeNames) {
            mazes.add(getMaze(mazeName + ".txt"));
        }


        Maze practiceMaze = getMaze("practice.txt");

        List<FeedbackType> feedbackTypes = FeedbackType.filterFeedbackTypes(positive, !positive);
        studyFlowManager.start(mazes, feedbackTypes, practiceMaze);

        // Set window properties
        stage.setTitle("Maze study");
        stage.setResizable(false);

        startMainLoop();
        stage.show();
    }

    private String[] getEqualMazesOfEach(int[] indices, String[] solvableMazes, String[] unsolvableMazes) {
        List<String> mazes = new ArrayList<>();

        int index = 0;
        while (index < indices.length / 2) {
            mazes.add(solvableMazes[indices[index]]);
            index++;
        }

        while (index < indices.length) {
            mazes.add(unsolvableMazes[indices[index]]);
            index++;
        }

        Collections.shuffle(mazes);
        return mazes.toArray(new String[4]);
    }

    private int[] getRandomIndices() {
        List<Integer> digits = IntStream.range(0,5).boxed().collect(Collectors.toList());
        Collections.shuffle(digits);
        int[] numbers = new int[4];
        for(int i=0;i<4;i++){
            numbers[i] = digits.get(i);
        }
        return numbers;
    }

    public void restartProgram() {
        this.stage.close();
        this.clearTickListeners();
        this.locationListenerManager = null;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new MainApplication().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public PlayerController activateGameScene(Scene scene, GameWindow gameWindow, Maze maze, StatsRecorder statsRecorder, boolean practiceMaze) {
        this.clearGameKeys(scene, this.playerController);
        this.deregisterTickListener(this.playerController);
        this.playerController = new PlayerController(gameWindow, maze.getAdjacencyList().getWallAdjacencyList(), statsRecorder);
        this.playerController.teleport(maze.getStartLocation());
        this.registerGameKeys(scene, this.playerController);

        this.locationListenerManager = new LocationListenerManager(this.playerController);
        this.locationListenerManager.addListener(maze.getEndLocation(), loc -> studyFlowManager.finishMaze(false, practiceMaze));

        this.registerTickListener(this.playerController);

        this.stage.setScene(scene);

        return this.playerController;
    }

    private void clearTickListeners() {
        this.tickListeners.clear();
    }

    private void deregisterTickListener(TickListener tickListener) {
        if (tickListener != null) {
            this.tickListeners.remove(tickListener);
        }
    }

    public void clearLocationListeners() {
        this.locationListenerManager.clearListeners();
    }

    public boolean handleNextMaze(Map<Maze, StatsRecorder> recorders, Queue<Maze> mazes) {
        if (!mazes.isEmpty()) {
            // Create transition scene
            Scene transitionScene = new TransitionScene();

            // Show the transition scene
            stage.setScene(transitionScene);
            stage.show();

            // Delay for 3 seconds
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> {
                Maze nextMaze = mazes.remove();
                StatsRecorder newRecorder = this.studyFlowManager.startMazeAndRecordings(nextMaze);
                recorders.put(nextMaze, newRecorder);
            });

            pause.play();
            return true;
        }
        return false;
    }

    public void triviallySetScene(Scene scene) {
        this.stage.setScene(scene);
    }

    private void startMainLoop() {
        // start game
        gameLoop = new GameLoop(this);
        gameLoop.start();
    }

    private void stopGameLoop() {
        gameLoop.stop();
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