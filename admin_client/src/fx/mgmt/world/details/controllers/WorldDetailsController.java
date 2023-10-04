package fx.mgmt.world.details.controllers;

import com.sun.xml.internal.ws.util.StringUtils;
import fx.mgmt.world.details.models.*;
import fx.mgmt.world.details.models.actions.*;
import fx.modules.GuiUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import other.WorldDTO;
import types.PropTypes;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class WorldDetailsController implements Initializable {
    @FXML private TreeView<TreeItemModel> worldCategoriesTreeView;
    @FXML private TreeView<TreeItemModel> selectedComponentDetailsTreeView;
    @FXML private GridPane container;

    private ObjectProperty<WorldDTO> selectedWorld;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedWorld = new SimpleObjectProperty<>();

        worldCategoriesTreeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (newValue.getValue() instanceof EntityModel)
                showEntityDetails((EntityModel) newValue.getValue());

            else if (newValue.getValue() instanceof PropertyModel)
                showEnvVarDetails((PropertyModel) newValue.getValue());

            else if (newValue.getValue() instanceof RuleModel)
                showRuleDetails((RuleModel) newValue.getValue());

            else
                selectedComponentDetailsTreeView.setRoot(null);
        });

        selectedWorld.addListener((observableValue, worldDTO, t1) -> {
            if (!Objects.isNull(t1)) {
                if (Objects.isNull(worldDTO) || !worldDTO.getName().equals(t1.getName()))
                    handleShowCategoriesData();
            }

            else
                worldCategoriesTreeView.setRoot(null);
        });
    }

    public void setSelectedWorldListener(ObjectProperty<WorldDTO> selectedWorldFromXmlController) {
        selectedWorldFromXmlController.addListener((observableValue, worldDTO, t1) -> {
            setSelectedWorld(t1);
        });
    }

    public void setSelectedWorld(WorldDTO selectedWorld) {
        this.selectedWorld.setValue(selectedWorld);
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
        } else if (actionModel instanceof CalculationModel) {
            CalculationModel calculationModel = (CalculationModel) actionModel;

            TreeItem<TreeItemModel> operationType = new TreeItem<>(new TreeItemModel("Operation type"));
            operationType.getChildren().add(new TreeItem<>(new TreeItemModel(calculationModel.getOperationType())));

            TreeItem<TreeItemModel> arg1 = new TreeItem<>(new TreeItemModel("Arg1"));
            arg1.getChildren().add(new TreeItem<>(new TreeItemModel(calculationModel.getArg1())));

            TreeItem<TreeItemModel> arg2 = new TreeItem<>(new TreeItemModel("Arg2"));
            arg2.getChildren().add(new TreeItem<>(new TreeItemModel(calculationModel.getArg2())));

            actionTreeItem.getChildren().addAll(Arrays.asList(operationType, arg1, arg2));
        } else if (actionModel instanceof ProximityModel) {
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
        } else if (actionModel instanceof ReplaceModel) {
            ReplaceModel replaceModel = (ReplaceModel) actionModel;

            TreeItem<TreeItemModel> kill = new TreeItem<>(new TreeItemModel("Kill"));
            TreeItem<TreeItemModel> create = new TreeItem<>(new TreeItemModel("Create"));
            TreeItem<TreeItemModel> mode = new TreeItem<>(new TreeItemModel("Mode"));

            kill.getChildren().add(new TreeItem<>(new TreeItemModel(replaceModel.getKill())));
            create.getChildren().add(new TreeItem<>(new TreeItemModel(replaceModel.getCreate())));
            mode.getChildren().add(new TreeItem<>(new TreeItemModel(replaceModel.getMode())));

            actionTreeItem.getChildren().addAll(Arrays.asList(kill, create, mode));
        } else if (actionModel instanceof SetModel) {
            SetModel setModel = (SetModel) actionModel;

            TreeItem<TreeItemModel> property = new TreeItem<>(new TreeItemModel("Property"));
            TreeItem<TreeItemModel> value = new TreeItem<>(new TreeItemModel("Value"));

            property.getChildren().add(new TreeItem<>(new TreeItemModel(setModel.getPropertyName())));
            value.getChildren().add(new TreeItem<>(new TreeItemModel(setModel.getValue())));

            actionTreeItem.getChildren().addAll(Arrays.asList(property, value));
        } else if (actionModel instanceof SingleConditionModel) {
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
        } else if (actionModel instanceof MultipleConditionModel) {
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

            range.getChildren().add(new TreeItem<>(envVar.getRange()));

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

                range.getChildren().add(new TreeItem<>(property.getRange()));

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
    public void handleShowCategoriesData() {
        if (Objects.isNull(selectedWorld.getValue()))
            return;

        worldCategoriesTreeView.setRoot(new TreeItem<>(new TreeItemModel(selectedWorld.getValue().getName())));
        showEnvironment();
        showEntities();
        showGrid();
        showRules();
    }

    private void showEntities() {
        //TODO: Add clear
        if (Objects.isNull(selectedWorld.getValue()))
            return;
        List<EntityModel> entities = GuiUtils.getEntities(selectedWorld.getValue());
        TreeItem<TreeItemModel> entitiesTreeItem = new TreeItem<>(new EntitiesModel(entities));

        entities.forEach(entity -> {
            TreeItem<TreeItemModel> entityTreeItem = new TreeItem<>(entity);
            entitiesTreeItem.getChildren().add(entityTreeItem);
        });

        worldCategoriesTreeView.getRoot().getChildren().add(entitiesTreeItem);
    }

    private void showEnvironment() {
        //TODO: Add clear
        if (Objects.isNull(selectedWorld.getValue()))
            return;

        List<PropertyModel> envVars = GuiUtils.getEnvironmentVars(selectedWorld.getValue());
        TreeItem<TreeItemModel> environmentTreeItem = new TreeItem<>(new EnvironmentModel(envVars));

        envVars.forEach(property -> {
            TreeItem<TreeItemModel> propertyTreeItem = new TreeItem<>(property);
            environmentTreeItem.getChildren().add(propertyTreeItem);
        });

        worldCategoriesTreeView.getRoot().getChildren().add(environmentTreeItem);
    }

    private void showGrid() {
        //TODO: Add clear
        if (Objects.isNull(selectedWorld.getValue()))
            return;

        TreeItem<TreeItemModel> rows = new TreeItem<>(new TreeItemModel("Rows"));
        rows.getChildren().add(new TreeItem<>(new TreeItemModel(String.valueOf(selectedWorld.getValue().getGridRows()))));

        TreeItem<TreeItemModel> cols = new TreeItem<>(new TreeItemModel("Columns"));
        cols.getChildren().add(new TreeItem<>(new TreeItemModel(String.valueOf(selectedWorld.getValue().getGridColumns()))));

        TreeItem<TreeItemModel> gridTreeItem = new TreeItem<>(new TreeItemModel(StringUtils.capitalize(WorldTreeViewCategories.GRID.name().toLowerCase())));
        gridTreeItem.getChildren().add(rows);
        gridTreeItem.getChildren().add(cols);

        worldCategoriesTreeView.getRoot().getChildren().add(gridTreeItem);
    }

    private void showRules() {
        //TODO: Add clear
        if (Objects.isNull(selectedWorld.getValue()))
            return;

        List<RuleModel> rules = GuiUtils.getRules(selectedWorld.getValue());
        TreeItem<TreeItemModel> rulesTreeItem = new TreeItem<>(new RulesModel(rules));

        rules.forEach(rule -> {
            TreeItem<TreeItemModel> ruleTreeItem = new TreeItem<>(rule);
            rulesTreeItem.getChildren().add(ruleTreeItem);
        });

        worldCategoriesTreeView.getRoot().getChildren().add(rulesTreeItem);
    }

    public GridPane getContainer() {
        return container;
    }
    //#endregion
}
