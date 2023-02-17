module main.snakegameproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens main.snakegameproject to javafx.fxml;
    exports main.snakegameproject;
}