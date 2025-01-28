package game.labyrinthstudy.gui;

import game.labyrinthstudy.graphics.GameLayerPane;
import game.labyrinthstudy.graphics.GameWindow;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import static game.labyrinthstudy.MainApplication.HEIGHT;
import static game.labyrinthstudy.MainApplication.WIDTH;

public class GameScene extends Scene {

    private final GameWindow gameWindow;
    private final boolean practice;

    private final FeedbackHudPane feedbackHudPane;

    public GameScene(GameWindow gameWindow, GameHudPane gameHudPane, boolean practice) {
        super(new StackPane(), WIDTH, HEIGHT);
        this.practice = practice;
        this.gameWindow = gameWindow;

        StackPane root = (StackPane) getRoot();

        GameLayerPane gameLayerPane = new GameLayerPane(gameWindow);

        this.feedbackHudPane = new FeedbackHudPane("You're making good progress!");
        this.feedbackHudPane.setTranslateY(250);

        StackPane gameWithFeedback = new StackPane(gameLayerPane);
        if (!practice) {
            gameWithFeedback.getChildren().add(this.feedbackHudPane);
        }
        gameWithFeedback.setAlignment(Pos.CENTER);
        gameWithFeedback.setTranslateX(250);

        StackPane.setAlignment(gameHudPane, Pos.CENTER_LEFT);
        gameHudPane.setTranslateX(150);

        root.getChildren().addAll(gameWithFeedback, gameHudPane);
    }

    public FeedbackHudPane getFeedbackHudPane() {
        return this.feedbackHudPane;
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }
}
