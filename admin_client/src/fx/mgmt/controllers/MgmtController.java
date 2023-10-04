package fx.mgmt.controllers;

import consts.Animations;
import fx.mgmt.threadpool.controllers.ThreadsAmountSetterController;
import fx.mgmt.world.details.controllers.WorldDetailsController;
import fx.xml.loader.controllers.XmlLoaderController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MgmtController implements Initializable {
    @FXML private XmlLoaderController xmlLoaderController;
    @FXML private ThreadsAmountSetterController threadsAmountSetterController;
    @FXML private WorldDetailsController worldDetailsController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Animations.IS_ANIMATIONS_ON.setValue(true);
    }
}
