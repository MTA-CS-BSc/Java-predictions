package engine;

import dtos.*;
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
    public boolean isHistoryEmpty() {
        return historyManager.isEmpty();
    }
    public boolean writeHistoryToFile(String filePath) {
        try {
            FileOutputStream f = new FileOutputStream(filePath);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(historyManager);
            o.close();
            f.close();
            EngineLoggers.API_LOGGER.info("History saved successfully.");
            return true;
        } catch (Exception e) {
            EngineLoggers.API_LOGGER.info("Could not save history: " + e.getMessage());
            return false;
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
    private void setInitialXmlWorld(World _initialWorld) {
        historyManager.setInitialXmlWorld(_initialWorld);
    }
    public ResponseDTO isXmlLoaded() {
        return new ResponseDTO(200, historyManager.isXmlLoaded() ? BoolPropValues.TRUE : BoolPropValues.FALSE);
    }
    private World getInitialWorldForSimulation() {
        if (!historyManager.isEmpty())
            return historyManager.getLatestWorldObject();

        return historyManager.getInitialWorld();
    }
    public ResponseDTO createSimulation() {
        SingleSimulation sm = new SingleSimulation(getInitialWorldForSimulation());
        historyManager.addPastSimulation(sm);
        return new ResponseDTO(200, sm.getUUID());
    }
    public void runSimulation(String uuid) throws Exception {
        if (!Objects.isNull(historyManager.getPastSimulation(uuid))) {
            historyManager.getPastSimulation(uuid).run();

            if (historyManager.getPastSimulation(uuid).getSimulationState() == SimulationState.ERROR)
                historyManager.getPastSimulations().remove(uuid);
        }
    }
    public SingleSimulationDTO getSimulationDetails() {
        return historyManager.getMockSimulationForDetails();
    }
    public ResponseDTO setEnvironmentVariable(String uuid, PropertyDTO prop, String val) {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(400, "UUID", UUIDNotFoundException.class.getSimpleName());

        Property foundProp = historyManager.getPastSimulation(uuid).getWorld().getEnvironment()
                .getEnvVars().values().stream().filter(envProp -> envProp.getName().equals(prop.getName()))
                .findFirst().orElse(null);

        if (!Objects.isNull(foundProp)) {
            if (!Utils.validateValueInRange(prop, val))
                return new ResponseDTO(500, String.format("Environment variable [%s] was not set", prop.getName()), "Value not in range");

            foundProp.getValue().setRandomInitialize(false);
            foundProp.getValue().setInit(Utils.removeExtraZeroes(foundProp, val));
            foundProp.getValue().setCurrentValue(foundProp.getValue().getInit());

            return new ResponseDTO(200, String.format("UUID [%s]: Set environment variable [%s]: Value: [%s]",
                    uuid, foundProp.getName(), foundProp.getValue().getCurrentValue()));
        }

        return new ResponseDTO(500, String.format("Environment variable [%s] was not set", prop.getName()),
                String.format("UUID [%s]: Environment variable [%s] not found", uuid, prop.getName()));
    }
    public List<PropertyDTO> getEnvironmentProperties(String uuid) {
        //TODO: Change response value to ResponseDTO
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return Collections.emptyList();

        return historyManager.getPastSimulation(uuid).getWorld().getEnvironment()
                .getEnvVars().values()
                .stream()
                .map(Mappers::toDto)
                .sorted(Comparator.comparing(PropertyDTO::getName))
                .collect(Collectors.toList());
    }
    public List<SingleSimulationDTO> getPastSimulations() {
        //TODO: Change response value to ResponseDTO

        if (isHistoryEmpty())
            return Collections.emptyList();

        return historyManager.getPastSimulations().values()
                .stream()
                .map(Mappers::toDto)
                .sorted(Comparator.comparing(SingleSimulationDTO::getStartTimestamp))
                .collect(Collectors.toList());
    }
    public List<EntityDTO> getEntities(String uuid) {
        //TODO: Change response value to ResponseDTO
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return Collections.emptyList();

        return historyManager.getPastSimulation(uuid).getStartWorldState()
                .getEntitiesMap().values()
                .stream()
                .map(Mappers::toDto)
                .sorted(Comparator.comparing(EntityDTO::getName))
                .collect(Collectors.toList());
    }
    public Map<String, Integer[]> getEntitiesBeforeAndAfterSimulation(String uuid) throws UUIDNotFoundException {
        //TODO: Change response value to ResponseDTO
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return null;

        return historyManager.getEntitiesBeforeAndAfter(uuid);
    }
    public Map<String, Long> getEntitiesCountForProp(String uuid, String entityName, String propertyName) throws UUIDNotFoundException {
        //TODO: Change response value to ResponseDTO
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return null;

        return historyManager.getEntitiesCountForProp(uuid, entityName, propertyName);
    }
    public SingleSimulationDTO findSelectedSimulationDTO(int selection) {
        //TODO: Change response value to ResponseDTO
        return getPastSimulations().get(selection - 1);
    }
    private SingleSimulationDTO findSimulationDTOByUuid(String uuid) {
        //TODO: Change response value to ResponseDTO
        return getPastSimulations().stream().filter(element -> element.getUuid().equals(uuid))
                .findFirst().orElse(null);
    }
    public EntityDTO findSelectedEntityDTO(String uuid, int selection) {
        //TODO: Change response value to ResponseDTO
        return !Objects.isNull(findSimulationDTOByUuid(uuid)) ? getEntities(uuid).get(selection - 1) : null;
    }
    public PropertyDTO findSelectedPropertyDTO(EntityDTO entity, int selection) {
        //TODO: Change response value to ResponseDTO
        return entity.getProperties().get(selection - 1);
    }
}
