package fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("views/WelcomeScreen.fxml")));

        Scene scene = new Scene(root);
        stage.setTitle("Predictions");
        stage.setScene(scene);
        stage.show();
    }
}
