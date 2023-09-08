package fx;

import fx.consts.Constants;
import fx.consts.FilePaths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(FilePaths.WELCOME_SCREEN_FXML_PATH)));
        Scene scene = new Scene(root);

        stage.setTitle(Constants.APP_TITLE);
        stage.setScene(scene);
        stage.show();
    }
}
