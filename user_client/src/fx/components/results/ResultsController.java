package fx.components.results;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ResultsController implements Initializable {
    @FXML private VBox container;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public VBox getContainer() {
        return container;
    }
}
