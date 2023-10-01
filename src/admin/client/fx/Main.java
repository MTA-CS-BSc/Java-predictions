package admin.client.fx;

import admin.client.consts.App;
import admin.client.consts.FilePaths;
import admin.client.fx.orchestrator.controllers.OrchestratorController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Scene createMainScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(FilePaths.ADMIN_ORCHESTRATOR_FXML));
        Scene mainScene = new Scene(loader.load());
        OrchestratorController controller = loader.getController();

        //TODO: Add themes
//        controller.addScene(mainScene);
//        controller.setThemeToAllScenes(StyleSheetsPaths.DEFAULT_THEME_CSS);

        return mainScene;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(App.APP_TITLE);
        stage.setScene(createMainScene());
        stage.show();
    }
}
