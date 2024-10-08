package game.labyrinthstudy.game;

import game.labyrinthstudy.graphics.ContainerPane;
import game.labyrinthstudy.graphics.GameWindow;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

public class PlayerController {

    private final double PLAYER_SPEED = 3;
    private final ContainerPane view;

    private double playerX;
    private double playerY;

    private double velX = 0;
    private double velY = 0;

    public PlayerController(ContainerPane view) {
        this.view = view;
    }

    public double getEffectiveVelX() {
        return velX * speedCorrectionMultiplier();
    }

    public double getEffectiveVelY() {
        return velY * speedCorrectionMultiplier();
    }

    private double speedCorrectionMultiplier() {
        double currentVel = Math.sqrt(velX*velX+velY+velY);
        return PLAYER_SPEED/Math.abs(currentVel);
    }

    public EventHandler<KeyEvent> keyPressed() {
        return (k) -> {
            switch (k.getCode()) {
                case A -> velX -= PLAYER_SPEED;
                case D -> velX += PLAYER_SPEED;
                case W -> velY += PLAYER_SPEED;
                case S -> velY -= PLAYER_SPEED;
            }
        };
    }

    public EventHandler<KeyEvent> keyReleased() {
        return (k) -> {
            switch (k.getCode()) {
                case A -> velX += PLAYER_SPEED;
                case D -> velX -= PLAYER_SPEED;
                case W -> velY -= PLAYER_SPEED;
                case S -> velY += PLAYER_SPEED;
            }
        };
    }

}
