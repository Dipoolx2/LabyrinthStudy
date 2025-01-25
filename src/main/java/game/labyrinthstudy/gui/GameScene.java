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

    public GameScene(GameWindow gameWindow, GameHudPane gameHudPane) {
        super(new StackPane(), WIDTH, HEIGHT);
        this.gameWindow = gameWindow;
        StackPane root = (StackPane) getRoot();

        GameLayerPane gameLayerPane = new GameLayerPane(gameWindow);
        root.getChildren().add(gameLayerPane);
        gameLayerPane.setTranslateX(250);

        root.getChildren().add(gameHudPane);
        StackPane.setAlignment(gameHudPane, Pos.CENTER_LEFT);
        gameHudPane.setTranslateX(150);
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }
}
