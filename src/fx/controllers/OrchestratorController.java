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
    @FXML
    private NewExecutionController newExecutionController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allScreensUpperBarController.setHeaderDetailsController(detailsScreenController);
        allScreensUpperBarController.setHeaderNewExecutionController(newExecutionController);

        initializeScreen();
    }

    public void setThemeToAllScenes(String cssPath) {
        allScreensUpperBarController.setThemeToAllScenes(cssPath);
    }

    public void addScene(Scene scene) {
        allScreensUpperBarController.addScene(scene);
    }

    private void initializeScreen() {
        newExecutionController.getGridPane().setVisible(false);
    }
}
