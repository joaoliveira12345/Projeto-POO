module SeuModulo {
    requires javafx.controls;
    requires javafx.fxml;

    opens seuPacote para javafx.fxml;
    exports seuPacote;
}

