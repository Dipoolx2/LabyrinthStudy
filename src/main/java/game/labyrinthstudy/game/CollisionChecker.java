package game.labyrinthstudy.game;

import game.labyrinthstudy.MainApplication;
import game.labyrinthstudy.graphics.GameWindow;

import java.util.List;
import java.util.Map;

public class CollisionChecker {

    private AdjacencyList adjacencyList;
    private PlayerController playerController;

    public CollisionChecker(AdjacencyList adjacencyList, PlayerController playerController) {
        this.adjacencyList = adjacencyList;
        this.playerController = playerController;
    }

    public Map.Entry<Double, Double> getLegalCoordinates(double desiredMovementX, double desiredMovementY, double playerRadius) {

        double currentX = this.playerController.getPlayerX();
        double currentY = this.playerController.getPlayerY();

        double wallWidth = GameWindow.WALL_WIDTH/MainApplication.CELL_SIZE;

        double correctedDeltaX = desiredMovementX;
        double correctedDeltaY = desiredMovementY;

        for (Map.Entry<Location, List<Location>> entry : this.adjacencyList.getMap().entrySet()) {
            Location node = entry.getKey();
            List<Location> neighbors = entry.getValue();

            for (Location neighbor : neighbors) {
                // Compute the wall bounds

                double wallMinX, wallMaxX, wallMinY, wallMaxY;
                if (node.getX() == neighbor.getX()) {
                    wallMinX = node.getX() - wallWidth;
                    wallMaxX = node.getX() + 1 + wallWidth;
                    wallMinY = Math.max(node.getY(), neighbor.getY()) - wallWidth;
                    wallMaxY = Math.max(node.getY(), neighbor.getY()) + wallWidth;
                } else {
                    wallMinY = node.getY() - wallWidth;
                    wallMaxY = node.getY() + 1 + wallWidth;
                    wallMinX = Math.max(node.getX(), neighbor.getX()) - wallWidth;
                    wallMaxX = Math.max(node.getX(), neighbor.getX()) + wallWidth;
                }

                // Predict the player's new position
                double predictedX = currentX + correctedDeltaX;
                double predictedY = currentY + correctedDeltaY;

                // Check if the player's predicted position would overlap with the wall
                if (predictedX + playerRadius > wallMinX && predictedX - playerRadius < wallMaxX
                        && predictedY + playerRadius > wallMinY && predictedY - playerRadius < wallMaxY) {

                    // Adjust the deltas to prevent overlap
                    if (predictedX + playerRadius > wallMinX && predictedX - playerRadius < wallMaxX) {
                        correctedDeltaX = 0; // Stop horizontal movement
                    }
                    if (predictedY + playerRadius > wallMinY && predictedY - playerRadius < wallMaxY) {
                        correctedDeltaY = 0; // Stop vertical movement
                    }
                }
            }
        }

        return Map.entry(correctedDeltaX, correctedDeltaY);
    }

}
