package fx.controllers;

import com.google.gson.Gson;
import com.sun.xml.internal.ws.util.StringUtils;
import dtos.ResponseDTO;
import dtos.SingleSimulationDTO;
import dtos.StopConditionDTO;
import dtos.WorldDTO;
import engine.EngineAPI;
import fx.models.WorldCategoriesTreeView;
import fx.modules.GuiUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
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
    @FXML
    private Button xmlLogButton;
    private Alert xmlErrorsAlert;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        engineAPI = new EngineAPI();
        xmlLogButton.setVisible(false);
        xmlErrorsAlert = new Alert(Alert.AlertType.INFORMATION);
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
                    xmlLogButton.setVisible(false);
                }

                else {
                    tray = new TrayNotification("FAILURE", "XML was not loaded. For details, see XML log", NotificationType.ERROR);
                    xmlLogButton.setVisible(true);
                    xmlErrorsAlert.setContentText(response.getErrorDescription().getCause());
                }

                tray.setAnimationType(AnimationType.SLIDE);
                tray.showAndDismiss(new Duration(2000));
            }

            catch (Exception e) {
                //TODO: Handle error
            }
        }
    }
    private void handleShowTreeView() {
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
    private void handleAddCategories() {
        TreeItem<String> root = new TreeItem<>(StringUtils.capitalize(WorldCategoriesTreeView.WORLD.name().toLowerCase()));
        Collection<TreeItem<String>> categories = Arrays.stream(WorldCategoriesTreeView.values())
                .filter(element -> element.ordinal() > 0)
                .map(item -> new TreeItem<>(StringUtils.capitalize(item.name().toLowerCase())))
                .collect(Collectors.toList());
        root.getChildren().addAll(categories);

        worldCategoriesTreeView.setRoot(root);
    }
    private void handleShowCategoriesData() {
        SingleSimulationDTO simulation = new Gson().fromJson(engineAPI.getSimulationDetails().getData(),
                SingleSimulationDTO.class);

        WorldDTO world = simulation.getWorld();

        showEnvironment(world);
        showEntities(world);
        showGrid(world);
        showTermination(world);
        showRules(world);
    }
    private void showRules(WorldDTO world) {
        TreeItem<String> rules = GuiUtils.findTreeItemByValue(worldCategoriesTreeView.getRoot(), WorldCategoriesTreeView.RULES.name());

        assert rules != null;

        Collection<TreeItem<String>> rulesNames = world.getRules().stream()
                .map(rule -> new TreeItem<>(rule.getName()))
                .collect(Collectors.toList());

        rules.getChildren().addAll(rulesNames);
    }
    private void showEntities(WorldDTO world) {
        TreeItem<String> entities = GuiUtils.findTreeItemByValue(worldCategoriesTreeView.getRoot(), WorldCategoriesTreeView.ENTITIES.name());

        Collection<TreeItem<String>> entitiesNames = world.getEntities().stream()
                .map(entity -> new TreeItem<>(entity.getName()))
                .collect(Collectors.toList());

        assert entities != null;
        entities.getChildren().addAll(entitiesNames);
    }
    private void showEnvironment(WorldDTO world) {
        TreeItem<String> environment = GuiUtils.findTreeItemByValue(worldCategoriesTreeView.getRoot(), WorldCategoriesTreeView.ENVIRONMENT.name());

        Collection<TreeItem<String>> envVars = world.getEnvironment().stream()
                .map(property -> new TreeItem<>(property.getName()))
                .collect(Collectors.toList());

        assert environment != null;
        environment.getChildren().addAll(envVars);
    }
    private void showGrid(WorldDTO world) {
        TreeItem<String> rows = new TreeItem<>("Rows");
        TreeItem<String> rowsAmount = new TreeItem<>(String.valueOf(world.getGridRows()));

        rows.getChildren().add(rowsAmount);

        TreeItem<String> cols = new TreeItem<>("Columns");
        TreeItem<String> columnsAmount = new TreeItem<>(String.valueOf(world.getGridColumns()));

        cols.getChildren().add(columnsAmount);

        TreeItem<String> grid = GuiUtils.findTreeItemByValue(worldCategoriesTreeView.getRoot(), WorldCategoriesTreeView.GRID.name());

        assert grid != null;
        grid.getChildren().add(rows);
        grid.getChildren().add(cols);
    }
    private void showTermination(WorldDTO world) {
        TreeItem<String> termination = GuiUtils.findTreeItemByValue(worldCategoriesTreeView.getRoot(), WorldCategoriesTreeView.TERMINATION.name());

        assert termination != null;

        if (world.getTermination().isByUser())
            termination.getChildren().add(new TreeItem<>("By User"));

        else {
            for (StopConditionDTO stopCondition : world.getTermination().getStopConditions()) {
                TreeItem<String> byWho = new TreeItem<>(StringUtils.capitalize(stopCondition.getByWho()));
                byWho.getChildren().add(new TreeItem<>(String.valueOf(stopCondition.getCount())));
                termination.getChildren().add(byWho);
            }
        }

    }
    @FXML
    private void handleShowXmlLog(ActionEvent event) {
        xmlErrorsAlert.setResizable(true);
        xmlErrorsAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        xmlErrorsAlert.setTitle("Latest loaded XML inspection");
        xmlErrorsAlert.setHeaderText("Validation errors");
        xmlErrorsAlert.showAndWait();
    }
}
