package ui.handlers;


import ui.consts.Constants;
import ui.modules.ScanCycles;
import ui.printers.MainMenuPrinter;
import ui.scanners.MainMenuScanner;

public class MainMenuHandler extends MainMenuScanner {
    public MainMenuHandler() {
        super();
    }
    public int selectionCycle() {
        MainMenuPrinter.print();
        int selected = ScanCycles.scanOption(scanner, maxOption);

        while (selected == Constants.NOT_FOUND) {
            System.out.println("Invalid menu option option selected");
            MainMenuPrinter.print();
            selected = ScanCycles.scanOption(scanner, maxOption);
        }

        return selected;
    }
}
