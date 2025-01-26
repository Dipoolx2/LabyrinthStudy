module game.labyrinthstudy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens game.labyrinthstudy to javafx.fxml;
    exports game.labyrinthstudy;
    exports game.labyrinthstudy.gui;
    opens game.labyrinthstudy.gui to javafx.fxml;
    exports game.labyrinthstudy.study;
    opens game.labyrinthstudy.study to javafx.fxml;
}