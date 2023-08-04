package engine.modules;

import engine.prototypes.implemented.Entity;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.PRDAction;
import engine.validators.Utils;
import engine.validators.actions.PRDActionValidators;

import java.util.Objects;

public class ExpressionParser {
    public static Object parseExpression(World world, String entityName,
                                         PRDAction relatedAction, String expression) {
        Entity entity = (Entity)Utils.findEntityByName(world, entityName);

        if (Objects.isNull(entity))
            return "";

        if (PRDActionValidators.isSystemFunction(expression))
            return parseSystemFunctionExpression(expression);

        else if (entity.getProperties().getPropsMap().containsKey(expression))
            return entity.getProperties().getPropsMap().get(expression);

        return validateTypes(entity, relatedAction, expression);
    }
}
