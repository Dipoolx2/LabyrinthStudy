package game.labyrinthstudy.gui;

import game.labyrinthstudy.study.StatsRecorder;
import game.labyrinthstudy.study.StudyFlowManager;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static game.labyrinthstudy.MainApplication.HEIGHT;
import static game.labyrinthstudy.MainApplication.WIDTH;

public class GameHudPane extends VBox {

    private final StatsRecorder statsRecorder;
    private final StudyFlowManager studyFlowManager;
    private final boolean inPractice;

    public GameHudPane(StatsRecorder statsRecorder, StudyFlowManager studyFlowManager, boolean inPractice) {
        super(70);
        this.inPractice = inPractice;

        this.statsRecorder = statsRecorder;
        this.studyFlowManager = studyFlowManager;

        StackPane infoPane = getInfoPane();
        StackPane buttonPane = getButtonPane();

        setAlignment(Pos.CENTER_LEFT);
        getChildren().addAll(infoPane, buttonPane);
    }

    private StackPane getButtonPane() {
        StackPane buttonPane = new StackPane();

        buttonPane.setMaxHeight((double) HEIGHT / 4);
        buttonPane.setMaxWidth((double) WIDTH / 4);

        Rectangle bg = new Rectangle((double) WIDTH / 4, (double) HEIGHT / 4);
        bg.setFill(Color.web("#2c3138"));
        bg.setStroke(Color.LIGHTYELLOW.darker().darker());
        bg.setStrokeWidth(3);
        bg.setArcHeight(20);
        bg.setArcWidth(20);
        buttonPane.getChildren().add(bg);

        VBox content = new VBox(20);
        content.setTranslateY(0);

        Label titleLabel = new Label("Action");
        titleLabel.setTextFill(Color.ANTIQUEWHITE);
        titleLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 40));
        titleLabel.setMaxHeight(40);

        StackPane impossibleButton = impossibleButton();

        content.getChildren().add(titleLabel);
        content.getChildren().add(impossibleButton);

        buttonPane.getChildren().add(content);
        content.setAlignment(Pos.CENTER);

        return buttonPane;
    }

    private StackPane impossibleButton() {
        int buttonWidth = 250, buttonHeight = 75;
        Color defaultBackgroundColor = Color.INDIANRED.brighter();
        Color selectedBackgroundColor = defaultBackgroundColor.darker();

        StackPane stackPane = new StackPane();
        stackPane.setMaxHeight(buttonHeight);
        stackPane.setMaxWidth(buttonWidth);

        Rectangle background = new Rectangle(buttonWidth,buttonHeight);
        background.setFill(defaultBackgroundColor);
        background.setStroke(Color.ANTIQUEWHITE);
        background.setStrokeWidth(2);
        background.setArcWidth(20);
        background.setArcHeight(20);

        Label buttonLabel = new Label("Maze Impossible");
        buttonLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
        buttonLabel.setTextFill(Color.ANTIQUEWHITE);

        Label subscriptLabel = new Label("(give up)");
        subscriptLabel.setFont(Font.font("Helvetica", 18));
        subscriptLabel.setTextFill(Color.ANTIQUEWHITE);

        VBox buttonLabelStack = new VBox();
        buttonLabelStack.getChildren().addAll(buttonLabel, subscriptLabel);

        buttonLabelStack.setAlignment(Pos.CENTER);
        stackPane.getChildren().addAll(background, buttonLabelStack);

        // Set the cursor to HAND when hovering over the StackPane
        stackPane.setOnMouseEntered(event -> {
            stackPane.setCursor(Cursor.HAND);
            background.setFill(selectedBackgroundColor);
        });
        stackPane.setOnMouseExited(event -> {
            stackPane.setCursor(Cursor.DEFAULT);
            background.setFill(defaultBackgroundColor);
        });

        stackPane.setOnMouseClicked(event -> {
            studyFlowManager.finishMaze(true, this.inPractice);
        });

        return stackPane;
    }

    private StackPane getInfoPane() {
        StackPane infoPane = new StackPane();

        infoPane.setMaxHeight((double) HEIGHT / 3.2);
        infoPane.setMaxWidth((double) WIDTH / 4);

        Rectangle bg = new Rectangle((double) WIDTH / 4, (double) HEIGHT / 3.2);
        bg.setFill(Color.web("#2c3138"));
        bg.setStroke(Color.LIGHTYELLOW.darker().darker());
        bg.setStrokeWidth(3);
        bg.setArcHeight(20);
        bg.setArcWidth(20);
        infoPane.getChildren().add(bg);

        VBox stats = new VBox(20);
        stats.setTranslateY(0);

        Label titleLabel = new Label("Info");

        titleLabel.setTextFill(Color.ANTIQUEWHITE);
        titleLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 40));
        titleLabel.setMaxHeight(40);

        stats.setAlignment(Pos.CENTER);
        stats.getChildren().add(titleLabel);

        VBox actualList = new VBox(20);
        actualList.setTranslateX(50);

        Font listItemFont = Font.font("Courier New", 25);
        Label currentMazeLabel = new Label("Current maze: " + studyFlowManager.getCurrentMazeName());
        currentMazeLabel.setFont(listItemFont);
        currentMazeLabel.setTextFill(Color.ANTIQUEWHITE);

        Label timeTakenLabel = new Label("Time taken: 3:20.532");
        timeTakenLabel.setFont(listItemFont);
        timeTakenLabel.setTextFill(Color.ANTIQUEWHITE);

        actualList.getChildren().add(currentMazeLabel);
        actualList.getChildren().add(timeTakenLabel);


        statsRecorder.getTimePassedProperty().addListener((obs, ov, nv) -> {
            long minutes = (nv.longValue() / 1000) / 60;
            long seconds = (nv.longValue() / 1000) % 60;
            long millis = nv.longValue() % 1000;

            String timePassed = String.format("%02d:%02d.%03d", minutes, seconds, millis);
            timeTakenLabel.setText("Time taken: " + timePassed);
        });

        infoPane.getChildren().add(stats);
        stats.getChildren().add(actualList);

        return infoPane;
    }
}
