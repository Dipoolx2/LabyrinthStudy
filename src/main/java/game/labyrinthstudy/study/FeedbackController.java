package game.labyrinthstudy.study;

import game.labyrinthstudy.gui.FeedbackHudPane;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.*;

public class FeedbackController {

    private boolean started = false;
    private final FeedbackHudPane hudPane;
    private final Queue<String> feedbackQueue;
    private final List<String> totalFeedbackList;

    public FeedbackController(FeedbackHudPane hudPane, Collection<String> feedbackCollection) {
        this.hudPane = hudPane;

        this.totalFeedbackList = new ArrayList<>(feedbackCollection);
        Collections.shuffle(this.totalFeedbackList);

        this.feedbackQueue = new LinkedList<>(feedbackCollection);
    }

    private Timeline currentTimeline;

    public void start() {
        this.started = true;

        Runnable giveFeedbackRunnable = () -> {
            if (this.feedbackQueue.isEmpty()) {
                this.refillFeedbackQueue();
            }
            this.hudPane.pulse(this.feedbackQueue.poll());
        };

        // Set up the Timeline
        this.currentTimeline = new Timeline(
            new KeyFrame(Duration.seconds(10), e -> {
                if (!this.started) {
                    this.stop();
                    return;
                }
                giveFeedbackRunnable.run();
            }) // Call the function every 2 seconds
        );
        currentTimeline.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
        currentTimeline.play(); // Start the timeline
    }

    private void refillFeedbackQueue() {
        this.feedbackQueue.clear();

        Collections.shuffle(this.totalFeedbackList);
        this.feedbackQueue.addAll(this.totalFeedbackList);
    }

    public void stop() {
        this.started = false;
        if (this.currentTimeline == null)
            return;

        this.currentTimeline.stop();
    }

}
