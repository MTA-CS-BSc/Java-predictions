package fx.xml.valid.worlds.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import other.WorldDTO;

import java.net.URL;
import java.util.ResourceBundle;

public class XmlValidWorldController implements Initializable {
    @FXML private TableView<WorldDTO> validWorldsTableView;
    @FXML private TableColumn<WorldDTO, String> nameColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
