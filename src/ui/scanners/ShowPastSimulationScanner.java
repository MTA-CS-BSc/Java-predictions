package ui.scanners;

import engine.EngineAPI;

import java.util.Scanner;

public abstract class ShowPastSimulationScanner {
    protected Scanner scanner;
    protected EngineAPI api;
    public ShowPastSimulationScanner(EngineAPI api) {
        scanner = new Scanner(System.in);
        this.api = api;
    }
}
