package fx.mgmt.threadpool.controllers;

import consts.FilePaths;
import fx.Main;
import fx.themes.ScenesStore;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
        Stage stage = new Stage();
        Scene threadsScene = new Scene(loader.load());

        ScenesStore.SCENES_PROPERTY.add(threadsScene);

        stage.setScene(threadsScene);
        stage.show();
    }
}
