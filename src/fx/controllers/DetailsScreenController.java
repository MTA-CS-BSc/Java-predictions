package fx.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.ws.util.StringUtils;
import dtos.ResponseDTO;
import dtos.SingleSimulationDTO;
import dtos.StopConditionDTO;
import dtos.WorldDTO;
import engine.EngineAPI;
import fx.models.DetailsScreen.*;
import fx.models.DetailsScreen.actions.*;
import fx.models.WorldTreeViewCategories;
import fx.modules.GuiUtils;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DetailsScreenController implements Initializable {
    private EngineAPI engineAPI;
    private Alert xmlErrorsAlert;
    @FXML
    private TextArea currentXmlFilePath;
    @FXML
    private TreeView<TreeItemModel> worldCategoriesTreeView;
    @FXML
    private TreeView<TreeItemModel> selectedComponentDetailsTreeView;
    @FXML
    private Button xmlLogButton;
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

        worldCategoriesTreeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (newValue.getValue() instanceof EntityModel)
                showEntityDetails((EntityModel)newValue.getValue());

            else if (newValue.getValue() instanceof PropertyModel)
                showEnvVarDetails((PropertyModel)newValue.getValue());

            else if (newValue.getValue() instanceof RuleModel)
                showRuleDetails((RuleModel)newValue.getValue());
            else
                selectedComponentDetailsTreeView.setRoot(null);
        });
    }
    private boolean isHistoryEmpty() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return !objectMapper.readValue(engineAPI.isXmlLoaded().getData(), Boolean.class)
                || objectMapper.readValue(engineAPI.isHistoryEmpty().getData(), Boolean.class);
    }
    private void initializeXmlErrorsAlert() {
        xmlErrorsAlert = new Alert(Alert.AlertType.INFORMATION);
        xmlErrorsAlert.setResizable(true);
        xmlErrorsAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        xmlErrorsAlert.setTitle("Latest loaded XML inspection");
        xmlErrorsAlert.setHeaderText("Validation errors");
    }
    @FXML
    private void handleShowXmlLog() {
        xmlErrorsAlert.showAndWait();
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

            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(new Duration(2000));
        }

        catch (Exception e) {
            xmlLogButton.setVisible(true);
            xmlErrorsAlert.setContentText("File not found or corrupted!");
        }
    }
    private TreeItem<TreeItemModel> getActionTreeItem(ActionModel actionModel) {
        TreeItem<TreeItemModel> actionTreeItem = new TreeItem<>(actionModel);

        if (!Objects.isNull(actionModel.getEntityName()) && !actionModel.getEntityName().isEmpty()) {
            TreeItem<TreeItemModel> entityNameTreeItem = new TreeItem<>(new TreeItemModel("Entity"));
            entityNameTreeItem.getChildren().add(new TreeItem<>(new TreeItemModel(actionModel.getEntityName())));
            actionTreeItem.getChildren().add(entityNameTreeItem);
        }

        if (!Objects.isNull(actionModel.getSecondaryEntity())) {
            TreeItem<TreeItemModel> secondaryEntityTreeItem = new TreeItem<>(new TreeItemModel("Secondary Entity"));
            secondaryEntityTreeItem.getChildren().add(new TreeItem<>(actionModel.getSecondaryEntity()));
            actionTreeItem.getChildren().add(secondaryEntityTreeItem);
        }

        addActionProps(actionTreeItem, actionModel);

        return actionTreeItem;
    }
    public void handleShowSimulationDetails() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        if (objectMapper.readValue(engineAPI.isXmlLoaded().getData(), Boolean.class))
            handleShowCategoriesData();

        else {
            TrayNotification tray = new TrayNotification("FAILURE", "XML was not loaded, nothing to show.", NotificationType.ERROR);
            tray.setAnimationType(AnimationType.FADE);
            tray.showAndDismiss(new Duration(2000));
        }

    }

    //#region Selected component
    private void showRuleDetails(RuleModel rule) {
        selectedComponentDetailsTreeView.setRoot(new TreeItem<>(rule));
        TreeItem<TreeItemModel> ticks = new TreeItem<>(new TreeItemModel("Ticks"));
        ticks.getChildren().add(new TreeItem<>(new TreeItemModel(String.valueOf(rule.getTicks()))));

        TreeItem<TreeItemModel> probability = new TreeItem<>(new TreeItemModel("Probability"));
        probability.getChildren().add(new TreeItem<>(new TreeItemModel(String.valueOf(rule.getProbability()))));

        TreeItem<TreeItemModel> actions = new TreeItem<>(new TreeItemModel("Actions"));
        rule.getActions().forEach(actionModel -> actions.getChildren().add(getActionTreeItem(actionModel)));

        selectedComponentDetailsTreeView.getRoot().getChildren().addAll(Arrays.asList(ticks, probability, actions));
    }
    private void addActionProps(TreeItem<TreeItemModel> actionTreeItem, ActionModel actionModel) {
        if (actionModel instanceof IncreaseDecreaseModel) {
            IncreaseDecreaseModel increaseDecreaseModel = (IncreaseDecreaseModel) actionModel;

            TreeItem<TreeItemModel> by = new TreeItem<>(new TreeItemModel("By"));
            by.getChildren().add(new TreeItem<>(new TreeItemModel(increaseDecreaseModel.getBy())));

            TreeItem<TreeItemModel> property = new TreeItem<>(new TreeItemModel("Property"));
            property.getChildren().add(new TreeItem<>(new TreeItemModel(increaseDecreaseModel.getPropertyName())));

            actionTreeItem.getChildren().addAll(Arrays.asList(property, by));
        }

        else if (actionModel instanceof CalculationModel) {
            CalculationModel calculationModel = (CalculationModel) actionModel;

            TreeItem<TreeItemModel> operationType = new TreeItem<>(new TreeItemModel("Operation type"));
            operationType.getChildren().add(new TreeItem<>(new TreeItemModel(calculationModel.getOperationType())));

            TreeItem<TreeItemModel> arg1 = new TreeItem<>(new TreeItemModel("Arg1"));
            arg1.getChildren().add(new TreeItem<>(new TreeItemModel(calculationModel.getArg1())));

            TreeItem<TreeItemModel> arg2 = new TreeItem<>(new TreeItemModel("Arg2"));
            arg2.getChildren().add(new TreeItem<>(new TreeItemModel(calculationModel.getArg2())));

            actionTreeItem.getChildren().addAll(Arrays.asList(operationType, arg1, arg2));
        }

        else if (actionModel instanceof ProximityModel) {
            ProximityModel proximityModel = (ProximityModel) actionModel;
            TreeItem<TreeItemModel> sourceEntity = new TreeItem<>(new TreeItemModel("Source entity"));
            TreeItem<TreeItemModel> targetEntity = new TreeItem<>(new TreeItemModel("Target Entity"));
            TreeItem<TreeItemModel> depth = new TreeItem<>(new TreeItemModel("Depth"));
            TreeItem<TreeItemModel> actionsAmount = new TreeItem<>(new TreeItemModel("Actions amount"));

            sourceEntity.getChildren().add(new TreeItem<>(new TreeItemModel(proximityModel.getSourceEntity())));
            targetEntity.getChildren().add(new TreeItem<>(new TreeItemModel(proximityModel.getTargetEntity())));
            depth.getChildren().add(new TreeItem<>(new TreeItemModel(proximityModel.getDepth())));
            actionsAmount.getChildren().add(new TreeItem<>(new TreeItemModel(String.valueOf(proximityModel.getActionsAmount()))));

            actionTreeItem.getChildren().addAll(Arrays.asList(sourceEntity, targetEntity, depth, actionsAmount));
        }

        else if (actionModel instanceof ReplaceModel) {
            ReplaceModel replaceModel = (ReplaceModel) actionModel;

            TreeItem<TreeItemModel> kill = new TreeItem<>(new TreeItemModel("Kill"));
            TreeItem<TreeItemModel> create = new TreeItem<>(new TreeItemModel("Create"));
            TreeItem<TreeItemModel> mode = new TreeItem<>(new TreeItemModel("Mode"));

            kill.getChildren().add(new TreeItem<>(new TreeItemModel(replaceModel.getKill())));
            create.getChildren().add(new TreeItem<>(new TreeItemModel(replaceModel.getCreate())));
            mode.getChildren().add(new TreeItem<>(new TreeItemModel(replaceModel.getMode())));

            actionTreeItem.getChildren().addAll(Arrays.asList(kill, create, mode));
        }

        else if (actionModel instanceof SetModel) {
            SetModel setModel = (SetModel) actionModel;

            TreeItem<TreeItemModel> property = new TreeItem<>(new TreeItemModel("Property"));
            TreeItem<TreeItemModel> value = new TreeItem<>(new TreeItemModel("Value"));

            property.getChildren().add(new TreeItem<>(new TreeItemModel(setModel.getPropertyName())));
            value.getChildren().add(new TreeItem<>(new TreeItemModel(setModel.getValue())));

            actionTreeItem.getChildren().addAll(Arrays.asList(property, value));
        }

        else if (actionModel instanceof SingleConditionModel) {
            SingleConditionModel singleConditionModel = (SingleConditionModel) actionModel;

            TreeItem<TreeItemModel> property = new TreeItem<>(new TreeItemModel("Property"));
            TreeItem<TreeItemModel> value = new TreeItem<>(new TreeItemModel("Value"));
            TreeItem<TreeItemModel> operator = new TreeItem<>(new TreeItemModel("Operator"));
            TreeItem<TreeItemModel> thenActionsAmount = new TreeItem<>(new TreeItemModel("Then actions amount"));
            TreeItem<TreeItemModel> elseActionsAmount = new TreeItem<>(new TreeItemModel("Else actions amount"));

            property.getChildren().add(new TreeItem<>(new TreeItemModel(singleConditionModel.getProperty())));
            value.getChildren().add(new TreeItem<>(new TreeItemModel(singleConditionModel.getValue())));
            operator.getChildren().add(new TreeItem<>(new TreeItemModel(singleConditionModel.getOperator())));
            thenActionsAmount.getChildren().add(new TreeItem<>(new TreeItemModel(String.valueOf(singleConditionModel.getThenActionsAmount()))));
            elseActionsAmount.getChildren().add(new TreeItem<>(new TreeItemModel(String.valueOf(singleConditionModel.getElseActionsAmount()))));

            actionTreeItem.getChildren().addAll(Arrays.asList(property, operator, value, thenActionsAmount, elseActionsAmount));
        }

        else if (actionModel instanceof MultipleConditionModel) {
            MultipleConditionModel multipleConditionModel = (MultipleConditionModel) actionModel;

            TreeItem<TreeItemModel> thenActionsAmount = new TreeItem<>(new TreeItemModel("Then actions amount"));
            TreeItem<TreeItemModel> elseActionsAmount = new TreeItem<>(new TreeItemModel("Else actions amount"));
            TreeItem<TreeItemModel> logicalOperator = new TreeItem<>(new TreeItemModel("Logical operator"));
            TreeItem<TreeItemModel> conditionsAmount = new TreeItem<>(new TreeItemModel("Conditions amount"));

            thenActionsAmount.getChildren().add(new TreeItem<>(new TreeItemModel(String.valueOf(multipleConditionModel.getThenActionsAmount()))));
            elseActionsAmount.getChildren().add(new TreeItem<>(new TreeItemModel(String.valueOf(multipleConditionModel.getElseActionsAmount()))));
            logicalOperator.getChildren().add(new TreeItem<>(new TreeItemModel(multipleConditionModel.getLogicalOperator())));
            conditionsAmount.getChildren().add(new TreeItem<>(new TreeItemModel(String.valueOf(multipleConditionModel.getConditionsAmount()))));

            actionTreeItem.getChildren().addAll(Arrays.asList(logicalOperator, conditionsAmount, thenActionsAmount, elseActionsAmount));
        }
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
    //#endregion

    //#region World Categories
    private void handleShowCategoriesData() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        worldCategoriesTreeView.setRoot(new TreeItem<>(new TreeItemModel(StringUtils.capitalize(WorldTreeViewCategories.WORLD.name().toLowerCase()))));

        WorldDTO world = objectMapper.readValue(engineAPI.getSimulationDetails().getData(),
                SingleSimulationDTO.class).getWorld();

        showEnvironment(world);
        showEntities(world);
        showGrid(world);
        showTermination(world);
        showRules(world);
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
                            .map(GuiUtils::createActionModel).collect(Collectors.toList());
                    return new RuleModel(rule.getName(), rule.getTicks(), rule.getProbability(), actions);
                })
                .collect(Collectors.toList());

        TreeItem<TreeItemModel> rulesTreeItem = new TreeItem<>(new RulesModel(rules));

        rules.forEach(rule -> {
            TreeItem<TreeItemModel> ruleTreeItem = new TreeItem<>(rule);
            rulesTreeItem.getChildren().add(ruleTreeItem);
        });

        worldCategoriesTreeView.getRoot().getChildren().add(rulesTreeItem);

    }
    //#endregion

}
