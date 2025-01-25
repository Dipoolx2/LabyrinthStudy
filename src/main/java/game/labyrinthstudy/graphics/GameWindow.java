package game.labyrinthstudy.graphics;

import game.labyrinthstudy.game.AdjacencyList;
import game.labyrinthstudy.game.Location;
import game.labyrinthstudy.game.Maze;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Map;

import static game.labyrinthstudy.MainApplication.CELL_SIZE;
import static game.labyrinthstudy.MainApplication.MAZE_SIZE;

public class GameWindow extends StackPane { // Singleton stack pane

    private final double gameOffsetX = (double) (MAZE_SIZE * CELL_SIZE) /2;
    private final double gameOffsetY = (double) (MAZE_SIZE * CELL_SIZE) /2;

    public static final double WALL_WIDTH = 2;

    private final AdjacencyList adjacencyList;
    private final Location startLocation, endLocation;

    public GameWindow(Maze maze) {
        this.adjacencyList = maze.getAdjacencyList();
        this.startLocation = maze.getStartLocation();
        this.endLocation = maze.getEndLocation();

        Canvas mazeWindow = new Canvas(MAZE_SIZE*CELL_SIZE, MAZE_SIZE*CELL_SIZE);

        GraphicsContext mazeGc = mazeWindow.getGraphicsContext2D();
        drawMaze(mazeGc);
        drawMapMark(mazeGc, this.startLocation.getX(), this.startLocation.getY(), Color.GOLD);
        drawMapMark(mazeGc, this.endLocation.getX(), this.endLocation.getY(), Color.GREEN);

        this.getChildren().add(mazeWindow);
    }

    private void drawMapMark(GraphicsContext gc, int coordinateX, int coordinateY, Color centerColor) {
        int x = coordinateX * CELL_SIZE+CELL_SIZE/2;
        int y = coordinateY * CELL_SIZE+CELL_SIZE/2;

        double maxRadius = 60;

        // Exponential diffusion gradient
        for (int r = (int)maxRadius; r > 0; r--) {
            // Exponential alpha falloff for more dramatic diffusion
            double alpha = Math.pow(1 - (double)r / maxRadius, 3);
            gc.setFill(centerColor.deriveColor(1, 1, 1, alpha * 0.8));
            gc.fillOval(x - r, y - r, 2 * r, 2 * r);
        }
    }

    public void updateMazeOffset(double playerX, double playerY) {
        this.setTranslateX(this.gameOffsetX-playerX*CELL_SIZE);
        this.setTranslateY(this.gameOffsetY-playerY*CELL_SIZE);
    }

    private void drawMaze(GraphicsContext gc) {
        gc.setLineWidth(WALL_WIDTH);
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
