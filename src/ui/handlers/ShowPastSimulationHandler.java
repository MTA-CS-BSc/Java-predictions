package ui.handlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dtos.EntityDTO;
import dtos.PropertyDTO;
import dtos.ResponseDTO;
import dtos.SingleSimulationDTO;
import engine.EngineAPI;
import engine.exceptions.UUIDNotFoundException;
import ui.consts.Constants;
import ui.enums.PastSimulationOutputOptions;
import ui.modules.ScanCycles;
import ui.printers.ShowPastSimulationPrinter;
import ui.scanners.ShowPastSimulationScanner;

import java.util.List;
import java.util.Objects;

public class ShowPastSimulationHandler extends ShowPastSimulationScanner {
    public ShowPastSimulationHandler(EngineAPI api) { super(api); }
    private void showSelectedSimulationDetails(String uuid) throws UUIDNotFoundException {
        ShowPastSimulationPrinter.printOutputOptions();
        int selected = ScanCycles.scanOption(scanner, Constants.STATISTICS_OPTIONS);

        while (selected == Constants.NOT_FOUND) {
            ShowPastSimulationPrinter.printOutputOptions();
            selected = ScanCycles.scanOption(scanner, Constants.STATISTICS_OPTIONS);
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
        int selection = ScanCycles.scanOption(scanner, propertiesAmount);

        while (selection == Constants.NOT_FOUND) {
            ShowPastSimulationPrinter.propHistogramPrintEntityProps(entity);
            selection = ScanCycles.scanOption(scanner, propertiesAmount);
        }

        return new Gson().fromJson(api.findSelectedPropertyDTO(entity, selection).getData(), PropertyDTO.class);
    }
    private EntityDTO getEntitySelection(String uuid) {
        int entitiesAmount = api.getEntities(uuid).size();

        ShowPastSimulationPrinter.propHistogramPrintEntities(api, uuid);
        int selection = ScanCycles.scanOption(scanner, entitiesAmount);

        while (selection == Constants.NOT_FOUND) {
            ShowPastSimulationPrinter.propHistogramPrintEntities(api, uuid);
            selection = ScanCycles.scanOption(scanner, entitiesAmount);
        }

        ResponseDTO findEntityResponse = api.findSelectedEntityDTO(uuid, selection);

        if (!Objects.isNull(findEntityResponse.getData()))
            return new Gson().fromJson(findEntityResponse.getData(), EntityDTO.class);

        return null;
    }

    public void handle() throws UUIDNotFoundException {
        System.out.println("Available past simulations: ");
        System.out.println("---------------------------------");

        ShowPastSimulationPrinter.printAvailableSimulations(api);
        List<SingleSimulationDTO> pastSimulations = new Gson().fromJson(api.getPastSimulations().getData(), new TypeToken<List<SingleSimulationDTO>>(){}.getType());
        int selected = ScanCycles.scanOption(scanner, pastSimulations.size());

        while (selected == Constants.NOT_FOUND) {
            ShowPastSimulationPrinter.printAvailableSimulations(api);
            selected = ScanCycles.scanOption(scanner, pastSimulations.size());
        }

        SingleSimulationDTO singleSimulation = new Gson().fromJson(api.findSelectedSimulationDTO(selected).getData(), SingleSimulationDTO.class);
        showSelectedSimulationDetails(singleSimulation.getUuid());
    }
}
