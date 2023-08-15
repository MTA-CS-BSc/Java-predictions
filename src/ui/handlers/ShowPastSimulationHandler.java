package ui.handlers;

import engine.EngineAPI;
import engine.exceptions.UUIDNotFoundException;
import engine.simulation.SingleSimulationDTO;
import ui.consts.Constants;
import ui.enums.PastSimulationOutputOptions;
import ui.modules.Utils;
import ui.printers.ShowPastSimulationPrinter;
import ui.scanners.ShowPastSimulationScanner;

public class ShowPastSimulationHandler extends ShowPastSimulationScanner {
    public ShowPastSimulationHandler(EngineAPI api) { super(api); }

    private SingleSimulationDTO findSelectedSimulation(int selection) {
        return api.getPastSimulations().get(selection - 1);
    }
    private void showSelectedSimulationDetails(SingleSimulationDTO simulation) throws UUIDNotFoundException {
        ShowPastSimulationPrinter.printOutputOptions();
        int selected = Utils.scanOption(scanner, 2);

        while (selected == Constants.NOT_FOUND) {
            ShowPastSimulationPrinter.printOutputOptions();
            selected = Utils.scanOption(scanner, 2);
        }

        if (selected == PastSimulationOutputOptions.ENTITIES_AMOUNT_BEFORE_AND_AFTER_SIMULATION.ordinal() + 1)
            ShowPastSimulationPrinter.printEntitiesBeforeAfter(api, simulation.getUuid());

        else if (selected == PastSimulationOutputOptions.PROPERTY_HISTOGRAM.ordinal() + 1)
            System.out.println("Not implemented");
            //showPropertyHistogram(simulation);
    }
//    private void showPropertyHistogram(SingleSimulation simulation) throws UUIDNotFoundException {
//        Entity entity = getEntitySelection(simulation);
//        Property property = getPropertySelection(entity);
//
//        ShowPastSimulationPrinter.printPropHistogram(historyManager, simulation.getUUID(),
//                entity.getName(), property.getName());
//    }
//    private Property getPropertySelection(Entity entity) {
//        int propertiesAmount = entity.getInitialProperties().getPropsMap().size();
//
//        ShowPastSimulationPrinter.propHistogramPrintEntityProps(entity);
//        int selection = Utils.scanOption(scanner, propertiesAmount);
//
//        while (selection == Constants.NOT_FOUND) {
//            ShowPastSimulationPrinter.propHistogramPrintEntityProps(entity);
//            selection = Utils.scanOption(scanner, propertiesAmount);
//        }
//
//        return entity.getInitialProperties().getPropsMap()
//                .values()
//                .stream()
//                .sorted(Comparator.comparing(Property::getName))
//                .collect(Collectors.toList()).get(selection - 1);
//    }
//    private Entity getEntitySelection(SingleSimulation simulation) {
//        int entitiesAmount = simulation.getStartWorldState().getEntitiesMap().size();
//
//        ShowPastSimulationPrinter.propHistogramPrintEntities(simulation);
//        int selection = Utils.scanOption(scanner, entitiesAmount);
//
//        while (selection == Constants.NOT_FOUND) {
//            ShowPastSimulationPrinter.propHistogramPrintEntities(simulation);
//            selection = Utils.scanOption(scanner, entitiesAmount);
//        }
//
//        return simulation.getFinishWorldState().getEntitiesMap()
//                .values()
//                .stream()
//                .sorted(Comparator.comparing(Entity::getName))
//                .collect(Collectors.toList()).get(selection - 1);
//    }
    public void handle() throws UUIDNotFoundException {
        System.out.println("Available past simulations: ");
        System.out.println("---------------------------------");

        ShowPastSimulationPrinter.printAvailableSimulations(api);
        int selected = Utils.scanOption(scanner, api.getPastSimulations().size() + 1);

        while (selected == Constants.NOT_FOUND) {
            ShowPastSimulationPrinter.printAvailableSimulations(api);
            selected = Utils.scanOption(scanner, api.getPastSimulations().size() + 1);
        }

        showSelectedSimulationDetails(findSelectedSimulation(selected));
    }
}
