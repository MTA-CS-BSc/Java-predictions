package engine.validators.actions;

import engine.consts.ActionTypes;
import engine.exceptions.EntityNotFoundException;
import engine.modules.ValidatorsUtils;
import engine.prototypes.jaxb.PRDAction;
import engine.prototypes.jaxb.PRDWorld;

import java.util.Objects;

public class PRDActionValidators {
    public static boolean validateAction(PRDWorld world, PRDAction action) throws Exception {
        if (Objects.isNull(ValidatorsUtils.findPRDEntityByName(world, action.getEntity())))
            throw new EntityNotFoundException(String.format("Action [%s]: Entity [%s] does not exist",
                    action.getType(), action.getEntity()));

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
            case ActionTypes.KILL:
            case ActionTypes.REPLACE:
            case ActionTypes.PROXIMITY:
                return true;
        }

        return false;
    }
}