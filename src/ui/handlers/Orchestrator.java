package ui.handlers;

import engine.SystemOrchestrator;
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
    String currentSimulationUuid;
    protected XmlLoaderHandler xmlLoaderHandler;
    protected SystemOrchestrator systemOrchestrator;
    protected MainMenuHandler mainMenuHandler;
    protected EnvPropsInitializerHandler envPropsInitializerHandler;

    public Orchestrator() {
        systemOrchestrator = new SystemOrchestrator();
        xmlLoaderHandler = new XmlLoaderHandler();
        mainMenuHandler = new MainMenuHandler();
        envPropsInitializerHandler = new EnvPropsInitializerHandler();

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
            System.out.println("Not implemented");
//            handleShowSimulationDetails();
        else if (selectedOption == MainMenu.SHOW_PAST_SIMULATION.ordinal())
            System.out.println("Not implemented");
//            handleShowPastSimulation();
        else if (selectedOption == MainMenu.LOAD_WORLD_STATE.ordinal())
            handleLoadWorldState();
        else if (selectedOption == MainMenu.SAVE_WORLD_STATE.ordinal())
            handleSaveWorldState();
    }
    private void handleLoadWorldState() {
        if (!systemOrchestrator.loadHistory())
            UILoggers.OrchestratorLogger.info("Error loading history.");

        else
            UILoggers.OrchestratorLogger.info("History was loaded");
    }
    private void handleLoadXmlFile() throws JAXBException, FileNotFoundException {
        String xmlPath = xmlLoaderHandler.fileInputCycle();
        PRDWorld prdWorld = XmlParser.parseWorldXml(xmlPath);

        if (PRDWorldValidators.validateWorld(prdWorld))
            systemOrchestrator.setInitialXmlWorld(new World(prdWorld));
    }
    private void handleSaveWorldState() {
        if (!systemOrchestrator.isXmlLoaded()) {
            UILoggers.OrchestratorLogger.info("Attempted to save world state but no XML file was loaded to the system");
            return;
        }

        else if (systemOrchestrator.isHistoryEmpty()) {
            UILoggers.OrchestratorLogger.info("Attempted to save world state but no simulations were made");
            return;
        }

        if (!systemOrchestrator.writeHistoryToFile())
            System.out.println("Error writing history file!");
    }
    private void handleRunSimulation() throws Exception {
        if (!systemOrchestrator.isXmlLoaded()) {
            UILoggers.OrchestratorLogger.info("Attempted to run simulation but no xml was loaded to the system!");
            return;
        }

        //envPropsInitializerHandler.handlePropsSettings(world);
        currentSimulationUuid = systemOrchestrator.createSimulation();

        UILoggers.OrchestratorLogger.info(String.format("Simulation [%s] is starting", currentSimulationUuid));
        systemOrchestrator.runSimulation(currentSimulationUuid);

        UILoggers.OrchestratorLogger.info(String.format("Simulation [%s] ended", currentSimulationUuid));
    }

//    private void handleShowPastSimulation() throws UUIDNotFoundException {
//        if (Objects.isNull(world)) {
//            UILoggers.OrchestratorLogger.info("Attempted to show past simulation but no XML file was loaded to the system");
//            return;
//        }
//
//        else if (historyManager.isEmpty()) {
//            UILoggers.OrchestratorLogger.info("Attempted to show past simulation but no simulations were made");
//            return;
//        }
//
//        showPastSimulationHandler.handle();
//    }
//    private void handleShowSimulationDetails() {
//        if (Objects.isNull(world)) {
//            UILoggers.OrchestratorLogger.info("Attempted to show simulation details but no XML file was loaded to the system");
//            return;
//        }
//
//        WorldDetailsPrinter.print(world);
//    }
}
