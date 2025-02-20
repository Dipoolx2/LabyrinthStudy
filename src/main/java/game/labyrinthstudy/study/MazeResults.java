package game.labyrinthstudy.study;

import game.labyrinthstudy.game.Location;
import game.labyrinthstudy.game.Maze;
import game.labyrinthstudy.game.PlayerController;

public class MazeResults {

    private final double distanceWalked;
    private final int keystrokesPressed;
    private final long timePassed;
    private final boolean gaveUp;
    private FeedbackType feedbackType;

    private String mazeName;
    private boolean finalResultsAvailable = false;
    private int distanceToEnd;
    private int distanceFromStart;

    public MazeResults(StatsRecorder statsRecorder, FeedbackType feedbackType, boolean gaveUp) {
        this.distanceWalked = statsRecorder.getDistanceWalked();
        this.keystrokesPressed = statsRecorder.getKeystrokesCount();
        this.timePassed = statsRecorder.getTimePassedMs();
        this.gaveUp = gaveUp;
        this.feedbackType = feedbackType;
    }

    public void computeAllResults(Maze maze, PlayerController playerController) {
        // Compute distance from end, distance from start
        Location playerLocation = playerController.getLocation();
        this.distanceFromStart = DistanceCalculator.calculateDistance(maze, playerLocation, maze.getStartLocation());
        this.distanceToEnd = DistanceCalculator.calculateDistance(maze, playerLocation, maze.getEndLocation());
        this.finalResultsAvailable = true;
        this.mazeName = maze.toString();
    }

    public double getDistanceWalked() {
        return distanceWalked;
    }

    public int getDistanceToEnd() {
        if (!this.finalResultsAvailable) {
            System.out.println("ERROR: Final result not yet available [first call MazeResults::computeAllResults()]");
            return -1;
        }
        return distanceToEnd;
    }

    public int getDistanceFromStart() {
        if (!this.finalResultsAvailable) {
            System.out.println("ERROR: Final result not yet available [first call MazeResults::computeAllResults()]");
            return -1;
        }
        return distanceFromStart;
    }

    public int getKeystrokesPressed() {
        return keystrokesPressed;
    }

    public long getTimePassed() {
        return timePassed;
    }

    public boolean isGaveUp() {
        return gaveUp;
    }

    @Override
    public String toString() {
        long minutes = (this.timePassed / 1000) / 60;
        long seconds = (this.timePassed / 1000) % 60;
        long millis = this.timePassed % 1000;

        String timePassed = String.format("%02d:%02d.%03d", minutes, seconds, millis);

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("Maze: ").append(this.mazeName).append(" (").append(this.feedbackType.descriptiveForm()).append(")\n");
        if (finalResultsAvailable)
            strBuilder.append("     Gave up: ").append(this.gaveUp).append("\n");
        strBuilder.append("     Time passed: ").append(timePassed).append("\n");
        strBuilder.append("     Keystrokes pressed: ").append(keystrokesPressed).append("\n");
        strBuilder.append("     Distance walked: ").append(distanceWalked).append("\n");

        if (finalResultsAvailable) {
            strBuilder.append("     Distance from start: ").append(distanceFromStart).append("\n");
            strBuilder.append("     Distance to end: ").append(distanceToEnd).append("\n");
        }
        // gave up, time passed, keystrokes pressed, distance walked, distance to start, distance to end

        return strBuilder.toString();
    }
}
