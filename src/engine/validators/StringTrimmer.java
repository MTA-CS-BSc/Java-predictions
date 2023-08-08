package engine.validators;

import engine.prototypes.jaxb.*;

import java.util.Objects;

public class StringTrimmer {
    public static void trimAllStrings(PRDWorld world) {
        trimEntities(world);
        trimEnvironment(world);
        trimRules(world);
    }
    private static void trimEntities(PRDWorld world) {
        if (Objects.isNull(world.getPRDEntities()))
            return;

        world.getPRDEntities().getPRDEntity().forEach(entity -> {
            if (!Objects.isNull(entity.getName()))
                entity.setName(entity.getName().trim());

            entity.getPRDProperties().getPRDProperty().forEach(property -> {
                if (!Objects.isNull(property.getPRDName()))
                    property.setPRDName(property.getPRDName().trim());

                if (!Objects.isNull(property.getType()))
                    property.setType(property.getType().trim());

                if (!property.getPRDValue().isRandomInitialize())
                    property.getPRDValue().setInit(property.getPRDValue().getInit().trim());
            });
        });
    }
    private static void trimEnvironment(PRDWorld world) {
        if (!Objects.isNull(world.getPRDEvironment()))
            world.getPRDEvironment().getPRDEnvProperty().forEach(property -> {
                if (!Objects.isNull(property.getPRDName()))
                    property.setPRDName(property.getPRDName().trim());

                if (!Objects.isNull(property.getType()))
                    property.setType(property.getType().trim());
            });
    }
    private static void trimRules(PRDWorld world) {
        if (!Objects.isNull(world.getPRDRules()))
            world.getPRDRules().getPRDRule().forEach(rule -> {
                if (!Objects.isNull(rule.getName()))
                    rule.setName(rule.getName().trim());

                rule.getPRDActions().getPRDAction().forEach(StringTrimmer::trimAction);
            });
    }
    private static void trimAction(PRDAction action) {
        if (Objects.isNull(action))
            return;

        if (!Objects.isNull(action.getEntity()))
            action.setEntity(action.getEntity().trim());

        if (!Objects.isNull(action.getType()))
            action.setType(action.getType().trim());

        if (!Objects.isNull(action.getBy()))
            action.setBy(action.getBy().trim());

        if (!Objects.isNull(action.getProperty()))
            action.setProperty(action.getProperty().trim());

        if (!Objects.isNull(action.getResultProp()))
            action.setResultProp(action.getResultProp().trim());

        if (!Objects.isNull(action.getValue()))
            action.setValue(action.getValue().trim());

        trimCondition(action.getPRDCondition());
        trimThen(action.getPRDThen());
        trimElse(action.getPRDElse());
    }
    private static void trimCondition(PRDCondition condition) {
        if (Objects.isNull(condition))
            return;

        if (!Objects.isNull(condition.getEntity()))
            condition.setEntity(condition.getEntity().trim());

        if (!Objects.isNull(condition.getProperty()))
            condition.setProperty(condition.getProperty().trim());

        if (!Objects.isNull(condition.getValue()))
            condition.setValue(condition.getValue().trim());

        if (!Objects.isNull(condition.getLogical()))
            condition.setLogical(condition.getLogical().trim());

        if (!Objects.isNull(condition.getSingularity()))
            condition.setSingularity(condition.getSingularity().trim());

        if (!Objects.isNull(condition.getOperator()))
            condition.setOperator(condition.getOperator().trim());
    }
    private static void trimThen(PRDThen prdThen) {
        if (!Objects.isNull(prdThen))
            prdThen.getPRDAction().forEach(StringTrimmer::trimAction);
    }
    private static void trimElse(PRDElse prdElse) {
        if (!Objects.isNull(prdElse))
            prdElse.getPRDAction().forEach(StringTrimmer::trimAction);
    }
}
