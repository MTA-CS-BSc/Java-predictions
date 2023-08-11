package ui.handlers;

import engine.exceptions.UUIDNotFoundException;
import engine.history.HistoryManager;
import engine.logs.EngineLoggers;
import engine.parsers.XmlParser;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.PRDWorld;
import engine.simulation.SingleSimulation;
import engine.validators.PRDWorldValidators;
import ui.consts.Constants;
import ui.enums.MainMenu;
import ui.logs.UILoggers;
import ui.printers.WorldDetailsPrinter;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.ConsoleHandler;

public class Orchestrator {
    protected HistoryManager historyManager;
    protected World world;
    protected XmlLoaderHandler xmlLoaderHandler;
    protected MainMenuHandler mainMenuHandler;
    protected ShowPastSimulationHandler showPastSimulationHandler;
    protected EnvPropsInitializerHandler envPropsInitializerHandler;

    public Orchestrator() {
        historyManager = new HistoryManager();
        xmlLoaderHandler = new XmlLoaderHandler();
        mainMenuHandler = new MainMenuHandler();
        showPastSimulationHandler = new ShowPastSimulationHandler(historyManager);
        envPropsInitializerHandler = new EnvPropsInitializerHandler();

        EngineLoggers.XML_ERRORS_LOGGER.addHandler(new ConsoleHandler());
        UILoggers.OrchestratorLogger.addHandler(new ConsoleHandler());
    }
    private void handleRunSimulation() throws Exception {
        if (Objects.isNull(world)) {
            UILoggers.OrchestratorLogger.info("Attempted to run simulation but no xml was loaded to the system!");
            return;
        }

        SingleSimulation simulation = new SingleSimulation(world);

        envPropsInitializerHandler.handlePropsSettings(world);

        UILoggers.OrchestratorLogger.info("Starting simulation...");
        simulation.run();
        historyManager.addPastSimulation(simulation);

        UILoggers.OrchestratorLogger.info(String.format("Simulation [%s] ended", simulation.getUUID()));
    }
    private void handleLoadXmlFile() throws JAXBException, FileNotFoundException {
        String xmlPath = xmlLoaderHandler.fileInputCycle();
        PRDWorld prdWorld = XmlParser.parseWorldXml(xmlPath);

        if (PRDWorldValidators.validateWorld(prdWorld)) {
            world = new World(prdWorld);

            try {
                Files.delete(Paths.get(Constants.HISTORY_FILE_PATH));
                UILoggers.OrchestratorLogger.info("Old history deleted.");
            } catch (Exception ignored) { }

            UILoggers.OrchestratorLogger.info("XML file was loaded successfully.");
        }
    }
    private void handleShowPastSimulation() throws UUIDNotFoundException {
        if (Objects.isNull(world)) {
            UILoggers.OrchestratorLogger.info("Attempted to show past simulation but no XML file was loaded to the system");
            return;
        }

        else if (historyManager.isEmpty()) {
            UILoggers.OrchestratorLogger.info("Attempted to show past simulation but no simulations were made");
            return;
        }

        showPastSimulationHandler.handle();
    }
    private void handleShowSimulationDetails() {
        if (Objects.isNull(world)) {
            UILoggers.OrchestratorLogger.info("Attempted to show simulation details but no XML file was loaded to the system");
            return;
        }

        WorldDetailsPrinter.print(world);
    }
    private void handleLoadWorldState() {
        try {
            FileInputStream fi = new FileInputStream(Constants.HISTORY_FILE_PATH);
            ObjectInputStream oi = new ObjectInputStream(fi);
            historyManager = (HistoryManager) oi.readObject();
            world = historyManager.getLatestWorldObject();
            showPastSimulationHandler = new ShowPastSimulationHandler(historyManager);

            oi.close();
            fi.close();

            UILoggers.OrchestratorLogger.info("History was loaded successfully.");
        }

        catch (Exception e) {
            UILoggers.OrchestratorLogger.info("Attempted to load history but no history file was found");
        }
    }
    private void handleSaveWorldState() {
        if (Objects.isNull(world)) {
            UILoggers.OrchestratorLogger.info("Attempted to save world state but no XML file was loaded to the system");
            return;
        }

        else if (historyManager.isEmpty()) {
            UILoggers.OrchestratorLogger.info("Attempted to save world state but no simulations were made");
            return;
        }

        writeHistoryToFile();
    }
    private void writeHistoryToFile() {
        try {
            FileOutputStream f = new FileOutputStream(Constants.HISTORY_FILE_PATH);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(historyManager);
            o.close();
            f.close();

            UILoggers.OrchestratorLogger.info("History saved successfully.");
        } catch (Exception e) {
            UILoggers.OrchestratorLogger.info("Could not save history: " + e.getMessage());
        }

    }
    private void fireOptionSelection(int selectedOption) throws Exception {
        selectedOption--;

        if (selectedOption == MainMenu.LOAD_XML_FILE.ordinal())
            handleLoadXmlFile();
        else if (selectedOption == MainMenu.RUN_SIMULATION.ordinal())
            handleRunSimulation();
        else if (selectedOption == MainMenu.SHOW_SIMULATION_DETAILS.ordinal())
            handleShowSimulationDetails();
        else if (selectedOption == MainMenu.SHOW_PAST_SIMULATION.ordinal())
            handleShowPastSimulation();
        else if (selectedOption == MainMenu.LOAD_WORLD_STATE.ordinal())
            handleLoadWorldState();
        else if (selectedOption == MainMenu.SAVE_WORLD_STATE.ordinal())
            handleSaveWorldState();
    }
    public void start() throws Exception {
        int selectedOption = mainMenuHandler.selectionCycle();

        while (selectedOption != MainMenu.EXIT.ordinal() + 1) {
            fireOptionSelection(selectedOption);
            selectedOption = mainMenuHandler.selectionCycle();
        }
    }
}
