package game.labyrinthstudy.game;

import game.labyrinthstudy.MainApplication;
import game.labyrinthstudy.graphics.GameLayerPane;
import game.labyrinthstudy.graphics.GameWindow;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerController {

    private final double PLAYER_SPEED = 3; // Cells per second
    private final GameWindow gameView;
    private final CollisionChecker collisionChecker;

    private double playerX, playerY, velX, velY;

    public PlayerController(GameWindow view, AdjacencyList adjacencyList) {
        this.gameView = view;

        this.collisionChecker = new CollisionChecker(adjacencyList, this);
        this.keysPressed = new HashSet<>();

        this.velX = 0;
        this.velY = 0;

        this.playerX = 0;
        this.playerY = 0;
    }

    public double getPlayerX() {
        return playerX;
    }

    public double getPlayerY() {
        return playerY;
    }

    public void teleport(Location location) {
        this.playerX = location.getX() + 0.5;
        this.playerY = location.getY() + 0.5;
    }

    public boolean atLocation(Location location) {
        return (int) this.playerX == location.getX() && (int) this.playerY == location.getY();
    }

    public double getEffectiveVelX() {
        return velX * speedCorrectionMultiplier();
    }

    public double getEffectiveVelY() {
        return velY * speedCorrectionMultiplier();
    }

    public void tick() {
        updatePositions();
        gameView.updateMazeOffset(playerX, playerY);
    }

    private void updatePositions() {
        Map.Entry<Double, Double> finalDeltas
                = this.collisionChecker.getLegalCoordinates(this.getEffectiveVelX(), this.getEffectiveVelY(),
                                                 GameLayerPane.PL_RADIUS/MainApplication.CELL_SIZE);


        this.playerX += finalDeltas.getKey();
        this.playerY += finalDeltas.getValue();
    }

    private double speedCorrectionMultiplier() {
        double currentVel = Math.sqrt(velX * velX + velY * velY);
        if (currentVel == 0) {
            return 1.0; // Or some other default value when there's no movement
        }
        return (PLAYER_SPEED/GameLoop.tps) / Math.abs(currentVel);
    }

    private final Set<KeyCode> keysPressed;
    private final Map<KeyCode, double[]> directionMap = Map.of(
            KeyCode.A, new double[]{-1, 0},   // Left
            KeyCode.D, new double[]{1, 0},    // Right
            KeyCode.W, new double[]{0, -1},   // Up
            KeyCode.S, new double[]{0, 1}     // Down
    );

    public EventHandler<KeyEvent> keyPressed() {
        return e -> {
            if (directionMap.containsKey(e.getCode())) {
                if (keysPressed.add(e.getCode())) {  // Add key if not already pressed
                    updateVelocities();
                }
            }
        };
    }

    public EventHandler<KeyEvent> keyReleased() {
        return e -> {
            if (directionMap.containsKey(e.getCode())) {
                if (keysPressed.remove(e.getCode())) {  // Remove key if it was pressed
                    updateVelocities();
                }
            }
        };
    }

    private void updateVelocities() {
        double netX = 0;
        double netY = 0;

        // Accumulate velocities based on currently pressed keys
        for (KeyCode key : keysPressed) {
            double[] direction = directionMap.get(key);
            double offsetPerTick = PLAYER_SPEED / (double) GameLoop.tps;

            netX += direction[0] * offsetPerTick;
            netY += direction[1] * offsetPerTick;
        }

        this.velX = netX;
        this.velY = netY;
    }

}
