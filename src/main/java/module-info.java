module game.labyrinthstudy {
    requires javafx.controls;
    requires javafx.fxml;


    opens game.labyrinthstudy to javafx.fxml;
    exports game.labyrinthstudy;
    exports game.labyrinthstudy.hud;
    opens game.labyrinthstudy.hud to javafx.fxml;
}