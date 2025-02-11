package game.labyrinthstudy.game;

import java.util.Objects;
import java.util.UUID;

public class Maze {
    private final AdjacencyList adjacencyList;
    private final Location startLocation, endLocation;

    private final String name;
    private final UUID uuid;

    public Maze(String name, AdjacencyList adjacencyList, Location startLocation, Location endLocation) {
        this.adjacencyList = adjacencyList;
        this.startLocation = startLocation;
        this.endLocation = endLocation;

        this.name = name.split("_")[0];
        this.uuid = UUID.randomUUID();
    }

    public AdjacencyList getAdjacencyList() {
        return adjacencyList;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Maze maze = (Maze) o;
        return Objects.equals(uuid, maze.uuid);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
