package fx.mgmt;

import consts.Animations;
import fx.mgmt.threadpool.queue.ThreadpoolQueueController;
import fx.mgmt.threadpool.setter.ThreadsAmountSetterController;
import fx.mgmt.valid.worlds.XmlValidWorldsController;
import fx.mgmt.world.details.WorldDetailsController;
import fx.xml.loader.XmlLoaderController;
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
    @FXML private ThreadpoolQueueController threadpoolQueueController;
    @FXML private VBox container;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Animations.IS_ANIMATIONS_ON.setValue(true);
        worldDetailsController.setSelectedWorldListener(xmlValidWorldsController.selectedWorldProperty());
        threadpoolQueueController.setIsParentVisibleProperty(container.visibleProperty());
        xmlValidWorldsController.setIsParentVisibleProperty(container.visibleProperty());
    }

    public VBox getContainer() {
        return container;
    }

    public void clearThreadpoolTableView() {
        threadpoolQueueController.clearTableView();
    }

    public void clearWorldDetails() {
        xmlValidWorldsController.setSelectedWorld(null);
    }
}
