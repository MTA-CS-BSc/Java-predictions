package fx.controllers;

import com.google.gson.Gson;
import com.sun.xml.internal.ws.util.StringUtils;
import dtos.ResponseDTO;
import dtos.SingleSimulationDTO;
import dtos.WorldDTO;
import engine.EngineAPI;
import fx.models.DetailsScreen.*;
import fx.models.WorldTreeViewCategories;
import javafx.beans.binding.Bindings;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DetailsScreenController implements Initializable {
    private EngineAPI engineAPI;
    @FXML
    private TextArea currentXmlFilePath;
    @FXML
    private TreeView<TreeItemModel> worldCategoriesTreeView;
    @FXML
    private Button xmlLogButton;
    private Alert xmlErrorsAlert;
    @FXML
    private Button detailsButton;
    @FXML
    private Button newExecutionButton;
    @FXML
    private Button resultsButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        engineAPI = new EngineAPI();
        detailsButton.disableProperty().bind(Bindings.isEmpty(currentXmlFilePath.textProperty()));
        newExecutionButton.disableProperty().bind(Bindings.isEmpty(currentXmlFilePath.textProperty()));
        resultsButton.disableProperty().bind(Bindings.createBooleanBinding(this::isHistoryEmpty));
        initializeXmlErrorsAlert();
    }
    private boolean isHistoryEmpty() {
        return !new Gson().fromJson(engineAPI.isXmlLoaded().getData(), Boolean.class)
            || new Gson().fromJson(engineAPI.isHistoryEmpty().getData(), Boolean.class);
    }
    private void initializeXmlErrorsAlert() {
        xmlErrorsAlert = new Alert(Alert.AlertType.INFORMATION);
        xmlErrorsAlert.setResizable(true);
        xmlErrorsAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        xmlErrorsAlert.setTitle("Latest loaded XML inspection");
        xmlErrorsAlert.setHeaderText("Validation errors");
    }
    @FXML
    private void handleLoadXmlButtonClick(ActionEvent event) {
        Node source = (Node) event.getSource();
        Scene scene = source.getScene();
        Window window = scene.getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an XML file");
        File file = fileChooser.showOpenDialog(window);

        if (file != null)
            handleNotNullXmlFileEntered(file);
    }
    private void handleNotNullXmlFileEntered(File file) {
        try {
            ResponseDTO response = engineAPI.loadXml(file.getAbsolutePath());
            TrayNotification tray;

            if (response.getStatus() == 200) {
                tray = new TrayNotification("SUCCESS", "XML was loaded successfully!", NotificationType.SUCCESS);
                currentXmlFilePath.setText(file.getAbsolutePath());
                xmlLogButton.setVisible(false);
            }

            else {
                tray = new TrayNotification("FAILURE", "XML was not loaded. For details, see the XML log.", NotificationType.ERROR);
                xmlLogButton.setVisible(true);
                xmlErrorsAlert.setContentText(response.getErrorDescription().getCause());
            }

            tray.setAnimationType(AnimationType.SLIDE);
            tray.showAndDismiss(new Duration(2000));
        }

        catch (Exception e) {
            xmlLogButton.setVisible(true);
            xmlErrorsAlert.setContentText("File not found or corrupted!");
        }
    }
    public void handleShowSimulationDetails() {
        if (new Gson().fromJson(engineAPI.isXmlLoaded().getData(), Boolean.class)) {
            worldCategoriesTreeView.setRoot(new TreeItem<>(new TreeItemModel(StringUtils.capitalize(WorldTreeViewCategories.WORLD.name().toLowerCase()))));
            handleShowCategoriesData();
        }

        else {
            TrayNotification tray = new TrayNotification("FAILURE", "XML was not loaded, nothing to show.", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.SLIDE);
            tray.showAndDismiss(new Duration(2000));
        }

    }
    private void handleShowCategoriesData() {
        WorldDTO world = new Gson().fromJson(engineAPI.getSimulationDetails().getData(),
                SingleSimulationDTO.class).getWorld();

        showEnvironment(world);
        showEntities(world);
        showGrid(world);
//        showTermination(world);
//        showRules(world);
    }
    private void showEntities(WorldDTO world) {
        List<EntityModel> entities = world.getEntities().stream()
                .map(entity -> {
                    List<EntityPropertyModel> props = entity.getProperties().stream()
                            .map(element -> {
                                RangeModel range = null;

                                if (!element.hasNoRange())
                                    range = new RangeModel(element.getRange().getFrom(), element.getRange().getTo());

                                return new EntityPropertyModel(element.getName(), element.getType(), range, element.getValue());
                            }).collect(Collectors.toList());
                    return new EntityModel(entity.getName(), props);
                }).collect(Collectors.toList());

        TreeItem<TreeItemModel> entitiesTreeItem = new TreeItem<>(new EntitiesModel(entities));

        entities.forEach(entity -> {
            TreeItem<TreeItemModel> entityTreeItem = new TreeItem<>(entity);
            entitiesTreeItem.getChildren().add(entityTreeItem);
        });

        worldCategoriesTreeView.getRoot().getChildren().add(entitiesTreeItem);
    }
    private void showEnvironment(WorldDTO world) {
        List<PropertyModel> envVars = world.getEnvironment().stream()
                .map(property -> {
                    RangeModel range = null;

                    if (!property.hasNoRange())
                        range = new RangeModel(property.getRange().getFrom(), property.getRange().getTo());

                    return new PropertyModel(property.getName(), property.getType(), range);
                }).collect(Collectors.toList());

        TreeItem<TreeItemModel> environmentTreeItem = new TreeItem<>(new EnvironmentModel(envVars));

        envVars.forEach(property -> {
           TreeItem<TreeItemModel> propertyTreeItem = new TreeItem<>(property);
           environmentTreeItem.getChildren().add(propertyTreeItem);
        });

        worldCategoriesTreeView.getRoot().getChildren().add(environmentTreeItem);
    }
    private void showGrid(WorldDTO world) {
        TreeItem<TreeItemModel> rows = new TreeItem<>(new TreeItemModel("Rows"));
        rows.getChildren().add(new TreeItem<>(new TreeItemModel(String.valueOf(world.getGridRows()))));

        TreeItem<TreeItemModel> cols = new TreeItem<>(new TreeItemModel("Columns"));
        cols.getChildren().add(new TreeItem<>(new TreeItemModel(String.valueOf(world.getGridColumns()))));

        TreeItem<TreeItemModel> gridTreeItem = new TreeItem<>(new TreeItemModel(StringUtils.capitalize(WorldTreeViewCategories.GRID.name().toLowerCase())));
        gridTreeItem.getChildren().add(rows);
        gridTreeItem.getChildren().add(cols);

        worldCategoriesTreeView.getRoot().getChildren().add(gridTreeItem);
    }
