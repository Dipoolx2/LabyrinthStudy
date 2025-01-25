package game.labyrinthstudy.gui;

import game.labyrinthstudy.study.StatsRecorder;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import static game.labyrinthstudy.MainApplication.HEIGHT;
import static game.labyrinthstudy.MainApplication.WIDTH;

public class GameHudPane extends VBox {

    public GameHudPane(StatsRecorder statsRecorder) {
        setMaxHeight((double) HEIGHT / 1.2);
        setMaxWidth((double) WIDTH / 4);
        setBackground(Background.fill(Color.BLACK.brighter().brighter().brighter()));
        setBorder(Border.stroke(Color.DARKGRAY));

        setAlignment(Pos.CENTER_LEFT);

//        Rectangle rect = new Rectangle(500, 500, Color.BLUE);
//        getChildren().add(rect);

        Label label1 = new Label();
        statsRecorder.getTimePassedProperty().addListener((obs, ov, nv) -> {
            label1.setText("Time passed: " + (double) nv / 1000d);
        });

        getChildren().add(label1);
    }
}
