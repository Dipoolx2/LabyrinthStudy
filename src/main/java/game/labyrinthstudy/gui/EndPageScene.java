package game.labyrinthstudy.gui;

import game.labyrinthstudy.UninstallerService;
import game.labyrinthstudy.game.Maze;
import game.labyrinthstudy.study.MazeResults;
import game.labyrinthstudy.study.StudyFlowManager;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static game.labyrinthstudy.MainApplication.HEIGHT;
import static game.labyrinthstudy.MainApplication.WIDTH;

public class EndPageScene extends Scene {

    private final StudyFlowManager studyFlowManager;

    public EndPageScene(List<Maze> mazeOrdering, Map<Maze, MazeResults> resultSet, String textualResults, StudyFlowManager studyFlowManager) {
        super(new StackPane());
        StackPane root = (StackPane) getRoot();
        this.studyFlowManager = studyFlowManager;

        Rectangle rectangle = new Rectangle(WIDTH, HEIGHT);
        rectangle.setFill(Color.DARKGRAY.darker().darker());

        Rectangle resultsContainerArea = new Rectangle(WIDTH / 1.4, HEIGHT / 1.30);
        resultsContainerArea.setFill(Color.DARKGRAY.darker().darker().darker().darker());
        resultsContainerArea.setStroke(Color.LIGHTYELLOW.darker());
        resultsContainerArea.setStrokeWidth(3);
        resultsContainerArea.setArcHeight(20);
        resultsContainerArea.setArcWidth(20);

        Label titleLabel = getTitleLabel();

        List<StackPane> resultsStackPanes = new ArrayList<>();
        for (Maze maze : mazeOrdering) {
            MazeResults mazeResult = resultSet.get(maze);
            resultsStackPanes.add(generateResultStackPane(maze, mazeResult));
        }

        HBox resultsContainers = new HBox(20);
        resultsContainers.getChildren().addAll(resultsStackPanes);

        StackPane copyButton = getCopyButton(textualResults);

        Label infoLabel = getInfoLabel();
        Hyperlink surveyLink = getSurveyLink();

        VBox infoPanel = new VBox(infoLabel, surveyLink);
        infoPanel.setAlignment(Pos.CENTER);

        HBox buttons = getExitButtons();

        VBox pageContent = new VBox(titleLabel, resultsContainers, copyButton, infoPanel, buttons);
        pageContent.setAlignment(Pos.TOP_CENTER);
        pageContent.setTranslateY(30);
        pageContent.setSpacing(30);

        StackPane resultGroupContainer = new StackPane(resultsContainerArea, pageContent);
        resultGroupContainer.setMaxWidth(WIDTH / 1.4);
        resultGroupContainer.setMaxHeight(HEIGHT / 1.5);

        resultGroupContainer.setAlignment(Pos.CENTER);
        resultsContainers.setAlignment(Pos.CENTER);

        root.getChildren().addAll(rectangle, resultGroupContainer);
    }

    private StackPane getCopyButton(String textualResults) {
        int width = 120, height = 30;
        Rectangle buttonBackground = new Rectangle(width, height);
        buttonBackground.setFill(Color.DARKGOLDENROD);
        buttonBackground.setArcWidth(20);
        buttonBackground.setArcHeight(20);
        buttonBackground.setStrokeWidth(2);
        buttonBackground.setStroke(Color.ANTIQUEWHITE);

        Label buttonLabel = new Label("Copy to clipboard");
        buttonLabel.setFont(Font.font("Helvetica", 12));
        buttonLabel.setTextFill(Color.ANTIQUEWHITE);

        StackPane copyButton = new StackPane(buttonBackground, buttonLabel);
        copyButton.setMaxWidth(width);
        copyButton.setMaxHeight(height);
        copyButton.setAlignment(Pos.CENTER);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sent to clipboard!");
        alert.setHeaderText(null);
        alert.setContentText("Copied results to clipboard!");

        copyButton.setCursor(Cursor.HAND);
        copyButton.setOnMouseClicked(e -> {
            StringSelection stringSelection = new StringSelection(textualResults);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);

            alert.showAndWait();
        });

