package ui.handlers;

import engine.exceptions.UUIDNotFoundException;
import engine.history.HistoryManager;
import engine.simulation.SingleSimulation;
import ui.consts.Constants;
import ui.enums.PastSimulationOutputOptions;
import ui.logs.UILoggers;
import ui.modules.Utils;
import ui.printers.ShowPastSimulationPrinter;
import ui.scanners.ShowPastSimulationScanner;

import java.util.Comparator;
import java.util.stream.Collectors;

public class ShowPastSimulationHandler extends ShowPastSimulationScanner {

    public ShowPastSimulationHandler(HistoryManager historyManager) {
        super(historyManager);
    }
    private SingleSimulation findSelectedSimulation(int selection) {
        return historyManager.getPastSimulations().values()
                .stream()
                .sorted(Comparator.comparing(s -> s.getStartTime()))
                .collect(Collectors.toList()).get(selection - 1);
    }
    private void showSelectedSimulationDetails(SingleSimulation simulation) {
        ShowPastSimulationPrinter.printOutputOptions();
        int selected = Utils.scanOption(scanner, 2);

        while (selected == Constants.NOT_FOUND) {
            ShowPastSimulationPrinter.printOutputOptions();
            selected = Utils.scanOption(scanner, maxOption);
        }

        if (selected == PastSimulationOutputOptions.ENTITIES_AMOUNT_BEFORE_AND_AFTER_SIMULATION.ordinal() + 1) {
            try {
                ShowPastSimulationPrinter.printEntitiesBeforeAfter(historyManager, simulation.getUUID().toString());
            } catch (UUIDNotFoundException e) {
                UILoggers.OrchestratorLogger.info(e.getMessage());
            }
        }

        else if (selected == PastSimulationOutputOptions.PROPERTY_HISTOGRAM.ordinal() + 1)
            showPropertyHistogram(simulation);
    }

    private void showPropertyHistogram(SingleSimulation simulation) {
        //TODO: Not implemented
    }

    public void handle() {
        System.out.println("Available past simulations: ");
        System.out.println("---------------------------------");

        ShowPastSimulationPrinter.printAvailableSimulations(historyManager);
        int selected = Utils.scanOption(scanner, maxOption);

        while (selected == Constants.NOT_FOUND) {
            ShowPastSimulationPrinter.printAvailableSimulations(historyManager);
            selected = Utils.scanOption(scanner, maxOption);
        }

        showSelectedSimulationDetails(findSelectedSimulation(selected));
    }
}
