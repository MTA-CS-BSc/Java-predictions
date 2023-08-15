package ui.handlers;

import engine.EngineAPI;
import engine.exceptions.UUIDNotFoundException;
import engine.prototypes.EntityDTO;
import engine.prototypes.PropertyDTO;
import ui.consts.Constants;
import ui.enums.PastSimulationOutputOptions;
import ui.modules.Utils;
import ui.printers.ShowPastSimulationPrinter;
import ui.scanners.ShowPastSimulationScanner;

public class ShowPastSimulationHandler extends ShowPastSimulationScanner {
    public ShowPastSimulationHandler(EngineAPI api) { super(api); }
    private void showSelectedSimulationDetails(String uuid) throws UUIDNotFoundException {
        ShowPastSimulationPrinter.printOutputOptions();
        int selected = Utils.scanOption(scanner, 2);

        while (selected == Constants.NOT_FOUND) {
            ShowPastSimulationPrinter.printOutputOptions();
            selected = Utils.scanOption(scanner, 2);
        }

        if (selected == PastSimulationOutputOptions.ENTITIES_AMOUNT_BEFORE_AND_AFTER_SIMULATION.ordinal() + 1)
            ShowPastSimulationPrinter.printEntitiesBeforeAndAfterSimulation(api, uuid);

        else if (selected == PastSimulationOutputOptions.PROPERTY_HISTOGRAM.ordinal() + 1)
            showPropertyHistogram(uuid);
    }
    private void showPropertyHistogram(String uuid) throws UUIDNotFoundException {
        EntityDTO entity = getEntitySelection(uuid);
        PropertyDTO property = getPropertySelection(entity);

        ShowPastSimulationPrinter.printPropHistogram(api, uuid, entity.getName(), property.getName());
    }
    private PropertyDTO getPropertySelection(EntityDTO entity) {
        int propertiesAmount = entity.getProperties().size();

        ShowPastSimulationPrinter.propHistogramPrintEntityProps(entity);
        int selection = Utils.scanOption(scanner, propertiesAmount);

        while (selection == Constants.NOT_FOUND) {
            ShowPastSimulationPrinter.propHistogramPrintEntityProps(entity);
            selection = Utils.scanOption(scanner, propertiesAmount);
        }

        return api.findSelectedPropertyDTO(entity, selection);
    }
    private EntityDTO getEntitySelection(String uuid) {
        int entitiesAmount = api.getEntities(uuid).size();

        ShowPastSimulationPrinter.propHistogramPrintEntities(api, uuid);
        int selection = Utils.scanOption(scanner, entitiesAmount);

        while (selection == Constants.NOT_FOUND) {
            ShowPastSimulationPrinter.propHistogramPrintEntities(api, uuid);
            selection = Utils.scanOption(scanner, entitiesAmount);
        }

        return api.findSelectedEntityDTO(uuid, selection);
    }
    public void handle() throws UUIDNotFoundException {
        System.out.println("Available past simulations: ");
        System.out.println("---------------------------------");

        ShowPastSimulationPrinter.printAvailableSimulations(api);
        int selected = Utils.scanOption(scanner, api.getPastSimulations().size() + 1);

        while (selected == Constants.NOT_FOUND) {
            ShowPastSimulationPrinter.printAvailableSimulations(api);
            selected = Utils.scanOption(scanner, api.getPastSimulations().size() + 1);
        }

        showSelectedSimulationDetails(api.findSelectedSimulationDTO(selected).getUuid());
    }
}
