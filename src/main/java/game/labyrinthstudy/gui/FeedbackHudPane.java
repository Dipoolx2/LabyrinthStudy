package game.labyrinthstudy.gui;

import javafx.animation.FadeTransition;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.Stack;

public class FeedbackHudPane extends StackPane {

    private final Label messageLabel;

    public FeedbackHudPane(String message) {
        Font fontToUse = Font.font("Helvetica", 20);
        Color backgroundColor = Color.DARKGRAY.deriveColor(0.3, 0.3, 0.3, 0.8);
        Color strokeColor = Color.YELLOW.brighter();

        messageLabel = new Label(message);
        messageLabel.setFont(fontToUse);
        messageLabel.setTextFill(Color.ANTIQUEWHITE);

        StackPane background = getBackground(messageLabel, backgroundColor, strokeColor);

        getChildren().addAll(background, messageLabel);
        setAlignment(Pos.CENTER);

        setOpacity(0);
    }

    private static StackPane getBackground(Label messageLabel, Color backgroundColor, Color strokeColor) {
        final int arc = 20, baseSize = 20, extraWidth = 40, extraHeight = 20;
        final ReadOnlyObjectProperty<Bounds> textBounds = messageLabel.layoutBoundsProperty();

        Rectangle background = new Rectangle(baseSize, baseSize);
        background.setFill(backgroundColor);
        background.setArcHeight(arc);
        background.setArcWidth(arc);

        BoxBlur blur = new BoxBlur(10, 10, 3);
        background.setEffect(blur); // Add blur effect to the background

        Rectangle stroke = new Rectangle(baseSize, baseSize);
        stroke.setFill(Color.TRANSPARENT);
        stroke.setArcWidth(arc);
        stroke.setArcHeight(arc);
        stroke.setStrokeWidth(2);
        stroke.setStroke(strokeColor);

        StackPane stackPane = new StackPane(background, stroke);
        stackPane.setMaxWidth(baseSize);
        stackPane.setMaxHeight(baseSize);

        textBounds.addListener((obs, ov, nv) -> {
            double newWidth = nv.getWidth() + extraWidth;
            double newHeight = nv.getHeight() + extraHeight;

            background.setWidth(newWidth);
            background.setHeight(newHeight);
            stroke.setWidth(newWidth);
            stroke.setHeight(newHeight);
            stackPane.setMaxWidth(newWidth);
            stackPane.setMaxHeight(newHeight);
        });

        return stackPane;
    }

    public void pulse(String message) {
        final double MSG_DELAY = 1.5 + message.split(" ").length * 0.20;

        this.messageLabel.setText(message);
        this.setOpacity(1.0);

        // Set up the FadeTransition
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), this);
        fadeTransition.setDelay(Duration.seconds(MSG_DELAY)); // Add a 3-second delay
        fadeTransition.setFromValue(1.0); // Fully visible
        fadeTransition.setToValue(0.0);   // Fully transparent
        fadeTransition.setOnFinished(e -> System.out.println("Fade-out complete!"));

        // Start the transition
        fadeTransition.play();
    }

}
