package engine.validators;

import engine.prototypes.jaxb.*;

public class StringTrimmer {
    public static void trimAllStrings(PRDWorld world) {
        trimEntities(world);
        trimEnvironment(world);
        trimRules(world);
    }
    private static void trimEntities(PRDWorld world) {
        world.getPRDEntities().getPRDEntity().forEach(entity -> {
            entity.setName(entity.getName().trim());
            entity.getPRDProperties().getPRDProperty().forEach(property -> {
                property.setPRDName(property.getPRDName().trim());
                property.setType(property.getType().trim());
                property.getPRDValue().setInit(property.getPRDValue().getInit().trim());
            });
        });
    }
    private static void trimEnvironment(PRDWorld world) {
        world.getPRDEvironment().getPRDEnvProperty().forEach(property -> {
            property.setPRDName(property.getPRDName().trim());
            property.setType(property.getType().trim());
        });
    }
    private static void trimRules(PRDWorld world) {
        world.getPRDRules().getPRDRule().forEach(rule -> {
            rule.setName(rule.getName().trim());
            rule.getPRDActions().getPRDAction().forEach(StringTrimmer::trimAction);
        });
    }
    private static void trimAction(PRDAction action) {
        action.setEntity(action.getEntity().trim());
        action.setType(action.getType().trim());
        action.setBy(action.getBy().trim());
        action.setProperty(action.getProperty().trim());
        action.setResultProp(action.getResultProp().trim());
        action.setValue(action.getValue().trim());
        trimCondition(action.getPRDCondition());
        trimThen(action.getPRDThen());
        trimElse(action.getPRDElse());
    }
    private static void trimCondition(PRDCondition condition) {
        condition.setEntity(condition.getEntity().trim());
        condition.setProperty(condition.getProperty().trim());
        condition.setValue(condition.getValue().trim());
        condition.setLogical(condition.getLogical().trim());
        condition.setSingularity(condition.getSingularity().trim());
        condition.setOperator(condition.getOperator().trim());
    }
    private static void trimThen(PRDThen prdThen) {
        prdThen.getPRDAction().forEach(StringTrimmer::trimAction);
    }
    private static void trimElse(PRDElse prdElse) {
        prdElse.getPRDAction().forEach(StringTrimmer::trimAction);
    }
}
