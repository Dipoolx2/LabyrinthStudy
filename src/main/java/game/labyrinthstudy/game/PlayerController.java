package game.labyrinthstudy.game;

import game.labyrinthstudy.graphics.ContainerPane;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerController {

    private final double PLAYER_SPEED = 3;
    private final ContainerPane view;

    private double playerX, playerY, velX, velY;

    public PlayerController(ContainerPane view) {
        this.view = view;
        this.keysPressed = new HashSet<>();

        this.velX = 0;
        this.velY = 0;

        this.playerX = 0;
        this.playerY = 0;
    }

    public double getEffectiveVelX() {
        return velX * speedCorrectionMultiplier();
    }

    public double getEffectiveVelY() {
        return velY * speedCorrectionMultiplier();
    }

    public void tick() {
        updatePositions();
        view.updateMazeOffset(playerX, playerY);
    }

    private void updatePositions() {
        this.playerX += this.getEffectiveVelX();
        this.playerY += this.getEffectiveVelY();
        System.out.println(this.playerX + " " + this.playerY);
    }

    private double speedCorrectionMultiplier() {
        double currentVel = Math.sqrt(velX * velX + velY * velY);
        if (currentVel == 0) {
            return 1.0; // Or some other default value when there's no movement
        }
        return PLAYER_SPEED / Math.abs(currentVel);
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
            netX += direction[0] * PLAYER_SPEED;
            netY += direction[1] * PLAYER_SPEED;
        }

        this.velX = netX;
        this.velY = netY;
    }

}
