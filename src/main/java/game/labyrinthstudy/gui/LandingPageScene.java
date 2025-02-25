package game.labyrinthstudy.gui;

import game.labyrinthstudy.game.Maze;
import game.labyrinthstudy.study.FeedbackType;
import game.labyrinthstudy.study.StudyFlowManager;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Collection;
import java.util.List;

import static game.labyrinthstudy.MainApplication.HEIGHT;
import static game.labyrinthstudy.MainApplication.WIDTH;

public class LandingPageScene extends Scene {

    private final String INSTRUCTIONS_P1 =
            """
            Welcome, and thank you for participating in our study.
                                                                                                
            You will soon be presented with a series of four mazes. Each maze will fall into one of two categories: it will either be
            solvable or unsolvable.
            
            Your task is to determine whether the maze presented to you is solvable. If you believe a maze is solvable, attempt
            to navigate through it to reach the exit. If you believe it is unsolvable, you may indicate this by selecting the
            "Maze Impossible" button. Please note that once you select this option, you will not be able to retry the maze later on.
            
            To navigate through the mazes, you may use the WASD keys or, alternatively, the arrow keys. The maze's start and end
            are marked yellow and green respectively. The "Maze Impossible" button is located near the bottom-left of the interface.
            
            Please note that you need to be eighteen years of age, or older, and you need to have signed the consent form to
            start the study. Lastly, please don't forget to fill in the post-study questionnaire after completing the study.
            
            Before beginning, please complete the practice maze to familiarize yourself with the user interface and controls.
            We wish you the best of luck! Note: The study starts right after you press the "Start Study >" button.
            \s""";

    private boolean practiceMazePlayed = false;
    private final Maze practiceMaze;
    private final StudyFlowManager studyFlowManager;
    private final HBox buttons;
    private final List<Maze> mazes;
    private final List<FeedbackType> feedbackTypes;

    public LandingPageScene(StudyFlowManager studyFlowManager, List<Maze> mazes, List<FeedbackType> feedbackTypes, Maze practiceMaze) {
        super(new StackPane());
        this.feedbackTypes = feedbackTypes;
        this.practiceMaze = practiceMaze;
        this.studyFlowManager = studyFlowManager;
        this.mazes = mazes;

        StackPane root = (StackPane) getRoot();

        Rectangle rectangle = new Rectangle(WIDTH, HEIGHT);
        rectangle.setFill(Color.DARKGRAY.darker().darker());

        Rectangle textContainerArea = new Rectangle(WIDTH / 1.4, HEIGHT / 1.3);
        textContainerArea.setFill(Color.DARKGRAY.darker().darker().darker().darker());
        textContainerArea.setStroke(Color.LIGHTYELLOW.darker());
        textContainerArea.setStrokeWidth(3);
        textContainerArea.setArcHeight(20);
        textContainerArea.setArcWidth(20);

        VBox contents = new VBox(30);
        Label title = new Label("Instructions and important information");
        title.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
        title.setTextFill(Color.ANTIQUEWHITE);

        Text textArea = new Text(INSTRUCTIONS_P1);
        textArea.setFont(Font.font("Helvetica", 20));
        textArea.setFill(Color.ANTIQUEWHITE);

        StackPane playPracticeButton = playPracticeButton();

        this.buttons = new HBox(playPracticeButton);
        buttons.setTranslateY(-35);
        buttons.setSpacing(15);
        buttons.setAlignment(Pos.CENTER_LEFT);

        contents.getChildren().addAll(title, textArea, buttons);
        contents.setTranslateX(50);
        contents.setTranslateY(50);

        StackPane container = new StackPane(textContainerArea, contents);
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(WIDTH / 1.4);
        container.setMaxHeight(HEIGHT / 1.35);

        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(rectangle, container);
    }

    private StackPane playStudyButton() {
        int buttonWidth = 200, buttonHeight = 50;
        Color defaultBackgroundColor = Color.GREEN;
        Color selectedBackgroundColor = defaultBackgroundColor.brighter();

        StackPane stackPane = new StackPane();
        stackPane.setMaxHeight(buttonHeight);
        stackPane.setMaxWidth(buttonWidth);

        Rectangle background = new Rectangle(buttonWidth,buttonHeight);
        background.setFill(defaultBackgroundColor);
        background.setStroke(Color.BLACK.brighter().brighter());
        background.setStrokeWidth(2);
        background.setArcWidth(20);
        background.setArcHeight(20);

        Label buttonLabel = new Label("Start Study >");
        buttonLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
        buttonLabel.setTextFill(Color.ANTIQUEWHITE);

        stackPane.getChildren().addAll(background, buttonLabel);

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
            studyFlowManager.startStudy(mazes, feedbackTypes);
        });

        return stackPane;
    }

    private StackPane playPracticeButton() {
        int buttonWidth = 200, buttonHeight = 50;
        Color defaultBackgroundColor = Color.ROYALBLUE;
        Color selectedBackgroundColor = defaultBackgroundColor.brighter();

        StackPane stackPane = new StackPane();
        stackPane.setMaxHeight(buttonHeight);
        stackPane.setMaxWidth(buttonWidth);

        Rectangle background = new Rectangle(buttonWidth,buttonHeight);
        background.setFill(defaultBackgroundColor);
        background.setStroke(Color.BLACK.brighter().brighter());
        background.setStrokeWidth(2);
        background.setArcWidth(20);
        background.setArcHeight(20);

        Label buttonLabel = new Label("Practice Maze >");
        buttonLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
        buttonLabel.setTextFill(Color.ANTIQUEWHITE);

        stackPane.getChildren().addAll(background, buttonLabel);

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
            studyFlowManager.playPracticeMaze();
            if (!practiceMazePlayed) {
                this.buttons.getChildren().add(playStudyButton());
                practiceMazePlayed = true;
            }
        });

        return stackPane;
    }



}
