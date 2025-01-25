package game.labyrinthstudy.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import static game.labyrinthstudy.MainApplication.HEIGHT;
import static game.labyrinthstudy.MainApplication.WIDTH;

public class GameLayerPane extends StackPane {

    private final Pane CIRCLE_OVERLAY;
    private final Circle PL_VIEW;
    private final GameWindow GAME_VIEW;
    private final Color PL_COLOR = Color.RED;

    public static final double PL_RADIUS = 13;
    private final double CIRCLE_OVERLAY_RADIUS = 360;

    private final Color BG_COLOR = Color.DARKGRAY;

    public GameLayerPane(GameWindow gameWindow) {
        this.PL_VIEW = generatePlayerView();
        this.CIRCLE_OVERLAY = generateCircleOverlay();
        this.GAME_VIEW = gameWindow;

        this.getChildren().add(GAME_VIEW);
        this.getChildren().add(PL_VIEW);
        this.getChildren().add(CIRCLE_OVERLAY);

        this.setBackground(Background.fill(BG_COLOR));
    }

    private Circle generatePlayerView() {
        Circle circle = new Circle(PL_RADIUS, PL_COLOR);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);
        return circle;
    }

    private Pane generateCircleOverlay() {
        StackPane overlayPane = new StackPane();

        Color foregroundColor = BG_COLOR.darker().darker();

        // Background rectangle covering entire screen
        Rectangle background = new Rectangle(WIDTH, HEIGHT);
        Rectangle backgroundsBackground = new Rectangle(WIDTH, HEIGHT); // To sharpen edges on the screen borders

        // Circular "hole" in the center
        Circle hole = new Circle((double) WIDTH/2, (double) HEIGHT/2, CIRCLE_OVERLAY_RADIUS);
        Circle sharpHole = new Circle((double) WIDTH/2, (double) HEIGHT/2, CIRCLE_OVERLAY_RADIUS*1.2);

        // Create a radial gradient with more gradual transition
        RadialGradient gradient = new RadialGradient(
                0, 0,  // focal angle and focal distance
                (double) WIDTH/2, (double) HEIGHT/2,  // center x, y
                CIRCLE_OVERLAY_RADIUS,  // larger radius for softer transition
                false,  // proportional
                CycleMethod.NO_CYCLE,
                new Stop(0.0, BG_COLOR.darker().deriveColor(0, 1, 1, 0.0)),
                new Stop(0.8, BG_COLOR.darker().deriveColor(0, 1, 1, 0.5)),
                new Stop(1.0, foregroundColor)
        );

        // Subtract the hole from the background
        Shape combinedShape = Shape.subtract(background, hole);
        combinedShape.setFill(gradient);

        Shape combinedShapeSharp = Shape.subtract(background, sharpHole);
        combinedShapeSharp.setFill(foregroundColor);

        // Add gaussian blur for extra soft edge
        GaussianBlur blur = new GaussianBlur(20);
        combinedShape.setEffect(blur);

        overlayPane.getChildren().addAll(combinedShape, combinedShapeSharp);

        return overlayPane;
    }


}
