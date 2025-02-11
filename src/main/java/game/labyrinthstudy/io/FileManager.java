package game.labyrinthstudy.io;

import game.labyrinthstudy.game.AdjacencyList;
import game.labyrinthstudy.game.Location;
import game.labyrinthstudy.game.Maze;
import game.labyrinthstudy.study.FeedbackType;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileManager {

    public List<String> readFeedbackSentences(FeedbackType feedbackType) {
        String fileName = feedbackType.fileName();

        Scanner fileScanner = null;
        try {
            fileScanner = getFileScanner(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: file " + fileName + " not found!");
            return null;
        }

        List<String> sentences = new ArrayList<>();
        while (fileScanner.hasNextLine()) {
            sentences.add(fileScanner.nextLine());
        }

        return sentences;
    }

    private Scanner getFileScanner(String fileName) throws FileNotFoundException {
        URL url = this.getClass().getClassLoader().getResource(fileName);
        System.out.println("Attempting to get file scanner for " + fileName);
        File file = null;
        try {
            assert url != null;
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
        }
        return new Scanner(file);
    }

    public Maze readMazeFromFile(String fileName) throws FileNotFoundException {
        Scanner sc = getFileScanner(fileName);

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

        return new Maze(fileName.substring(0, fileName.lastIndexOf('.')), adjacencyList, startLocation, endLocation);
    }


}
