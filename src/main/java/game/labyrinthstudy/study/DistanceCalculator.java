package game.labyrinthstudy.study;

import game.labyrinthstudy.game.Location;
import game.labyrinthstudy.game.Maze;

import java.util.*;

public class DistanceCalculator {

    public static int calculateDistance(Maze maze, Location start, Location end) {
        Map<Location, List<Location>> adjacencyList = maze.getAdjacencyList().getMap();

        Map<Location, Integer> distances = new HashMap<>();
        Queue<Location> q = new LinkedList<>();

        q.add(start);
        distances.put(start, 0);

        while (!q.isEmpty()) {
            Location current = q.poll();
            int currentDistance = distances.get(current);

            if (current.equals(end)) {
                return currentDistance;
            }

            if (!adjacencyList.containsKey(current)) continue;

            for (Location neighbour : adjacencyList.get(current)) {
                if (!distances.containsKey(neighbour)) {
                    distances.put(neighbour, currentDistance + 1);
                    q.add(neighbour);
                }
            }
        }

        return -1; // Not reachable
    }

}
