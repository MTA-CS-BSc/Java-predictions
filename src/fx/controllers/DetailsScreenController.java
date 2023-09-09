package fx.controllers;

import com.google.gson.Gson;
import com.sun.xml.internal.ws.util.StringUtils;
import dtos.ResponseDTO;
import dtos.SingleSimulationDTO;
import dtos.StopConditionDTO;
import dtos.WorldDTO;
import dtos.actions.*;
import engine.EngineAPI;
import fx.models.DetailsScreen.*;
import fx.models.DetailsScreen.actions.*;
import fx.models.WorldTreeViewCategories;
import helpers.PropTypes;
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
    private TreeView<TreeItemModel> selectedComponentDetailsTreeView;
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
        showTermination(world);
        showRules(world);

        worldCategoriesTreeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (newValue.getValue() instanceof EntityModel)
                showEntityDetails((EntityModel)newValue.getValue());

            else if (newValue.getValue() instanceof PropertyModel)
                showEnvVarDetails((PropertyModel)newValue.getValue());

            else
                selectedComponentDetailsTreeView.setRoot(null);
        });
    }
    private void showEnvVarDetails(PropertyModel envVar) {
        selectedComponentDetailsTreeView.setRoot(new TreeItem<>(envVar));

        TreeItem<TreeItemModel> type = new TreeItem<>(new TreeItemModel("Type"));
        type.getChildren().add(new TreeItem<>(new TreeItemModel(StringUtils.capitalize(envVar.getType()))));

        selectedComponentDetailsTreeView.getRoot().getChildren().add(type);

        if (!envVar.hasNoRange()) {
            TreeItem<TreeItemModel> range = new TreeItem<>(new TreeItemModel("Range"));

            range.getChildren().add(new TreeItem<>(new TreeItemModel(String.format("[%.2f, %.2f]",
                    envVar.getRange().getFrom(), envVar.getRange().getTo()))));

            selectedComponentDetailsTreeView.getRoot().getChildren().add(range);
        }
    }
    private void showEntityDetails(EntityModel entityModel) {
        selectedComponentDetailsTreeView.setRoot(new TreeItem<>(entityModel));

        TreeItem<TreeItemModel> propertiesTreeItem = new TreeItem<>(new TreeItemModel("Properties"));
        entityModel.getProperties().forEach(property -> {
            TreeItem<TreeItemModel> propertyTreeItem = new TreeItem<>(property);

            TreeItem<TreeItemModel> type = new TreeItem<>(new TreeItemModel("Type"));
            type.getChildren().add(new TreeItem<>(new TreeItemModel(StringUtils.capitalize(property.getType()))));
            propertyTreeItem.getChildren().add(type);

            if (PropTypes.NUMERIC_PROPS.contains(property.getType())) {
                TreeItem<TreeItemModel> range = new TreeItem<>(new TreeItemModel("Range"));

                range.getChildren().add(new TreeItem<>(new TreeItemModel(String.format("[%.2f, %.2f]",
                        property.getRange().getFrom(), property.getRange().getTo()))));

                propertyTreeItem.getChildren().add(range);
            }

            TreeItem<TreeItemModel> value = new TreeItem<>(new TreeItemModel("Value"));
            value.getChildren().add(new TreeItem<>(new TreeItemModel(property.getValue())));
            propertyTreeItem.getChildren().add(value);

            propertiesTreeItem.getChildren().add(propertyTreeItem);
        });

        selectedComponentDetailsTreeView.getRoot().getChildren().add(propertiesTreeItem);
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
    private void showTermination(WorldDTO world) {
        TreeItem<TreeItemModel> terminationTreeItem = new TreeItem<>(new TreeItemModel(StringUtils.capitalize(WorldTreeViewCategories.TERMINATION.name().toLowerCase())));

        if (world.getTermination().isByUser())
            terminationTreeItem.getChildren().add(new TreeItem<>(new TreeItemModel("By User")));

        else {
            for (StopConditionDTO stopCondition : world.getTermination().getStopConditions()) {
                TreeItem<TreeItemModel> byWho = new TreeItem<>(new TreeItemModel(StringUtils.capitalize(stopCondition.getByWho())));
                byWho.getChildren().add(new TreeItem<>(new TreeItemModel(String.valueOf(stopCondition.getCount()))));
                terminationTreeItem.getChildren().add(byWho);
            }
        }

        worldCategoriesTreeView.getRoot().getChildren().add(terminationTreeItem);

    }
    private void showRules(WorldDTO world) {
        List<RuleModel> rules = world.getRules().stream()
                .map(rule -> {
                    List<ActionModel> actions = rule.getActions().stream()
                            .map(this::createActionModel).collect(Collectors.toList());
                    return new RuleModel(rule.getName(), rule.getTicks(), rule.getProbability(), actions);
                })
                .collect(Collectors.toList());

        TreeItem<TreeItemModel> rulesTreeItem = new TreeItem<>(new RulesModel(rules));

        rules.forEach(rule -> {
            TreeItem<TreeItemModel> propertyTreeItem = new TreeItem<>(rule);
            rulesTreeItem.getChildren().add(propertyTreeItem);
        });

        worldCategoriesTreeView.getRoot().getChildren().add(rulesTreeItem);

    }
    private ActionModel createActionModel(ActionDTO action) {
        SecondaryEntityModel secondaryEntityModel = null;
        String entityName = action.getEntityName();

        if (action.isSecondaryEntityExists())
            secondaryEntityModel = new SecondaryEntityModel(action.getSecondaryEntity().getEntityName());

        if (action instanceof SetDTO) {
            SetDTO setAction = (SetDTO) action;
            return new SetModel(entityName, secondaryEntityModel,
                    setAction.getPropertyName(), setAction.getValue());
        }

        else if (action instanceof ReplaceDTO) {
            ReplaceDTO replaceAction = (ReplaceDTO) action;
            return new ReplaceModel(entityName, secondaryEntityModel,
                    replaceAction.getKill(), replaceAction.getCreate(), replaceAction.getMode());
        }

        else if (action instanceof CalculationDTO) {
            CalculationDTO calcAction = (CalculationDTO)action;
            return new CalculationModel(entityName, secondaryEntityModel,
                    calcAction.getOperationType(), calcAction.getArg1(), calcAction.getArg2());
        }

        else if (action instanceof KillDTO)
            return new KillModel(entityName, secondaryEntityModel);

        else if (action instanceof ProximityDTO) {
            ProximityDTO proximityAction = (ProximityDTO) action;
            return new ProximityModel(secondaryEntityModel, proximityAction.getSourceEntity(),
                    proximityAction.getTargetEntity(), proximityAction.getDepth(),
                    proximityAction.getActionsAmount());
        }

        else if (action instanceof IncreaseDecreaseDTO) {
            IncreaseDecreaseDTO increaseDecreaseAction = (IncreaseDecreaseDTO) action;
            return new IncreaseDecreaseModel(action.getType(), entityName,
                    secondaryEntityModel, increaseDecreaseAction.getPropertyName(), increaseDecreaseAction.getBy());
        }

        else if (action instanceof ConditionDTO) {
            if (action instanceof SingleConditionDTO) {
                SingleConditionDTO condition = (SingleConditionDTO) action;
                return new SingleConditionModel(entityName, secondaryEntityModel,
                        condition.getThenActionsAmount(), condition.getElseActionsAmount(),
                        condition.getOperator(), condition.getProperty(), condition.getValue());
            }

            else {
                MultipleConditionDTO condition = ((MultipleConditionDTO) action);
                return new MultipleConditionModel(entityName, secondaryEntityModel,
                        condition.getThenActionsAmount(), condition.getElseActionsAmount(),
                        condition.getLogicalOperator(), condition.getConditionsAmount());
            }
        }

        return null;
    }
    @FXML
    private void handleShowXmlLog() {
        xmlErrorsAlert.showAndWait();
    }
}
