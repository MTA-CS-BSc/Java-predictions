package ui.handlers;


import ui.consts.Constants;
import ui.enums.MainMenu;
import ui.logs.UILoggers;
import ui.modules.Utils;
import ui.printers.MainMenuPrinter;
import ui.scanners.MainMenuScanner;

public class MainMenuHandler extends MainMenuScanner {
    public MainMenuHandler() {
        super();
    }
    public int selectionCycle() {
        MainMenuPrinter.print();
        int selected = Utils.scanOption(scanner, maxOption);

        while (selected == Constants.NOT_FOUND) {
            UILoggers.OrchestratorLogger.info("Invalid menu option option selected");
            MainMenuPrinter.print();
            selected = Utils.scanOption(scanner, maxOption);
        }

        return selected;
    }
}
