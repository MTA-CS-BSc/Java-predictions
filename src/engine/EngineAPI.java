package engine;

import engine.consts.Restrictions;
import engine.exceptions.UUIDNotFoundException;
import engine.history.HistoryManager;
import engine.logs.EngineLoggers;
import engine.prototypes.PropertyDTO;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.World;
import engine.simulation.SingleSimulation;
import engine.simulation.SingleSimulationDTO;
import engine.simulation.SingleSimulationLog;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    public SingleSimulationDTO getSimulationDetails(String uuid) {
        if (!Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new SingleSimulationDTO(historyManager.getPastSimulation(uuid));

        EngineLoggers.API_LOGGER.info("Simulation with uuid " + uuid + " not found!");
        return null;
    }
    public List<PropertyDTO> getEnvironmentProperties(String uuid) {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return Collections.emptyList();

        return historyManager.getPastSimulation(uuid).getWorld().getEnvironment()
                .getEnvVars().values()
                .stream()
                .sorted(Comparator.comparing(Property::getName))
                .map(property -> new PropertyDTO(property.getName(), property.getType()))
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
                .sorted(Comparator.comparing(SingleSimulationLog::getStartTimestamp))
                .map(SingleSimulationDTO::new)
                .collect(Collectors.toList());
    }
    public Map<String, Integer[]> getEntitiesBeforeAndAfterSimulation(String uuid) throws UUIDNotFoundException {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return null;

        return historyManager.getEntitiesBeforeAndAfter(uuid);
    }
}
