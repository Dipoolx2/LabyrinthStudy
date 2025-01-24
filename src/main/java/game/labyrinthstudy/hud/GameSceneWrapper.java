package game.labyrinthstudy.hud;

import game.labyrinthstudy.graphics.GameWindow;
import javafx.scene.Scene;

public class GameSceneWrapper {

    private Scene gameScene;
    private GameWindow gameWindow;

    public GameSceneWrapper(Scene gameScene, GameWindow gameWindow) {
        this.gameScene = gameScene;
        this.gameWindow = gameWindow;
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }

    public Scene getGameScene() {
        return gameScene;
    }
}
