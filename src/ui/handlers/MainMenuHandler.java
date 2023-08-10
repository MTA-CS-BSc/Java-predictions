package ui.handlers;


import ui.consts.Constants;
import ui.enums.MainMenu;
import ui.printers.MainMenuPrinter;
import ui.scanners.MainMenuScanner;

public class MainMenuHandler extends MainMenuScanner {
    public MainMenuHandler() {
        super();
    }

    public int selectionCycle() {
        MainMenuPrinter.print();
        int selected = scanMenuOption();

        while (selected != MainMenu.EXIT.ordinal() + 1  && selected == Constants.NOT_FOUND) {
            System.out.println("Invalid choice! Please try again:");
            MainMenuPrinter.print();
            selected = scanMenuOption();
        }

        return selected;
    }
}
