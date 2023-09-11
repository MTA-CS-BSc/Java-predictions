package fx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class NewExecutionController implements Initializable {
    @FXML
    private GridPane newExecutionGridPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    public GridPane getGridPane() {
        return newExecutionGridPane;
    }
}
