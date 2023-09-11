package fx;

import fx.consts.Constants;
import fx.consts.FilePaths;
import fx.views.StyleSheetsPaths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    private Scene createMainScene(Parent root) {
        Scene scene = new Scene(root);
        scene.getStylesheets().add(String.valueOf(getClass().getResource(StyleSheetsPaths.DEFAULT_THEME_CSS)));
        return scene;
    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(FilePaths.DETAILS_SCREEN_FXML_PATH)));
        stage.setTitle(Constants.APP_TITLE);
        stage.setScene(createMainScene(root));
        stage.show();

    }
}
