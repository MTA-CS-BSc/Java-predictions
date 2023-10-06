package fx.components.details;

import fx.components.details.valid.worlds.XmlValidWorldsController;
import fx.components.details.world.WorldDetailsController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DetailsController implements Initializable {
    @FXML private VBox container;
    @FXML private WorldDetailsController worldDetailsController;
    @FXML private XmlValidWorldsController xmlValidWorldsController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        worldDetailsController.setSelectedWorldListener(xmlValidWorldsController.selectedWorldProperty());
        xmlValidWorldsController.setIsParentVisibleProperty(container.visibleProperty());
    }

    public VBox getContainer() {
        return container;
    }

    public void clearWorldSelection() {
        xmlValidWorldsController.setSelectedWorld(null);
    }
}
