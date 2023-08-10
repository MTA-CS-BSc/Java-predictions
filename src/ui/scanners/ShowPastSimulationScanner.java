package ui.scanners;

import engine.history.HistoryManager;

import java.util.Scanner;

public abstract class ShowPastSimulationScanner {
    protected int maxOption;
    protected Scanner scanner;
    protected HistoryManager historyManager;


    public ShowPastSimulationScanner(HistoryManager hm) {
        historyManager = hm;
        maxOption = hm.getPastSimulations().values().size();
        scanner = new Scanner(System.in);
    }
}
