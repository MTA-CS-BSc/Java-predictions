package fx.controllers;

import com.google.gson.Gson;
import com.sun.xml.internal.ws.util.StringUtils;
import dtos.ResponseDTO;
import dtos.SingleSimulationDTO;
import dtos.WorldDTO;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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

                    //TODO: move from here
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
    private void handleShowTreeView() throws Exception {
        if (new Gson().fromJson(engineAPI.isXmlLoaded().getData(), Boolean.class)) {
            handleAddCategories();
            handleShowCategoriesData();
        }

        else {
            TrayNotification tray = new TrayNotification("FAILURE", "XML was not loaded, nothing to show.", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.SLIDE);
            tray.showAndDismiss(new Duration(2000));
        }

    }
    private void handleShowCategoriesData() throws Exception {
        SingleSimulationDTO simulation = new Gson().fromJson(engineAPI.getSimulationDetails().getData(),
                SingleSimulationDTO.class);

        WorldDTO world = simulation.getWorld();

        showEnvironment(world);
        showEntities(world);
        showGrid(world);
    }
    private void showEntities(WorldDTO world) throws Exception {
        TreeItem<String> entities = worldCategoriesTreeView.getRoot().getChildren().stream().filter(element -> element.getValue().equalsIgnoreCase(WorldCategoriesTreeView.ENTITIES.name()))
                .findFirst().orElseThrow(() -> new Exception("Error"));

        Collection<TreeItem<String>> entitiesNames = world.getEntities().stream()
                .map(entity -> new TreeItem<>(entity.getName()))
                .collect(Collectors.toList());

        entities.getChildren().addAll(entitiesNames);
    }
    private void showEnvironment(WorldDTO world) throws Exception {
        TreeItem<String> environment = worldCategoriesTreeView.getRoot().getChildren().stream().filter(element -> element.getValue().equalsIgnoreCase(WorldCategoriesTreeView.ENVIRONMENT.name()))
                .findFirst().orElseThrow(() -> new Exception("Error"));

        Collection<TreeItem<String>> envVars = world.getEnvironment().stream()
                .map(property -> new TreeItem<>(property.getName()))
                .collect(Collectors.toList());

        environment.getChildren().addAll(envVars);
    }
    private void showGrid(WorldDTO world) throws Exception {
        TreeItem<String> rows = new TreeItem<>("Rows");
        TreeItem<String> rowsAmount = new TreeItem<>(String.valueOf(world.getGridRows()));

        rows.getChildren().add(rowsAmount);

        TreeItem<String> cols = new TreeItem<>("Columns");
        TreeItem<String> columnsAmount = new TreeItem<>(String.valueOf(world.getGridColumns()));

        cols.getChildren().add(columnsAmount);

        TreeItem<String> grid = worldCategoriesTreeView.getRoot().getChildren()
                .stream()
                .filter(element -> element.getValue().equalsIgnoreCase(WorldCategoriesTreeView.GRID.name()))
                .findFirst().orElseThrow(() -> new Exception("Error"));

        grid.getChildren().add(rows);
        grid.getChildren().add(cols);
    }
    private void handleAddCategories() {
        TreeItem<String> root = new TreeItem<>(StringUtils.capitalize(WorldCategoriesTreeView.WORLD.name().toLowerCase()));
        Collection<TreeItem<String>> categories = Arrays.stream(WorldCategoriesTreeView.values())
                .filter(element -> element.ordinal() > 0)
                .map(item -> new TreeItem<>(StringUtils.capitalize(item.name().toLowerCase())))
                .collect(Collectors.toList());
        root.getChildren().addAll(categories);

        worldCategoriesTreeView.setRoot(root);
    }
    @FXML
    private void handleShowXmlLog(ActionEvent event) {
        //TODO: Not implemented
    }
}
