package ui.scanners;

import ui.consts.Constants;
import ui.enums.MainMenu;
import ui.logs.UILoggers;

import java.util.Scanner;

public abstract class MainMenuScanner {
    protected int maxOption;
    protected Scanner scanner;

    public MainMenuScanner() {
        maxOption = MainMenu.values().length;
        scanner = new Scanner(System.in);
    }
    protected int scanMenuOption() {
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
