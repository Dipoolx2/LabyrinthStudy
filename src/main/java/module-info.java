module game.labyrinthstudy {
    requires javafx.controls;
    requires javafx.fxml;


    opens game.labyrinthstudy to javafx.fxml;
    exports game.labyrinthstudy;
    exports game.labyrinthstudy.gui;
    opens game.labyrinthstudy.gui to javafx.fxml;
}