//    private void showRules(WorldDTO world) {
//        TreeItem<String> rules = GuiUtils.findTreeItemByValue(worldCategoriesTreeView.getRoot(), WorldCategoriesTreeView.RULES.name());
//
//        assert rules != null;
//
//        Collection<TreeItem<String>> rulesNames = world.getRules().stream()
//                .map(rule -> new TreeItem<>(rule.getName()))
//                .collect(Collectors.toList());
//
//        rules.getChildren().addAll(rulesNames);
//    }
//    private void showEntities(WorldDTO world) {
//        TreeItem<String> entities = GuiUtils.findTreeItemByValue(worldCategoriesTreeView.getRoot(), WorldCategoriesTreeView.ENTITIES.name());
//
//        Collection<TreeItem<String>> entitiesNames = world.getEntities().stream()
//                .map(entity -> new TreeItem<>(entity.getName()))
//                .collect(Collectors.toList());
//
//        assert entities != null;
//        entities.getChildren().addAll(entitiesNames);
//    }
//    private void showEnvironment(WorldDTO world) {
//        TreeItem<String> environment = GuiUtils.findTreeItemByValue(worldCategoriesTreeView.getRoot(), WorldCategoriesTreeView.ENVIRONMENT.name());
//
//        Collection<TreeItem<String>> envVars = world.getEnvironment().stream()
//                .map(property -> new TreeItem<>(property.getName()))
//                .collect(Collectors.toList());
//
//        assert environment != null;
//        environment.getChildren().addAll(envVars);
//    }

//    private void showTermination(WorldDTO world) {
//        TreeItem<String> termination = GuiUtils.findTreeItemByValue(worldCategoriesTreeView.getRoot(), WorldCategoriesTreeView.TERMINATION.name());
//
//        assert termination != null;
//
//        if (world.getTermination().isByUser())
//            termination.getChildren().add(new TreeItem<>("By User"));
//
//        else {
//            for (StopConditionDTO stopCondition : world.getTermination().getStopConditions()) {
//                TreeItem<String> byWho = new TreeItem<>(StringUtils.capitalize(stopCondition.getByWho()));
//                byWho.getChildren().add(new TreeItem<>(String.valueOf(stopCondition.getCount())));
//                termination.getChildren().add(byWho);
//            }
//        }
//
//    }
    @FXML
    private void handleShowXmlLog() {
        xmlErrorsAlert.showAndWait();
    }
}
