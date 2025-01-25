package game.labyrinthstudy.study;

import game.labyrinthstudy.game.Maze;
import game.labyrinthstudy.game.PlayerController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;

public class StatsRecorder {

    private SimpleLongProperty timePassedMs;
    private SimpleIntegerProperty unitsWalked;
    private SimpleIntegerProperty keystrokesCount;

    public StatsRecorder(PlayerController playerController) {
        this.timePassedMs = new SimpleLongProperty(0);
        this.unitsWalked = new SimpleIntegerProperty(0);
        this.keystrokesCount = new SimpleIntegerProperty(0);
    }

    public void startRecordings() {

    }

    public void stopRecordings() {

    }

    public void saveRecordings() {

    }


}

