package fx.controllers;

import dtos.ResponseDTO;
import engine.EngineAPI;
import fx.models.WorldCategoriesTreeView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    private EngineAPI engineAPI;
    @FXML
    private TextArea currentXmlFilePath;
    @FXML
    private TreeView<String> worldCategoriesTreeView;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        engineAPI = new EngineAPI();
    }

    @FXML
    private void handleLoadXmlButtonClick(ActionEvent event) {
        Node source = (Node) event.getSource();
        Scene scene = source.getScene();
        Window window = scene.getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an XML file");
        File file = fileChooser.showOpenDialog(window);

        if (file != null) {
            try {
                ResponseDTO response = engineAPI.loadXml(file.getAbsolutePath());
                TrayNotification tray;

                if (response.getStatus() == 200) {
                    tray = new TrayNotification("SUCCESS", "XML loaded successfully!", NotificationType.SUCCESS);
                    currentXmlFilePath.setText(file.getAbsolutePath());

                    //TODO: Move next line to only on Details button click
                    handleShowTreeView();
                }

                else
                    tray = new TrayNotification("FAILURE", "XML was not loaded. For details, see XML log", NotificationType.ERROR);

                tray.setAnimationType(AnimationType.SLIDE);
                tray.showAndDismiss(new Duration(2000));
            }

            catch (Exception e) {
                //TODO: Handle error
            }
        }
    }

    private void handleShowTreeView() {
//        WorldDTO world = new Gson().fromJson(engineAPI.getSimulationDetails().getData(),
//                SingleSimulationDTO.class).getWorld();

        handleAddCategories();
    }

    private void handleAddCategories() {
        TreeItem<String> root = new TreeItem<>("World");
        root.getChildren().add(new TreeItem<>(WorldCategoriesTreeView.ENVIRONMENT.name().toLowerCase()));
        root.getChildren().add(new TreeItem<>(WorldCategoriesTreeView.GRID.name().toLowerCase()));
        root.getChildren().add(new TreeItem<>(WorldCategoriesTreeView.ENTITIES.name().toLowerCase()));
        root.getChildren().add(new TreeItem<>(WorldCategoriesTreeView.RULES.name().toLowerCase()));
        root.getChildren().add(new TreeItem<>(WorldCategoriesTreeView.TERMINATION.name().toLowerCase()));

        worldCategoriesTreeView.setRoot(root);
    }
    @FXML
    private void handleShowXmlLog(ActionEvent event) {
        //TODO: Not implemented
    }
}
