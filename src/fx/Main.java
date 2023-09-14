package fx;

import fx.consts.Constants;
import fx.consts.FilePaths;
import fx.consts.StyleSheetsPaths;
import fx.views.Orchestrator.OrchestratorController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Scene createMainScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FilePaths.ORCHESTRATOR_FXML_PATH));
        Scene mainScene = new Scene(loader.load());
        OrchestratorController controller = loader.getController();

        controller.addScene(mainScene);
        controller.setThemeToAllScenes(StyleSheetsPaths.DEFAULT_THEME_CSS);

        return mainScene;
    }
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(Constants.APP_TITLE);
        stage.setScene(createMainScene());
        stage.show();

    }
}
