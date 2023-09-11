package fx;

import fx.consts.Constants;
import fx.consts.FilePaths;
import fx.consts.StyleSheetsPaths;
import fx.controllers.DetailsScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Scene createMainScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FilePaths.DETAILS_SCREEN_FXML_PATH));
        DetailsScreenController detailsScreenController = loader.getController();
        Scene mainScene = new Scene(loader.load());

        detailsScreenController.getAllScreensUpperBarController()
                .getAppMenuController().addScene(mainScene);
        detailsScreenController.getAllScreensUpperBarController()
                .getAppMenuController().setTheme(mainScene, StyleSheetsPaths.DEFAULT_THEME_CSS);

        return mainScene;
    }
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(Constants.APP_TITLE);
        stage.setScene(createMainScene());
        stage.show();

    }
}
