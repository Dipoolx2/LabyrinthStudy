package game.labyrinthstudy.graphics;

import javafx.scene.layout.StackPane;

public class ContainerPane extends StackPane {

    private GameWindow gameWindow;

    public ContainerPane(GameWindow gameWindow) {
        this.gameWindow = gameWindow;

        this.getChildren().add(gameWindow);
    }

}
