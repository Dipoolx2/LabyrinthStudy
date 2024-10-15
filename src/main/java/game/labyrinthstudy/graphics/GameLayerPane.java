package game.labyrinthstudy.graphics;

import game.labyrinthstudy.MainApplication;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameLayerPane extends StackPane {

    private final Circle PL_VIEW;
    private final ContainerPane GAME_VIEW;

    public static final double mazeCenterOffsetX = (double) MainApplication.WIDTH / 2;
    public static final double mazeCenterOffsetY = (double) MainApplication.HEIGHT / 2;

    private final Color PL_COLOR = Color.RED;
    private final double PL_RADIUS = 4;

    public GameLayerPane(ContainerPane containerPane) {
        this.PL_VIEW = generatePlayerView();
        this.GAME_VIEW = containerPane;

        this.GAME_VIEW.setAlignment(Pos.CENTER);

        this.GAME_VIEW.setTranslateX(mazeCenterOffsetX);
        this.GAME_VIEW.setTranslateY(mazeCenterOffsetY);

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
