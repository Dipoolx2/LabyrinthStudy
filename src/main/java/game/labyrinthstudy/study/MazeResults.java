package game.labyrinthstudy.study;

import game.labyrinthstudy.game.Location;
import game.labyrinthstudy.game.Maze;
import game.labyrinthstudy.game.PlayerController;

public class MazeResults {

    private final double distanceWalked;
    private final int keystrokesPressed;
    private final long timePassed;
    private final boolean gaveUp;

    private boolean finalResultsAvailable = false;
    private int distanceToEnd;
    private int distanceFromStart;

    public MazeResults(StatsRecorder statsRecorder, boolean gaveUp) {
        this.distanceWalked = statsRecorder.getDistanceWalked();
        this.keystrokesPressed = statsRecorder.getKeystrokesCount();
        this.timePassed = statsRecorder.getTimePassedMs();
        this.gaveUp = gaveUp;
    }

    public void computeAllResults(Maze maze, PlayerController playerController) {
        // Compute distance from end, distance from start
        Location playerLocation = playerController.getLocation();
        this.distanceFromStart = DistanceCalculator.calculateDistance(maze, playerLocation, maze.getStartLocation());
        this.distanceToEnd = DistanceCalculator.calculateDistance(maze, playerLocation, maze.getEndLocation());
        this.finalResultsAvailable = true;
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
}
