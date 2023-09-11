package fx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

import java.net.URL;
import java.util.ResourceBundle;

public class AllScreensUpperBarController implements Initializable {
    @FXML
    private AppMenuController appMenuController;
    @FXML
    private HeaderComponentController headerComponentController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    public AppMenuController getAppMenuController() {
        return appMenuController;
    }

    public HeaderComponentController getHeaderComponentController() {
        return headerComponentController;
    }

    public void addScene(Scene scene) {
        appMenuController.addScene(scene);
    }
    public void setThemeToAllScenes(String cssPath) {
        appMenuController.setThemeToAllScenes(cssPath);
    }
}
