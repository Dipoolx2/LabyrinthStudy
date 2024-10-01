package game.labyrinthstudy.game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AdjacencyList {

    private final Map<Location, List<Location>> adjacencyList;

    private AdjacencyList(Map<Location, List<Location>> map) {
        this.adjacencyList = map;
    }

    public Map<Location, List<Location>> getMap() {
        return adjacencyList;
    }

    private AdjacencyList fromString(String input) {
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

    private Location parseLocation(String locationStr) {
        locationStr = locationStr.replace("(", "").replace(")", "");
        String[] coordinates = locationStr.split(", ");
        int col = Integer.parseInt(coordinates[0]);
        int row = Integer.parseInt(coordinates[1]);
        return new Location(row, col);
    }
}