package game.labyrinthstudy.io;

import game.labyrinthstudy.game.AdjacencyList;
import game.labyrinthstudy.game.Location;
import game.labyrinthstudy.game.Maze;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

public class FileManager {

    public Maze readMazeFromFile(String fileName) throws FileNotFoundException {
        URL url = this.getClass().getClassLoader().getResource(fileName);
        File file = null;
        try {
            assert url != null;
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
        }
        Scanner sc = new Scanner(file);

        String seLocations = sc.nextLine();

        String start = seLocations.split(":")[0];
        String end = seLocations.split(":")[1];

        Location startLocation = AdjacencyList.parseLocation(start);
        Location endLocation = AdjacencyList.parseLocation(end);

        StringBuilder stringBuilder = new StringBuilder();
        while (sc.hasNext()) {
            stringBuilder.append(sc.nextLine()).append("\n");
        }

        AdjacencyList adjacencyList = AdjacencyList.fromString(stringBuilder.toString());

        return new Maze(adjacencyList, startLocation, endLocation);
    }


}
