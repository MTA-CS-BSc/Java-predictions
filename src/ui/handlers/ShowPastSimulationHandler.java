package ui.handlers;

import engine.exceptions.UUIDNotFoundException;
import engine.history.HistoryManager;
import engine.prototypes.implemented.Entity;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.World;
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
                .sorted(Comparator.comparing(s -> s.getStartTimestamp()))
                .collect(Collectors.toList()).get(selection - 1);
    }
    private void showSelectedSimulationDetails(SingleSimulation simulation) throws UUIDNotFoundException {
        ShowPastSimulationPrinter.printOutputOptions();
        int selected = Utils.scanOption(scanner, 2);

        while (selected == Constants.NOT_FOUND) {
            ShowPastSimulationPrinter.printOutputOptions();
            selected = Utils.scanOption(scanner, 2);
        }

        if (selected == PastSimulationOutputOptions.ENTITIES_AMOUNT_BEFORE_AND_AFTER_SIMULATION.ordinal() + 1) {
            try {
                ShowPastSimulationPrinter.printEntitiesBeforeAfter(historyManager, simulation.getUUID());
            } catch (UUIDNotFoundException e) {
                UILoggers.OrchestratorLogger.info(e.getMessage());
            }
        }

        else if (selected == PastSimulationOutputOptions.PROPERTY_HISTOGRAM.ordinal() + 1)
            showPropertyHistogram(simulation);
    }
    private void showPropertyHistogram(SingleSimulation simulation) throws UUIDNotFoundException {
        Entity entity = getEntitySelection(simulation);
        Property property = getPropertySelection(entity);

        ShowPastSimulationPrinter.printPropHistogram(historyManager, simulation.getUUID(),
                entity.getName(), property.getName());
    }
    private Property getPropertySelection(Entity entity) {
        int propertiesAmount = entity.getInitialProperties().getPropsMap().size();

        ShowPastSimulationPrinter.propHistogramPrintEntityProps(entity);
        int selection = Utils.scanOption(scanner, propertiesAmount);

        while (selection == Constants.NOT_FOUND) {
            ShowPastSimulationPrinter.propHistogramPrintEntityProps(entity);
            selection = Utils.scanOption(scanner, propertiesAmount);
        }

        return entity.getInitialProperties().getPropsMap()
                .values()
                .stream()
                .sorted(Comparator.comparing(Property::getName))
                .collect(Collectors.toList()).get(selection - 1);
    }
    private Entity getEntitySelection(SingleSimulation simulation) {
        int entitiesAmount = simulation.getStartWorldState().getEntitiesMap().size();

        ShowPastSimulationPrinter.propHistogramPrintEntities(simulation);
        int selection = Utils.scanOption(scanner, entitiesAmount);

        while (selection == Constants.NOT_FOUND) {
            ShowPastSimulationPrinter.propHistogramPrintEntities(simulation);
            selection = Utils.scanOption(scanner, entitiesAmount);
        }

        return simulation.getFinishWorldState().getEntitiesMap()
                .values()
                .stream()
                .sorted(Comparator.comparing(Entity::getName))
                .collect(Collectors.toList()).get(selection - 1);
    }
    public void handle() throws UUIDNotFoundException {
        System.out.println("Available past simulations: ");
        System.out.println("---------------------------------");

        ShowPastSimulationPrinter.printAvailableSimulations(historyManager);
        int selected = Utils.scanOption(scanner, historyManager.getPastSimulations().size());

        while (selected == Constants.NOT_FOUND) {
            ShowPastSimulationPrinter.printAvailableSimulations(historyManager);
            selected = Utils.scanOption(scanner, historyManager.getPastSimulations().size());
        }

        showSelectedSimulationDetails(findSelectedSimulation(selected));
    }
}
