package engine.validators.actions;

import engine.consts.ReplaceActionModes;
import engine.exceptions.EmptyExpressionException;
import engine.exceptions.EntityNotFoundException;
import engine.exceptions.ValueNotInRangeException;
import engine.modules.ValidatorsUtils;
import engine.prototypes.jaxb.PRDAction;
import engine.prototypes.jaxb.PRDWorld;

import java.util.Objects;

public abstract class ReplaceValidators {
    public static boolean validate(PRDWorld world, PRDAction action) throws Exception {
        String killEntity = action.getKill();
        String createEntity = action.getCreate();

        if (killEntity.isEmpty() || createEntity.isEmpty())
            throw new EmptyExpressionException(String.format("Action [%s]: Kill or create property is empty!",
                    action.getType()));

        else if (Objects.isNull(ValidatorsUtils.findPRDEntityByName(world, killEntity)))
            throw new EntityNotFoundException(String.format("Action [%s]: Kill entity [%s] does not exist!",
                    action.getType(), killEntity));

        else if (Objects.isNull(ValidatorsUtils.findPRDEntityByName(world, createEntity)))
            throw new EntityNotFoundException(String.format("Action [%s]: Create entity [%s] does not exist!",
                    action.getType(), createEntity));

        else if (!ReplaceActionModes.REPLACE_MODES.contains(action.getMode()))
            throw new ValueNotInRangeException(String.format("Action [%s]: Mode [%s] is not valid!",
                    action.getType(), action.getMode()));

        return true;
    }
}
