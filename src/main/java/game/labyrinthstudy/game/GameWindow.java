package game.labyrinthstudy.game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Map;

public class GameWindow extends StackPane {

    private final Canvas mazeView;

    private final AdjacencyList adjacencyList;

    private static final int CELL_SIZE = 20;
    private static final int MAZE_SIZE = 30;

    public GameWindow(AdjacencyList maze) {
        this.adjacencyList = maze;

        this.setBackground(Background.fill(Color.web("#1a1a1a")));
        this.mazeView = new Canvas();

        GraphicsContext mazeGc = this.mazeView.getGraphicsContext2D();
        drawMaze(mazeGc);

        this.getChildren().add(this.mazeView);
    }

    private void drawMaze(GraphicsContext gc) {
        gc.setLineWidth(2);
        for (Map.Entry<Location, List<Location>> entry : adjacencyList.getMap().entrySet()) {
            Location current = entry.getKey();
            List<Location> neighbors = entry.getValue();

            int x = current.getX() * CELL_SIZE;
            int y = current.getY() * CELL_SIZE;

            // Draw walls
            gc.strokeRect(x, y, CELL_SIZE, CELL_SIZE);

            // Remove walls for connected cells
            for (Location neighbor : neighbors) {
                int dx = neighbor.getX() - current.getX();
                int dy = neighbor.getY() - current.getY();

                if (dx == 1) {
                    gc.clearRect(x + CELL_SIZE - 1, y + 1, 2, CELL_SIZE - 2);
                } else if (dx == -1) {
                    gc.clearRect(x - 1, y + 1, 2, CELL_SIZE - 2);
                } else if (dy == 1) {
                    gc.clearRect(x + 1, y + CELL_SIZE - 1, CELL_SIZE - 2, 2);
                } else if (dy == -1) {
                    gc.clearRect(x + 1, y - 1, CELL_SIZE - 2, 2);
                }
            }
        }
    }
}
