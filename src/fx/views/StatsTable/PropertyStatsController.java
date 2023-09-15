package fx.views.StatsTable;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class PropertyStatsController implements Initializable {
    @FXML
    private StackPane container;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        container.setVisible(false);
    }

    public void toggleVisibility() {
        container.setVisible(!container.isVisible());
    }
}
