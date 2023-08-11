package ui.modules;

import ui.consts.Constants;
import ui.logs.UILoggers;

import java.util.Scanner;

public class Utils {
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
}
