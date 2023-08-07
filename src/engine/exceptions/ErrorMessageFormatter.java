package engine.exceptions;

public class ErrorMessageFormatter {
    public static String formatActionErrorMessage(String actionName, String entityName,
                                                  String propertyName, String rest) {
        return String.format("Action [%s]: Entity [%s]: Property [%s]: [%s]", actionName, entityName, propertyName, rest);
    }

    public static String formatEntityNotFoundMessage(String actionName, String entityName) {
        return String.format("Action [%s]: Entity [%s] not found", actionName, entityName);
    }

    public static String formatPropertyNotFoundMessage(String actionName, String entityName, String propertyName) {
        return String.format("Action [%s]: Entity [%s]: Property [%s] not found", actionName, entityName, propertyName);
    }
}