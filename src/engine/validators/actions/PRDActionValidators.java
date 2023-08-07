package engine.validators.actions;

import engine.consts.ActionTypes;
import engine.consts.ConditionSingularities;
import engine.consts.PropTypes;
import engine.exceptions.EntityNotFoundException;
import engine.exceptions.InvalidTypeException;
import engine.exceptions.PRDThenNotFoundException;
import engine.exceptions.PropertyNotFoundException;
import engine.logs.Loggers;
import engine.modules.*;
import engine.parsers.ExpressionParser;
import engine.parsers.ValidationExpressionParser;
import engine.prototypes.jaxb.*;
import org.omg.CORBA.DynAnyPackage.Invalid;

import java.util.List;
import java.util.Objects;

public class PRDActionValidators {
    public static boolean validateAction(PRDWorld world, PRDAction action) throws EntityNotFoundException, PropertyNotFoundException, InvalidTypeException, PRDThenNotFoundException {
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
                return ConditionValidator.validate(world, action, action.getPRDCondition());
            case ActionTypes.KILL:
            case ActionTypes.REPLACE:
            case ActionTypes.PROXIMITY:
                return true;
        }

        return false;
    }
}
