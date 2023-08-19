package ui;

import ui.handlers.Orchestrator;

public class Main {
    public static void main(String[] args) {
        Orchestrator orchestrator = new Orchestrator();

        try {
            orchestrator.start();
        }

        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
