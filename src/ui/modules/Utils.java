package ui.modules;

import ui.consts.Constants;
import ui.logs.UILoggers;

import java.util.Scanner;

public class Utils {
    public static int scanOption(Scanner scanner, int maxOption) {
        int selected;

        try {
            selected = scanner.nextInt();

            if (selected > maxOption || selected < 1) {
                UILoggers.ScannerLogger.info("Option value entered is not in range");
                return Constants.NOT_FOUND;
            }

            return selected;
        }

        catch (Exception e) {
            UILoggers.ScannerLogger.info("Option value entered is not a number");
            return Constants.NOT_FOUND;
        }
    }
}
