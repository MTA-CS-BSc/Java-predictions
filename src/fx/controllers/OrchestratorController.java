package fx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

import java.net.URL;
import java.util.ResourceBundle;

public class OrchestratorController implements Initializable {
    @FXML
    private DetailsScreenController detailsScreenController;
    @FXML
    private AllScreensUpperBarController allScreensUpperBarController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    public void setThemeToAllScenes(String cssPath) {
        allScreensUpperBarController.setThemeToAllScenes(cssPath);
    }

    public void addScene(Scene scene) {
        allScreensUpperBarController.addScene(scene);
    }
}
