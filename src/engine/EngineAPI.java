package engine;

import engine.consts.Restrictions;
import engine.exceptions.UUIDNotFoundException;
import engine.history.HistoryManager;
import engine.logs.EngineLoggers;
import engine.prototypes.EntityDTO;
import engine.prototypes.PropertyDTO;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.PRDWorld;
import engine.simulation.SingleSimulation;
import engine.simulation.SingleSimulationDTO;
import engine.parsers.XmlParser;
import engine.validators.PRDWorldValidators;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class EngineAPI {
    protected HistoryManager historyManager;
    public EngineAPI() {
        historyManager = new HistoryManager();
    }
    public boolean isHistoryEmpty() {
        return historyManager.isEmpty();
    }
    public boolean writeHistoryToFile() {
        try {
            FileOutputStream f = new FileOutputStream(Restrictions.HISTORY_FILE_PATH);
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
    public boolean loadHistory() {
        try {
            FileInputStream fi = new FileInputStream(Restrictions.HISTORY_FILE_PATH);
            ObjectInputStream oi = new ObjectInputStream(fi);
            historyManager = (HistoryManager) oi.readObject();
            EngineLoggers.API_LOGGER.info("History was loaded successfully");
            return true;
        }

        catch (Exception e) {
            EngineLoggers.API_LOGGER.info("Attempted to load history but no history file was found");
            return false;
        }
    }
    public boolean loadXml(String xmlPath) throws JAXBException, FileNotFoundException {
        PRDWorld prdWorld = XmlParser.parseWorldXml(xmlPath);

        if (PRDWorldValidators.validateWorld(prdWorld)) {
            setInitialXmlWorld(new World(prdWorld));
            return true;
        }

        return false;
    }
    public void setInitialXmlWorld(World _initialWorld) {
        historyManager.setInitialXmlWorld(_initialWorld);
    }
    public boolean isXmlLoaded() {
        return historyManager.isXmlLoaded();
    }
    private World getInitialWorldForSimulation() {
        if (!historyManager.isEmpty())
            return historyManager.getLatestWorldObject();

        return historyManager.getInitialWorld();
    }
    public String createSimulation() {
        SingleSimulation sm = new SingleSimulation(getInitialWorldForSimulation());
        historyManager.addPastSimulation(sm);
        return sm.getUUID();
    }
    public void runSimulation(String uuid) throws Exception {
        if (!Objects.isNull(historyManager.getPastSimulation(uuid)))
            historyManager.getPastSimulation(uuid).run();
    }
    public SingleSimulationDTO getSimulationDetails() {
        return historyManager.getMockSimulationForDetails();
    }
    public List<PropertyDTO> getEnvironmentProperties(String uuid) {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return Collections.emptyList();

        return historyManager.getPastSimulation(uuid).getWorld().getEnvironment()
                .getEnvVars().values()
                .stream()
                .map(PropertyDTO::new)
                .sorted(Comparator.comparing(PropertyDTO::getName))
                .collect(Collectors.toList());
    }
    public void setEnvironmentVariable(String uuid, PropertyDTO prop, String val) {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return;

        Property foundProp = historyManager.getPastSimulation(uuid).getWorld().getEnvironment()
                .getEnvVars().values().stream().filter(envProp -> envProp.getName().equals(prop.getName()))
                .findFirst().orElse(null);

        if (!Objects.isNull(foundProp)) {
            foundProp.getValue().setRandomInitialize(false);
            foundProp.getValue().setInit(val);
            foundProp.getValue().setCurrentValue(foundProp.getValue().getInit());
        }
    }
    public List<SingleSimulationDTO> getPastSimulations() {
        if (isHistoryEmpty())
            return Collections.emptyList();

        return historyManager.getPastSimulations().values()
                .stream()
                .map(SingleSimulationDTO::new)
                .sorted(Comparator.comparing(SingleSimulationDTO::getStartTimestamp))
                .collect(Collectors.toList());
    }
    public List<EntityDTO> getEntities(String uuid) {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return Collections.emptyList();

        return historyManager.getPastSimulation(uuid).getStartWorldState()
                .getEntitiesMap().values()
                .stream()
                .map(EntityDTO::new)
                .sorted(Comparator.comparing(EntityDTO::getName))
                .collect(Collectors.toList());
    }
    public Map<String, Integer[]> getEntitiesBeforeAndAfterSimulation(String uuid) throws UUIDNotFoundException {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return null;

        return historyManager.getEntitiesBeforeAndAfter(uuid);
    }
    public Map<String, Long> getEntitiesCountForProp(String uuid, String entityName, String propertyName) throws UUIDNotFoundException {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return null;

        return historyManager.getEntitiesCountForProp(uuid, entityName, propertyName);
    }
    public SingleSimulationDTO findSelectedSimulationDTO(int selection) {
        return getPastSimulations().get(selection - 1);
    }
    private SingleSimulationDTO findSimulationDTOByUuid(String uuid) {
        return getPastSimulations().stream().filter(element -> element.getUuid().equals(uuid))
                .findFirst().orElse(null);
    }
    public EntityDTO findSelectedEntityDTO(String uuid, int selection) {
        return !Objects.isNull(findSimulationDTOByUuid(uuid)) ? getEntities(uuid).get(selection - 1) : null;
    }
    public PropertyDTO findSelectedPropertyDTO(EntityDTO entity, int selection) {
        return entity.getProperties().get(selection - 1);
    }
}
