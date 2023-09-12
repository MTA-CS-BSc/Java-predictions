package engine.validators.actions;

import helpers.types.ActionTypes;
import engine.consts.SecondaryEntityCounts;
import engine.exceptions.EntityNotFoundException;
import engine.exceptions.ValueNotInRangeException;
import engine.modules.ValidatorsUtils;
import engine.prototypes.jaxb.PRDAction;
import engine.prototypes.jaxb.PRDWorld;
import helpers.types.TypesUtils;

import java.util.Objects;

public abstract class PRDActionValidators {
    public static boolean validateAction(PRDWorld world, PRDAction action) throws Exception {
        if (Objects.isNull(ValidatorsUtils.findPRDEntityByName(world, action.getEntity()))
                && !ActionTypes.ENTITY_MAY_NOT_EXIST_TYPES.contains(action.getType()))
            throw new EntityNotFoundException(String.format("Action [%s]: Entity [%s] does not exist",
                    action.getType(), action.getEntity()));

        if (!Objects.isNull(action.getPRDActions()))
            for (PRDAction act : action.getPRDActions().getPRDAction())
                if (!validateAction(world, act))
                    return false;

        if (!Objects.isNull(action.getPRDSecondaryEntity()))
            if (!validateSecondaryEntity(world, action))
                return false;

        switch (action.getType()) {
            case ActionTypes.SET:
                return SetValidator.validate(world, action);
            case ActionTypes.INCREASE:
            case ActionTypes.DECREASE:
                return IncreaseDecreaseValidator.validate(world, action);
            case ActionTypes.CALCULATION:
                return CalculationValidator.validate(world, action);
            case ActionTypes.CONDITION:
                return ConditionValidators.validate(world, action, action.getPRDCondition());
            case ActionTypes.REPLACE:
                return ReplaceValidators.validate(world, action);
            case ActionTypes.PROXIMITY:
                return ProximityValidators.validate(world, action);
            case ActionTypes.KILL:
                return true;
        }

        return false;
    }
    private static boolean validateSecondaryEntity(PRDWorld world, PRDAction action) throws Exception {
        if (Objects.isNull(ValidatorsUtils.findPRDEntityByName(world, action.getPRDSecondaryEntity().getEntity())))
            throw new EntityNotFoundException(String.format("Action [%s]: Secondary entity [%s] does not exist",
                    action.getType(), action.getPRDSecondaryEntity().getEntity()));

        String count = action.getPRDSecondaryEntity().getPRDSelection().getCount();

        if (!count.equals(SecondaryEntityCounts.ALL) && !TypesUtils.isDecimal(count))
            throw new ValueNotInRangeException(String.format("Action [%s]: Secondary entity [%s]: count is not valid",
                    action.getType(), action.getPRDSecondaryEntity().getEntity()));

        if (!ConditionValidators.validate(world, action, action.getPRDSecondaryEntity().getPRDSelection().getPRDCondition()))
            throw new Exception(String.format("Action [%s]: Secondary entity [%s]: Failed to validate condition",
                    action.getType(), action.getPRDSecondaryEntity().getEntity()));

        return true;
    }
}