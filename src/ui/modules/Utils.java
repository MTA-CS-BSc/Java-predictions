package ui.modules;

import engine.consts.BoolPropValues;
import ui.consts.Constants;
import ui.logs.UILoggers;

import java.util.Scanner;

public class Utils {
    public static String scanBoolean(Scanner scanner) {
        String selected = scanner.nextLine();

        while (!selected.equals(BoolPropValues.TRUE) && !selected.equals(BoolPropValues.FALSE)) {
            UILoggers.ScannerLogger.info("Option value entered is not a boolean");
            selected = scanner.nextLine();
        }

        return selected;
    }
    public static String scanFloat(Scanner scanner) {
        String selected = scanner.nextLine();

        while (!engine.modules.Utils.isFloat(selected) && !engine.modules.Utils.isDecimal(selected)) {
            UILoggers.ScannerLogger.info("Option value entered is not a float");
            selected = scanner.nextLine();
        }

        return selected;
    }
    public static int scanOption(Scanner scanner, int maxOption) {
        String selected;
        int choice;

        try {
            selected = scanner.nextLine();

            if (!engine.modules.Utils.isDecimal(selected)) {
                UILoggers.ScannerLogger.info("Option value entered is not a number");
                return Constants.NOT_FOUND;

            }

            else {
                choice = Integer.parseInt(selected);

                if (choice > maxOption || choice < 1) {
                    UILoggers.ScannerLogger.info("Option value entered is not in menu range");
                    return Constants.NOT_FOUND;
                }

                return choice;
            }
        }

        catch (Exception e) {
            UILoggers.ScannerLogger.info("Option value entered is not a number");
            return Constants.NOT_FOUND;
        }
    }
    public static String scanDecimal(Scanner scanner) {
        String selected = scanner.nextLine();

        while (!engine.modules.Utils.isDecimal(selected)) {
            UILoggers.ScannerLogger.info("Option value entered is not a decimal");
            selected = scanner.nextLine();
        }

        return selected;
    }
}
