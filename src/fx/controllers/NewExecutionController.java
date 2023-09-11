package fx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import javax.swing.text.TableView;
import java.net.URL;
import java.util.ResourceBundle;

public class NewExecutionController implements Initializable {
    @FXML
    private GridPane newExecutionGridPane;

    @FXML
    private TableView populationTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeEntitiesTable();
    }

    private void initializeEntitiesTable() {
    }

    public GridPane getGridPane() {
        return newExecutionGridPane;
    }
}
