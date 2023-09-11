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
        Scene mainScene = new Scene(loader.load());
        DetailsScreenController detailsScreenController = loader.getController();

        detailsScreenController.getAllScreensUpperBarController().addScene(mainScene);
        detailsScreenController.getAllScreensUpperBarController().setThemeToAllScenes(StyleSheetsPaths.DEFAULT_THEME_CSS);

        return mainScene;
    }
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(Constants.APP_TITLE);
        stage.setScene(createMainScene());
        stage.show();

    }
}
