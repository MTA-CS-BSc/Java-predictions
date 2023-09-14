package fx.views.Orchestrator;

import fx.views.Results.ResultsScreenController;
import fx.views.AllScreensUpperBar.AllScreensUpperBarController;
import fx.views.DetailsScreen.DetailsScreenController;
import fx.views.NewExecution.NewExecutionController;
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
    @FXML
    private ResultsScreenController resultsScreenController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allScreensUpperBarController.setHeaderDetailsController(detailsScreenController);
        allScreensUpperBarController.setHeaderNewExecutionController(newExecutionController);
        allScreensUpperBarController.setHeaderResultsScreenController(resultsScreenController);
        newExecutionController.setHeaderComponentController(allScreensUpperBarController.getHeaderComponentController());
        resultsScreenController.setHeaderController(allScreensUpperBarController.getHeaderComponentController());
        initializeScreen();
    }

    public void setThemeToAllScenes(String cssPath) {
        allScreensUpperBarController.setThemeToAllScenes(cssPath);
    }

    public void addScene(Scene scene) {
        allScreensUpperBarController.addScene(scene);
    }

    private void initializeScreen() {
        newExecutionController.getContainer().setVisible(false);
        resultsScreenController.getContainer().setVisible(false);
    }
}