        return copyButton;
    }

    private Label getInfoLabel() {
        final String INFO_TEXT = "You have reached the end of maze trials. Please kindly fill out the questionnaire with the above results:";
        Label label = new Label(INFO_TEXT);
        label.setFont(Font.font("Helvetica", 20));
        label.setTextFill(Color.ANTIQUEWHITE);
        return label;
    }

    private HBox getExitButtons() {
        StackPane restartButton = getRestartButton();
        StackPane exitButton = getExitButton();
        StackPane exitUninstallButton = getExitAndUninstallButton();

        HBox exitButtons = new HBox(restartButton, exitButton, exitUninstallButton);
        exitButtons.setAlignment(Pos.CENTER);
        exitButtons.setSpacing(20);

        return exitButtons;
    }

    private StackPane getExitAndUninstallButton() {
        Runnable action = UninstallerService::startUninstaller;
        return getButton(Color.INDIANRED, 200, "Exit and Uninstall", action);
    }

    private StackPane getExitButton() {
        Runnable action = () -> System.exit(0);
        return getButton(Color.INDIANRED, 80, "Exit", action);
    }

    private StackPane getRestartButton() {
        Runnable action = studyFlowManager::restartStudy;
        return getButton(Color.GREEN, 180, "< Restart Study", action);
    }

    private StackPane getButton(Color fillColor, int buttonWidth, String text, Runnable onClick) {
        final int buttonHeight = 50;
        final Color backgroundColor = fillColor;

        Rectangle background = new Rectangle(buttonWidth, buttonHeight);
        background.setFill(backgroundColor);
        background.setArcHeight(20);
        background.setArcWidth(20);

        Label buttonLabel = new Label(text);
        buttonLabel.setTextFill(Color.ANTIQUEWHITE);
        buttonLabel.setFont(Font.font("Helvetica", 20));

        StackPane stackPane = new StackPane(background, buttonLabel);
        stackPane.setMaxHeight(buttonHeight);
        stackPane.setMaxWidth(buttonWidth);
        stackPane.setAlignment(Pos.CENTER);

        stackPane.setOnMouseEntered(e -> {
            background.setFill(backgroundColor.brighter());
            stackPane.setCursor(Cursor.HAND);
        });

        stackPane.setOnMouseExited(e -> {
            background.setFill(backgroundColor);
            stackPane.setCursor(Cursor.DEFAULT);
        });

        stackPane.setOnMouseClicked(e -> onClick.run());
        return stackPane;
    }

    private Hyperlink getSurveyLink() {
        String linkText = "https://psychru.qualtrics.com/jfe/form/SV_7Nx8yH5bNytfbPo";
        Hyperlink link = new Hyperlink(linkText);
        link.setScaleX(1.4);
        link.setScaleY(1.4);

        link.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI(linkText));
            } catch (Exception e) {
                link.setDisable(true);
            }
        });

        return link;
    }

    private Label getTitleLabel() {
        Label label = new Label("Results");
        label.setTextFill(Color.ANTIQUEWHITE);
        label.setFont(Font.font("Helvetica", FontWeight.BOLD, 50));
        return label;
    }

    private StackPane generateResultStackPane(Maze maze, MazeResults results) {
        final int cardWidth = 250, cardHeight = 285;
        Rectangle background = new Rectangle(cardWidth, cardHeight);
        background.setFill(Color.ROYALBLUE);
        background.setArcWidth(20);
        background.setArcHeight(20);

        Label mazeName = new Label(maze.toString());
        mazeName.setTextFill(Color.ANTIQUEWHITE);
        mazeName.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
        mazeName.setTranslateY(20);

        Label timeTakenLabel = getTimeTakenLabel(results.getTimePassed());
        Label keystorkesPressedLabel = getKeystrokesPressedLabel(results.getKeystrokesPressed());
        Label distanceWalkedLabel = getDistanceWalkedLabel(results.getDistanceWalked());
        Label distanceFromEndLabel = getDistanceFromEndLabel(results.getDistanceToEnd());
        Label distanceFromStartLabel = getDistanceFromStartLabel(results.getDistanceFromStart());
        Label gaveUpLabel = getGaveUpLabel(results.isGaveUp());

        VBox stats =  new VBox(timeTakenLabel, gaveUpLabel, keystorkesPressedLabel, distanceWalkedLabel, distanceFromEndLabel, distanceFromStartLabel);
        stats.setAlignment(Pos.CENTER_LEFT);
        stats.setTranslateX(25);

        VBox content = new VBox(mazeName, stats);
        content.setAlignment(Pos.TOP_CENTER);
        content.setSpacing(30);

        StackPane card = new StackPane(background, content);
        card.setMaxWidth(cardWidth);
        card.setMaxHeight(cardHeight);

        return card;
    }

    private Label getGaveUpLabel(boolean gaveUp) {
        return generateLabel("Gave up", Boolean.toString(gaveUp));
    }

    private Label getDistanceFromStartLabel(int distanceFromStart) {
        return generateLabel("Distance from start", Integer.toString(distanceFromStart));

    }

    private Label getDistanceFromEndLabel(int distanceToEnd) {
        return generateLabel("Distance from end", Integer.toString(distanceToEnd));

    }

    private Label getDistanceWalkedLabel(double distanceWalked) {
        return generateLabel("Distance walked", String.format("%.1f", distanceWalked));

    }

    private Label getTimeTakenLabel(long timePassed) {
        long minutes = (timePassed / 1000) / 60;
        long seconds = (timePassed / 1000) % 60;
        long millis = timePassed % 1000;

        String timePassedString = String.format("%02d:%02d.%03d", minutes, seconds, millis);
        return generateLabel("Time taken", timePassedString);
    }

    private Label getKeystrokesPressedLabel(int keystrokesPressed) {
        return generateLabel("Keystrokes pressed", Integer.toString(keystrokesPressed));
    }

    private Label generateLabel(String key, String value) {
        Label label = new Label(key+": "+value);
        label.setFont(Font.font("Helvetica", 20));
        label.setTextFill(Color.ANTIQUEWHITE);
        return label;
    }
}
