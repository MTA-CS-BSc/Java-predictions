package fx.component.mgmt;

import fx.component.loader.XmlLoaderController;
import fx.component.mgmt.threadpool.queue.ThreadpoolQueueController;
import fx.component.mgmt.threadpool.setter.ThreadsAmountSetterController;
import fx.component.mgmt.valid.worlds.XmlValidWorldsController;
import fx.component.mgmt.world.details.WorldDetailsController;
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
