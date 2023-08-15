package ui.scanners;

import engine.EngineAPI;

import java.util.Scanner;

public abstract class ShowPastSimulationScanner {
    protected Scanner scanner;
    protected EngineAPI api;
    public ShowPastSimulationScanner(EngineAPI _api) {
        scanner = new Scanner(System.in);
        api = _api;
    }
}
