module main.snakegameproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens main.snakegame to javafx.fxml;
    exports main.snakegame;
}