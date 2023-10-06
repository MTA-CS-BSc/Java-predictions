package fx;

import consts.App;
import consts.paths.FXMLPaths;
import fx.themes.ScenesStore;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Scene createMainScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(FXMLPaths.USER_ORCHESTRATOR_FXML));
        Scene mainScene = new Scene(loader.load());
        ScenesStore.SCENES_PROPERTY.add(mainScene);

        return mainScene;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(App.APP_TITLE);
        stage.setScene(createMainScene());
        stage.show();
    }
}
