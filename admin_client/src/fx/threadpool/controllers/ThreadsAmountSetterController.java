package fx.threadpool.controllers;

import consts.FilePaths;
import fx.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ThreadsAmountSetterController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    @FXML
    private void handleSetThreadsAmountClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(FilePaths.THREADS_AMOUNT_POPUP_FXML));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
