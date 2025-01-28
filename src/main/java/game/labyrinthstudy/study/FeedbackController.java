package game.labyrinthstudy.study;

import game.labyrinthstudy.gui.FeedbackHudPane;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class FeedbackController {

    private boolean started = false;
    private final FeedbackHudPane hudPane;

    public FeedbackController(FeedbackHudPane hudPane) {
        this.hudPane = hudPane;
    }

    public void start() {
        this.started = true;

        Runnable giveFeedbackRunnable = () -> {

        };

        // Set up the Timeline
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(2), e -> giveFeedbackRunnable.run()) // Call the function every 2 seconds
        );
        timeline.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
        timeline.play(); // Start the timeline
    }

    public void stop() {

    }

}
