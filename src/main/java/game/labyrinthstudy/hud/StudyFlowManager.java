package game.labyrinthstudy.hud;

import game.labyrinthstudy.game.Maze;
import game.labyrinthstudy.graphics.GameLayerPane;
import game.labyrinthstudy.graphics.GameWindow;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import java.util.ArrayDeque;
import java.util.Queue;

import static game.labyrinthstudy.MainApplication.HEIGHT;
import static game.labyrinthstudy.MainApplication.WIDTH;

public class StudyFlowManager {

    private Queue<Maze> mazes;

    public StudyFlowManager() {
        this.mazes = new ArrayDeque<>();
    }


    public void finished() {
        this.stopRecordings();
        this.saveRecordings();

        if (!mazes.isEmpty()) {
            Maze maze = mazes.remove();

        }
    }

    private void stopRecordings() {

    }

    private void saveRecordings() {

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
