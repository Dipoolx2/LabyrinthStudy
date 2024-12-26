package game.labyrinthstudy.game;

import java.util.*;

public class AdjacencyList {

    private final Map<Location, List<Location>> adjacencyList;

    private AdjacencyList(Map<Location, List<Location>> map) {
        this.adjacencyList = map;
    }

    public Map<Location, List<Location>> getMap() {
        return adjacencyList;
    }

    public AdjacencyList getWallAdjacencyList() {
        Map<Location, List<Location>> wallAdjacencyList = new HashMap<>();

        for (Map.Entry<Location, List<Location>> entry : this.getMap().entrySet()) {
            Location current = entry.getKey();
            Set<Location> accessibleNeighbors = new HashSet<>(entry.getValue());

            for (Direction direction : Direction.values()) {
                Location neighbor = current.move(direction);

                // If the neighbor is not accessible, it's a wall.
                if (!accessibleNeighbors.contains(neighbor)) {
                    wallAdjacencyList.computeIfAbsent(current, k -> new ArrayList<>()).add(neighbor);
                }
            }
        }

        return new AdjacencyList(wallAdjacencyList);
    }

    public enum Direction {
        UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

        public int dx;
        public int dy;
        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    public static AdjacencyList fromString(String input) {
        Map<Location, List<Location>> map = new HashMap<>();

        // Split the input string into lines
        String[] lines = input.split("\n");

        for (String line : lines) {
            // Split each line into the Location and the list of neighbors
            String[] parts = line.split(" -> ");
            if (parts.length < 2) continue;

            // Parse the current Location (node)
            Location node = parseLocation(parts[0]);

            // Parse the list of neighboring Locations
            String neighborsStr = parts[1].replace("[", "").replace("]", "").trim();
            String[] neighborTokens = neighborsStr.split("\\), ");

            List<Location> neighbors = new LinkedList<>();
            for (String token : neighborTokens) {
                if (!token.isEmpty()) {
                    neighbors.add(parseLocation(token));
                }
            }

            // Add this node and its neighbors to the adjacency list
            map.put(node, neighbors);
        }

        return new AdjacencyList(map);
    }

    public static Location parseLocation(String locationStr) {
        locationStr = locationStr.replace("(", "").replace(")", "");
        String[] coordinates = locationStr.split(", ");
        int col = Integer.parseInt(coordinates[0]);
        int row = Integer.parseInt(coordinates[1]);
        return new Location(row, col);
    }
}