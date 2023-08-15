package ui.handlers;

import engine.EngineAPI;
import engine.logs.EngineLoggers;
import engine.parsers.XmlParser;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.PRDWorld;
import engine.validators.PRDWorldValidators;
import ui.enums.MainMenu;
import ui.logs.UILoggers;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.logging.ConsoleHandler;

public class Orchestrator {
    String currentSimulationUuid;
    protected XmlLoaderHandler xmlLoaderHandler;
    protected EngineAPI engineAPI;
    protected MainMenuHandler mainMenuHandler;
    protected EnvPropsInitializerHandler environmentPropsInitializer;

    public Orchestrator() {
        engineAPI = new EngineAPI();
        xmlLoaderHandler = new XmlLoaderHandler();
        mainMenuHandler = new MainMenuHandler();
        environmentPropsInitializer = new EnvPropsInitializerHandler();

        EngineLoggers.XML_ERRORS_LOGGER.addHandler(new ConsoleHandler());
        UILoggers.OrchestratorLogger.addHandler(new ConsoleHandler());
    }
    public void start() throws Exception {
        int selectedOption = mainMenuHandler.selectionCycle();

        while (selectedOption != MainMenu.EXIT.ordinal() + 1) {
            fireOptionSelection(selectedOption);
            selectedOption = mainMenuHandler.selectionCycle();
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
            System.out.println("Not implemented");
//            handleShowPastSimulation();
        else if (selectedOption == MainMenu.LOAD_WORLD_STATE.ordinal())
            handleLoadWorldState();
        else if (selectedOption == MainMenu.SAVE_WORLD_STATE.ordinal())
            handleSaveWorldState();
    }
    private void handleLoadWorldState() {
        if (!engineAPI.loadHistory())
            UILoggers.OrchestratorLogger.info("Error loading history.");

        else
            UILoggers.OrchestratorLogger.info("History was loaded");
    }
    private void handleLoadXmlFile() throws JAXBException, FileNotFoundException {
        String xmlPath = xmlLoaderHandler.fileInputCycle();
        PRDWorld prdWorld = XmlParser.parseWorldXml(xmlPath);

        if (PRDWorldValidators.validateWorld(prdWorld)) {
            engineAPI.setInitialXmlWorld(new World(prdWorld));
            currentSimulationUuid = engineAPI.createSimulation();
            UILoggers.OrchestratorLogger.info("XML loaded successfully.");
        }
    }
    private void handleSaveWorldState() {
        if (!engineAPI.isXmlLoaded()) {
            UILoggers.OrchestratorLogger.info("Attempted to save world state but no XML file was loaded to the system");
            return;
        }

        else if (engineAPI.isHistoryEmpty()) {
            UILoggers.OrchestratorLogger.info("Attempted to save world state but no simulations were made");
            return;
        }

        if (!engineAPI.writeHistoryToFile())
            System.out.println("Error writing history file!");
    }
    private void handleRunSimulation() throws Exception {
        if (!engineAPI.isXmlLoaded()) {
            UILoggers.OrchestratorLogger.info("Attempted to run simulation but no xml was loaded to the system!");
            return;
        }

        environmentPropsInitializer.handlePropsSettings(engineAPI, currentSimulationUuid);

        UILoggers.OrchestratorLogger.info(String.format("Simulation [%s] is starting", currentSimulationUuid));
        engineAPI.runSimulation(currentSimulationUuid);

        UILoggers.OrchestratorLogger.info(String.format("Simulation [%s] ended", currentSimulationUuid));
    }
    private void handleShowSimulationDetails() {
        if (!engineAPI.isXmlLoaded()) {
            UILoggers.OrchestratorLogger.info("Attempted to show simulation details but no XML file was loaded to the system");
            return;
        }

        System.out.println(engineAPI.getSimulationDetails(currentSimulationUuid));
    }
//    private void handleShowPastSimulation() {
//        if (!systemOrchestrator.isXmlLoaded()) {
//            UILoggers.OrchestratorLogger.info("Attempted to show past simulation but no XML file was loaded to the system");
//            return;
//        }
//
//        else if (systemOrchestrator.isHistoryEmpty()) {
//            UILoggers.OrchestratorLogger.info("Attempted to show past simulation but no simulations were made");
//            return;
//        }
//
//        //showPastSimulationHandler.handle();
//    }
}
