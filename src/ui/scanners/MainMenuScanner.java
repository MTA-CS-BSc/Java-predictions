package ui.scanners;

import ui.enums.MainMenu;

import java.util.Scanner;

public abstract class MainMenuScanner {
    protected int maxOption;
    protected Scanner scanner;

    public MainMenuScanner() {
        maxOption = MainMenu.values().length;
        scanner = new Scanner(System.in);
    }
}
