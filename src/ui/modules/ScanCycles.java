package ui.modules;

import helpers.TypesUtils;
import ui.consts.Constants;
import ui.logs.UILoggers;

import java.util.Scanner;

public class ScanCycles {
    public static String scanBoolean(Scanner scanner) {
        String selected = scanner.nextLine();

        while (!TypesUtils.isBoolean(selected)) {
            UILoggers.ScannerLogger.info("Option value entered is not a boolean");
            selected = scanner.nextLine();
        }

        return selected;
    }
    public static String scanFloat(Scanner scanner) {
        String selected = scanner.nextLine();

        while (!TypesUtils.isFloat(selected) && !TypesUtils.isDecimal(selected)) {
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

            if (!TypesUtils.isDecimal(selected)) {
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

        while (!TypesUtils.isDecimal(selected)) {
            UILoggers.ScannerLogger.info("Option value entered is not a decimal");
            selected = scanner.nextLine();
        }

        return selected;
    }
}
