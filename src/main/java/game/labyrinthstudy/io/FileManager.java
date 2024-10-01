package game.labyrinthstudy.io;

import game.labyrinthstudy.game.AdjacencyList;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

public class FileManager {

    public AdjacencyList readAdjacencyListFromFile(String fileName) throws FileNotFoundException {
        URL url = this.getClass().getClassLoader().getResource(fileName);
        File file = null;
        try {
            assert url != null;
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
        }
        Scanner sc = new Scanner(file);

        StringBuilder stringBuilder = new StringBuilder();
        while (sc.hasNext()) {
            stringBuilder.append(sc.nextLine()).append("\n");
        }

        return AdjacencyList.fromString(stringBuilder.toString());
    }


}
