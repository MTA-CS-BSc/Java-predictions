package fx.mgmt.controllers;

import consts.Animations;
import fx.threadpool.controllers.ThreadsAmountSetterController;
import fx.xml.loader.controllers.XmlLoaderController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MgmtController implements Initializable {
    @FXML private XmlLoaderController xmlLoaderController;
    @FXML private ThreadsAmountSetterController threadsAmountSetterController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Animations.IS_ANIMATIONS_ON.setValue(true);
    }
}
