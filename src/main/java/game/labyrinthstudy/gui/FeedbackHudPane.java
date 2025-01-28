package game.labyrinthstudy.gui;

import javafx.animation.FadeTransition;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class FeedbackHudPane extends StackPane {

    private final Label messageLabel;

    public FeedbackHudPane(String message) {
        Font fontToUse = Font.font("Helvetica", 20);
        Color backgroundColor = Color.DARKGRAY.deriveColor(0.3, 0.3, 0.3, 0.8);
        Color strokeColor = Color.YELLOW.brighter();

        messageLabel = new Label(message);
        messageLabel.setFont(fontToUse);
        messageLabel.setTextFill(Color.ANTIQUEWHITE);

        Rectangle background = getBackground(messageLabel, backgroundColor, strokeColor);

        getChildren().addAll(background, messageLabel);
        setAlignment(Pos.CENTER);
    }

    private static Rectangle getBackground(Label messageLabel, Color backgroundColor, Color strokeColor) {
        final ReadOnlyObjectProperty<Bounds> textBounds = messageLabel.layoutBoundsProperty();

        Rectangle background = new Rectangle(20, 20);
        background.setFill(backgroundColor);
        background.setArcHeight(20);
        background.setArcWidth(20);
        background.setStroke(strokeColor);
        background.setStrokeWidth(2);

        textBounds.addListener((obs, ov, nv) -> {
            background.setWidth(nv.getWidth() + 40);
            background.setHeight(nv.getHeight() + 20);
        });

        return background;
    }

    public void pulse(String message) {
        this.messageLabel.setText(message);
        this.setOpacity(1.0);

        // Set up the FadeTransition
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), this);
        fadeTransition.setDelay(Duration.seconds(3)); // Add a 3-second delay
        fadeTransition.setFromValue(1.0); // Fully visible
        fadeTransition.setToValue(0.0);   // Fully transparent
        fadeTransition.setOnFinished(e -> System.out.println("Fade-out complete!"));

        // Start the transition
        fadeTransition.play();
    }

}
