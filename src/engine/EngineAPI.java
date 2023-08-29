package engine;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dtos.*;
import engine.consts.PropTypes;
import engine.exceptions.UUIDNotFoundException;
import engine.history.HistoryManager;
import engine.logs.EngineLoggers;
import engine.modules.Utils;
import engine.parsers.XmlParser;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.PRDWorld;
import engine.simulation.SingleSimulation;
import engine.validators.PRDWorldValidators;
import helpers.BoolPropValues;
import helpers.SimulationState;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class EngineAPI {
    protected int threadpoolCount;
    protected HistoryManager historyManager;
    public EngineAPI() {
        historyManager = new HistoryManager();

        configureLoggers();
    }
    private void configureLoggers() {
        EngineLoggers.SIMULATION_LOGGER.setLevel(Level.OFF);
        EngineLoggers.API_LOGGER.setLevel(Level.OFF);

        EngineLoggers.formatLogger(EngineLoggers.XML_ERRORS_LOGGER);
        EngineLoggers.formatLogger(EngineLoggers.SIMULATION_ERRORS_LOGGER);
    }
    public ResponseDTO isHistoryEmpty() {
        return new ResponseDTO(200,  historyManager.isEmpty() ? BoolPropValues.TRUE : BoolPropValues.FALSE);
    }
    public ResponseDTO writeHistoryToFile(String filePath) {
        try {
            FileOutputStream f = new FileOutputStream(filePath);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(historyManager);
            o.close();
            f.close();
            EngineLoggers.API_LOGGER.info("History saved successfully.");
            return new ResponseDTO(200, BoolPropValues.TRUE);
        } catch (Exception e) {
            EngineLoggers.API_LOGGER.info("Could not save history: " + e.getMessage());
            return new ResponseDTO(200, BoolPropValues.FALSE);
        }
    }
    public ResponseDTO loadHistory(String filePath) {
        try {
            FileInputStream fi = new FileInputStream(filePath);
            ObjectInputStream oi = new ObjectInputStream(fi);
            historyManager = (HistoryManager) oi.readObject();
            return new ResponseDTO(200);
        }

        catch (Exception e) {
            EngineLoggers.API_LOGGER.info(e.getMessage());
            return new ResponseDTO(500, "Attempted to load history but no history file was found");
        }
    }
    public ResponseDTO loadXml(String xmlPath) throws JAXBException, FileNotFoundException {
        PRDWorld prdWorld = XmlParser.parseWorldXml(xmlPath);
        ResponseDTO validateWorldResponse = PRDWorldValidators.validateWorld(prdWorld);

        if (Objects.isNull(validateWorldResponse.getErrorDescription())) {
            setInitialXmlWorld(new World(prdWorld));
            threadpoolCount = prdWorld.getPRDThreadCount();
        }

        return validateWorldResponse;
    }
    public ResponseDTO isXmlLoaded() {
        return new ResponseDTO(200, historyManager.isXmlLoaded() ? BoolPropValues.TRUE : BoolPropValues.FALSE);
    }
    public ResponseDTO createSimulation() {
        SingleSimulation sm = new SingleSimulation(getInitialWorldForSimulation());
        historyManager.addPastSimulation(sm);
        return new ResponseDTO(200, sm.getUUID());
    }
    public ResponseDTO getSimulationDetails() {
        return new ResponseDTO(200, historyManager.getMockSimulationForDetails());
    }
    public ResponseDTO setEnvironmentVariable(String uuid, PropertyDTO prop, String val) {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(400, "UUID", UUIDNotFoundException.class.getSimpleName());

        Property foundProp = Utils.findEnvironmentPropertyByName(historyManager.getPastSimulation(uuid).getWorld(), prop.getName());

        if (!Objects.isNull(foundProp)) {
            if (!Utils.validateValueInRange(prop, val))
                return new ResponseDTO(500, String.format("Environment variable [%s] was not set", prop.getName()), "Value not in range");

            foundProp.getValue().setRandomInitialize(false);

            if (PropTypes.NUMERIC_PROPS.contains(foundProp.getType()))
                val = Utils.removeExtraZeroes(val);

            foundProp.getValue().setInit(val);
            foundProp.getValue().setCurrentValue(foundProp.getValue().getInit());

            return new ResponseDTO(200, String.format("UUID [%s]: Set environment variable [%s]: Value: [%s]",
                    uuid, foundProp.getName(), foundProp.getValue().getCurrentValue()));
        }

        return new ResponseDTO(500, String.format("Environment variable [%s] was not set", prop.getName()),
                String.format("UUID [%s]: Environment variable [%s] not found", uuid, prop.getName()));
    }
    public ResponseDTO getEnvironmentProperties(String uuid) {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(500, Collections.emptyList(), String.format("UUID [%s] not found", uuid));

        List<PropertyDTO> data = historyManager.getPastSimulation(uuid).getWorld().getEnvironment()
                .getEnvVars().values()
                .stream()
                .map(Mappers::toDto)
                .sorted(Comparator.comparing(PropertyDTO::getName))
                .collect(Collectors.toList());

        return new ResponseDTO(200, data);
    }
    public ResponseDTO getPastSimulations() {
        if (isHistoryEmpty().getData().equals(BoolPropValues.TRUE))
            return new ResponseDTO(400, Collections.emptyList(), "History is empty!");

        List<SingleSimulationDTO> data = historyManager.getPastSimulations().values()
                .stream()
                .map(Mappers::toDto)
                .sorted(Comparator.comparing(SingleSimulationDTO::getStartTimestamp))
                .collect(Collectors.toList());

        return new ResponseDTO(200, data);
    }
    public ResponseDTO findSelectedSimulationDTO(int selection) {
        List<SingleSimulationDTO> pastSimulations = new Gson().fromJson(getPastSimulations().getData(), new TypeToken<List<SingleSimulationDTO>>(){}.getType());
        return new ResponseDTO(200, pastSimulations.get(selection - 1));
    }
    public ResponseDTO findSelectedEntityDTO(String uuid, int selection) {
        EntityDTO data = null;

        if (!Objects.isNull(findSimulationDTOByUuid(uuid))) {
            List<EntityDTO> entities = new Gson().fromJson(getEntities(uuid).getData(), new TypeToken<List<EntityDTO>>(){});
            data = entities.get(selection - 1);
        }

        return Objects.isNull(data) ?
                new ResponseDTO(500, null, "Unknown") : new ResponseDTO(200, data);
    }
    public ResponseDTO findSelectedPropertyDTO(EntityDTO entity, int selection) {
        if (Objects.isNull(entity))
            return new ResponseDTO(500, null, "Entity is null");

        return new ResponseDTO(200, entity.getProperties().get(selection - 1));
    }
    public ResponseDTO getEntities(String uuid) {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(400, Collections.emptyList(), String.format("UUID [%s] not found", uuid));

        List<EntityDTO> data = historyManager.getPastSimulation(uuid).getStartWorldState()
                .getEntitiesMap().values()
                .stream()
                .map(Mappers::toDto)
                .sorted(Comparator.comparing(EntityDTO::getName))
                .collect(Collectors.toList());

        return new ResponseDTO(200, data);
    }
    public ResponseDTO getEntitiesBeforeAndAfterSimulation(String uuid) throws UUIDNotFoundException {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(500, Collections.emptyMap(), String.format("UUID [%s] not found", uuid));

        return new ResponseDTO(200, historyManager.getEntitiesBeforeAndAfter(uuid));
    }
    public ResponseDTO getEntitiesCountForProp(String uuid, String entityName, String propertyName) throws UUIDNotFoundException {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(500, Collections.emptyMap(), String.format("UUID [%s] not found", uuid));

        return new ResponseDTO(200, historyManager.getEntitiesCountForProp(uuid, entityName, propertyName));
    }
    public ResponseDTO runSimulation(String uuid) throws Exception {
        if (!Objects.isNull(historyManager.getPastSimulation(uuid))) {
            historyManager.getPastSimulation(uuid).run();

            if (historyManager.getPastSimulation(uuid).getSimulationState() == SimulationState.ERROR) {
                historyManager.getPastSimulations().remove(uuid);
                return new ResponseDTO(500, String.format("Simulation [%s] was removed", uuid), "Simulation runtime error occurred.");
            }

            return new ResponseDTO(200, SimulationState.FINISHED);
        }

        return new ResponseDTO(500, String.format("Simulation [%s] was not executed", uuid), String.format("Simulation [%s] could not be found", uuid));
    }
    private void setInitialXmlWorld(World _initialWorld) {
        historyManager.setInitialXmlWorld(_initialWorld);
    }
    private World getInitialWorldForSimulation() {
        if (!historyManager.isEmpty())
            return historyManager.getLatestWorldObject();

        return historyManager.getInitialWorld();
    }
    private SingleSimulationDTO findSimulationDTOByUuid(String uuid) {
        List<SingleSimulationDTO> pastSimulations = new Gson().fromJson(getPastSimulations().getData(), new TypeToken<List<SingleSimulationDTO>>(){}.getType());

        return pastSimulations.stream().filter(element -> element.getUuid().equals(uuid))
                .findFirst().orElse(null);
    }
}
