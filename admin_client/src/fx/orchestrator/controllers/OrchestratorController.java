package fx.orchestrator.controllers;

import fx.header.controllers.HeaderController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

import java.net.URL;
import java.util.ResourceBundle;

public class OrchestratorController implements Initializable {
    @FXML private HeaderController headerController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    public void setThemeToAllScenes(String cssPath) {
        headerController.setThemeToAllScenes(cssPath);
    }

    public void addScene(Scene scene) {
        headerController.addScene(scene);
    }
}