package game.labyrinthstudy.game;

public class Maze {
    private AdjacencyList adjacencyList;
    private Location startLocation, endLocation;

    public Maze(AdjacencyList adjacencyList, Location startLocation, Location endLocation) {
        this.adjacencyList = adjacencyList;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
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
}
