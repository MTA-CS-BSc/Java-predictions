package fx.views.AllScreensUpperBar;

import fx.views.AppMenu.AppMenuController;
import fx.views.DetailsScreen.DetailsScreenController;
import fx.views.HeaderComponent.HeaderComponentController;
import fx.views.NewExecution.NewExecutionController;
import fx.views.Results.ResultsScreenController;
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
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void addScene(Scene scene) {
        appMenuController.addScene(scene);
    }

    public void setThemeToAllScenes(String cssPath) {
        appMenuController.setThemeToAllScenes(cssPath);
    }

    public void setHeaderDetailsController(DetailsScreenController controller) {
        headerComponentController.setDetailsScreenController(controller);
    }

    public void setHeaderNewExecutionController(NewExecutionController controller) {
        headerComponentController.setNewExecutionController(controller);
    }

    public void setHeaderResultsScreenController(ResultsScreenController controller) {
        headerComponentController.setResultsScreenController(controller);
    }

    public HeaderComponentController getHeaderComponentController() {
        return headerComponentController;
    }
}
