package ui.scanners;

import engine.history.HistoryManager;

import java.util.Scanner;

public abstract class ShowPastSimulationScanner {
    protected Scanner scanner;
    protected HistoryManager historyManager;

    public ShowPastSimulationScanner(HistoryManager hm) {
        historyManager = hm;
        scanner = new Scanner(System.in);
    }
}
