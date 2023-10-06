package fx.modules;

import actions.*;
import fx.component.mgmt.world.details.models.*;
import fx.component.mgmt.world.details.models.actions.*;
import javafx.animation.FadeTransition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import other.WorldDTO;

import java.util.List;
import java.util.stream.Collectors;

public abstract class GuiUtils {
    public static ActionModel createActionModel(ActionDTO action) {
        SecondaryEntityModel secondaryEntityModel = null;
        String entityName = action.getEntityName();

        if (action.isSecondaryEntityExists())
            secondaryEntityModel = new SecondaryEntityModel(action.getSecondaryEntity().getEntityName());

        if (action instanceof SetDTO) {
            SetDTO setAction = (SetDTO) action;
            return new SetModel(entityName, secondaryEntityModel,
                    setAction.getPropertyName(), setAction.getValue());
        } else if (action instanceof ReplaceDTO) {
            ReplaceDTO replaceAction = (ReplaceDTO) action;
            return new ReplaceModel(entityName, secondaryEntityModel,
                    replaceAction.getKill(), replaceAction.getCreate(), replaceAction.getMode());
        } else if (action instanceof CalculationDTO) {
            CalculationDTO calcAction = (CalculationDTO) action;
            return new CalculationModel(entityName, secondaryEntityModel,
                    calcAction.getOperationType(), calcAction.getArg1(), calcAction.getArg2());
        } else if (action instanceof KillDTO)
            return new KillModel(entityName, secondaryEntityModel);

        else if (action instanceof ProximityDTO) {
            ProximityDTO proximityAction = (ProximityDTO) action;
            return new ProximityModel(secondaryEntityModel, proximityAction.getSourceEntity(),
                    proximityAction.getTargetEntity(), proximityAction.getDepth(),
                    proximityAction.getActionsAmount());
        } else if (action instanceof IncreaseDecreaseDTO) {
            IncreaseDecreaseDTO increaseDecreaseAction = (IncreaseDecreaseDTO) action;
            return new IncreaseDecreaseModel(action.getType(), entityName,
                    secondaryEntityModel, increaseDecreaseAction.getPropertyName(), increaseDecreaseAction.getBy());
        } else if (action instanceof SingleConditionDTO) {
            SingleConditionDTO condition = (SingleConditionDTO) action;
            return new SingleConditionModel(entityName, secondaryEntityModel,
                    condition.getThenActionsAmount(), condition.getElseActionsAmount(),
                    condition.getOperator(), condition.getProperty(), condition.getValue());
        } else if (action instanceof MultipleConditionDTO) {
            MultipleConditionDTO condition = ((MultipleConditionDTO) action);
            return new MultipleConditionModel(entityName, secondaryEntityModel,
                    condition.getThenActionsAmount(), condition.getElseActionsAmount(),
                    condition.getLogicalOperator(), condition.getConditionsAmount());
        }

        return null;
    }

    public static void fadeInAnimation(Pane root) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1800), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        fadeIn.setOnFinished(event -> root.setVisible(true));
        fadeIn.play();
    }

    public static void fadeOutAnimation(Pane root) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(1200), root);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> root.setVisible(false));
        fadeOut.play();
    }

    public static List<EntityModel> getEntities(WorldDTO world) {
        return world.getEntities().stream()
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
    }

    public static List<PropertyModel> getEnvironmentVars(WorldDTO world) {
        return world.getEnvironment().stream()
                .map(property -> {
                    RangeModel range = null;

                    if (!property.hasNoRange())
                        range = new RangeModel(property.getRange().getFrom(), property.getRange().getTo());

                    return new PropertyModel(property.getName(), property.getType(), range);
                }).collect(Collectors.toList());
    }

    public static List<RuleModel> getRules(WorldDTO world) {
        return world.getRules().stream()
                .map(rule -> {
                    List<ActionModel> actions = rule.getActions().stream()
                            .map(GuiUtils::createActionModel).collect(Collectors.toList());
                    return new RuleModel(rule.getName(), rule.getTicks(), rule.getProbability(), actions);
                })
                .collect(Collectors.toList());
    }
}
