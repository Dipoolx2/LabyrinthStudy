package game.labyrinthstudy.graphics;

import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class ContainerPane extends StackPane {

    public ContainerPane(GameWindow gameWindow) {
        this.setBackground(Background.fill(Color.DARKGRAY));
        gameWindow.setAlignment(Pos.TOP_LEFT);

        this.getChildren().add(gameWindow);
    }

    public void updateMazeOffset(double playerX, double playerY) {
        this.getChildren().get(0).setTranslateX(-playerX);
        this.getChildren().get(0).setTranslateY(-playerY);
    }

}
