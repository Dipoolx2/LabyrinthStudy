package game.labyrinthstudy.gui;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static game.labyrinthstudy.MainApplication.HEIGHT;
import static game.labyrinthstudy.MainApplication.WIDTH;

public class TransitionScene extends Scene {
    public TransitionScene() {
        super(getTransitionLayout(), WIDTH, HEIGHT);
    }

    private static StackPane getTransitionLayout() {
        Label titleLabel = new Label("Maze finished");
        Font titleFont = Font.font("Helvetica", FontWeight.BOLD, 50);
        titleLabel.setFont(titleFont);
        titleLabel.setTextFill(Color.ANTIQUEWHITE);

        Label subscript = new Label("You will commence to the next maze in 3 seconds...");
        Font subscriptFont = Font.font("Helvetica", FontWeight.SEMI_BOLD, 30);
        subscript.setFont(subscriptFont);
        subscript.setTextFill(Color.ANTIQUEWHITE);

        VBox text = new VBox(titleLabel, subscript);
        text.setAlignment(Pos.CENTER);

        StackPane stackPane = new StackPane(text);
        stackPane.setBackground(Background.fill(Color.BLACK.brighter().brighter()));

        return new StackPane(stackPane);
    }
}
