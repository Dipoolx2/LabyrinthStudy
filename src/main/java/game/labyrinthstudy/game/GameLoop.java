package game.labyrinthstudy.game;

import game.labyrinthstudy.MainApplication;
import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {

    private final MainApplication mainApp;
    public static final long tps = 60;

    private long lastTick = 0;
    private long lastSecond = 0;
    private int effectiveTPS = 0;

    public GameLoop(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void handle(long now) {
        if (lastTick == 0) {
            lastTick = now;
            lastSecond = now;
        }

        // Check if enough time has passed for the next tick
        // Nanoseconds per tick
        final long nsPerTick = 1000000000 / tps;
        if (now - lastTick >= nsPerTick) {
            this.mainApp.tick();
            lastTick += nsPerTick;
            effectiveTPS++;
        }

        // Check if a second has passed to log the effective TPS
        if (now - lastSecond >= 1000000000) {
            System.out.println("Effective TPS: " + effectiveTPS);
            lastSecond = now;
            effectiveTPS = 0;
        }
    }
}
