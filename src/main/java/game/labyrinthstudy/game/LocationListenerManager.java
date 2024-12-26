package game.labyrinthstudy.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationListenerManager {

    private final PlayerController playerController;
    private final Map<Location, List<ListenerAction>> listeners;

    public LocationListenerManager(PlayerController playerController) {
        this.playerController = playerController;

        this.listeners = new HashMap<>();
    }

    public void addListener(Location location, ListenerAction action) {
        this.listeners.putIfAbsent(location, new ArrayList<>());
        this.listeners.get(location).add(action);
    }

    public void tick() {
        for (Location loc : this.listeners.keySet()) {
            if (this.playerController.atLocation(loc)) {
                this.listeners.get(loc).forEach(listenerAction -> listenerAction.action(loc));
            }
        }
    }

    public interface ListenerAction {
        void action(Location location);
    }
}
