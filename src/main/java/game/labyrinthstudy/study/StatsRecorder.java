package game.labyrinthstudy.study;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;

public class StatsRecorder {

    private long lastTick;
    private final SimpleLongProperty timePassedMs;
    private final SimpleDoubleProperty distanceWalked;
    private final SimpleIntegerProperty keystrokesCount;

    private boolean active;

    public StatsRecorder() {
        this.active = false;
        this.lastTick = 0;

        this.timePassedMs = new SimpleLongProperty(0);
        this.distanceWalked = new SimpleDoubleProperty(0);
        this.keystrokesCount = new SimpleIntegerProperty(0);
    }

    public void updateTime() {
        if (!active) return;
        this.timePassedMs.set(this.timePassedMs.get() + (System.currentTimeMillis() - lastTick));
        this.lastTick = System.currentTimeMillis();
    }

    public void incrementDistanceWalked(double value) {
        if (!active) return;
        this.distanceWalked.set(this.distanceWalked.get() + value);
    }

    public void incrementKeystrokes() {
        if (!active) return;
        this.keystrokesCount.set(this.keystrokesCount.get() + 1);
    }

    public void startRecordings() {
        this.active = true;
        this.lastTick = System.currentTimeMillis();
    }

    public void stopRecordings() {
        this.active = false;
    }

    public void saveRecordings() {

    }

    public double getDistanceWalked() {
        return distanceWalked.get();
    }

    public int getKeystrokesCount() {
        return keystrokesCount.get();
    }

    public long getTimePassedMs() {
        return timePassedMs.get();
    }

    public SimpleIntegerProperty getKeystrokesCountProperty() {
        return this.keystrokesCount;
    }

    public SimpleLongProperty getTimePassedProperty() {
        return this.timePassedMs;
    }

    public SimpleDoubleProperty getDistanceWalkedProperty() {
        return this.distanceWalked;
    }
}

