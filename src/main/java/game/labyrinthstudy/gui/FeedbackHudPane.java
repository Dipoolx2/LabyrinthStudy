package game.labyrinthstudy.gui;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class FeedbackHudPane extends StackPane {

    public FeedbackHudPane(String message) {
        Font fontToUse = Font.font("Helvetica", 20);
        Color backgroundColor = Color.DARKGRAY.deriveColor(0.3, 0.3, 0.3, 0.8);
        Color strokeColor = Color.YELLOW.brighter();

        Label messageLabel = new Label(message);
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

}
