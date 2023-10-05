package fx.mgmt.controllers;

import consts.Animations;
import fx.mgmt.threadpool.controllers.ThreadsAmountSetterController;
import fx.mgmt.valid.worlds.controllers.XmlValidWorldsController;
import fx.mgmt.world.details.controllers.WorldDetailsController;
import fx.xml.loader.controllers.XmlLoaderController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MgmtController implements Initializable {
    @FXML private XmlLoaderController xmlLoaderController;
    @FXML private ThreadsAmountSetterController threadsAmountSetterController;
    @FXML private WorldDetailsController worldDetailsController;
    @FXML private XmlValidWorldsController xmlValidWorldsController;
    @FXML private VBox container;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Animations.IS_ANIMATIONS_ON.setValue(true);
        worldDetailsController.setSelectedWorldListener(xmlValidWorldsController.selectedWorldProperty());
    }

    public VBox getContainer() {
        return container;
    }
}
