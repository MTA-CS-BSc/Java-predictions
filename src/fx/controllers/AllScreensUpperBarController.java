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
    protected void addScene(Scene scene) {
        appMenuController.addScene(scene);
    }
    protected void setThemeToAllScenes(String cssPath) {
        appMenuController.setThemeToAllScenes(cssPath);
    }
    protected void setHeaderDetailsController(DetailsScreenController controller) {
        headerComponentController.setDetailsScreenController(controller);
    }
    protected void setHeaderNewExecutionController(NewExecutionController controller) {
        headerComponentController.setNewExecutionController(controller);
    }
}
