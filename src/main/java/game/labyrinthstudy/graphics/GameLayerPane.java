package game.labyrinthstudy.graphics;

import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameLayerPane extends StackPane {

    private final Circle PL_VIEW;
    private final GameWindow GAME_VIEW;
    private final Color PL_COLOR = Color.RED;
    public static final double PL_RADIUS = 13;



    public GameLayerPane(GameWindow gameWindow) {
        this.PL_VIEW = generatePlayerView();
        this.GAME_VIEW = gameWindow;

        this.getChildren().add(GAME_VIEW);
        this.getChildren().add(PL_VIEW);

        this.setBackground(Background.fill(Color.DARKGRAY));
    }

    private Circle generatePlayerView() {
        Circle circle = new Circle(PL_RADIUS, PL_COLOR);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);
        return circle;
    }

}
