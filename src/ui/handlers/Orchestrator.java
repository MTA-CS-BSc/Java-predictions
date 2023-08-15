package ui.handlers;

import engine.EngineAPI;
import engine.exceptions.UUIDNotFoundException;
import engine.logs.EngineLoggers;
import ui.enums.MainMenu;
import ui.logs.UILoggers;
import ui.printers.WorldDetailsPrinter;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.logging.ConsoleHandler;

public class Orchestrator {
    String currentSimulationUuid;
    protected EngineAPI api;
    protected XmlLoaderHandler xmlLoaderHandler;
    protected MainMenuHandler mainMenuHandler;
    protected EnvPropsInitializerHandler environmentPropsInitializer;
    protected ShowPastSimulationHandler showPastSimulationHandler;

    public Orchestrator() {
        api = new EngineAPI();
        xmlLoaderHandler = new XmlLoaderHandler();
        mainMenuHandler = new MainMenuHandler();
        environmentPropsInitializer = new EnvPropsInitializerHandler();
        showPastSimulationHandler = new ShowPastSimulationHandler(api);

        EngineLoggers.XML_ERRORS_LOGGER.addHandler(new ConsoleHandler());
        UILoggers.OrchestratorLogger.addHandler(new ConsoleHandler());
    }
    public void start() throws Exception {
        int selectedOption = mainMenuHandler.selectionCycle();

        while (selectedOption != MainMenu.EXIT.ordinal() + 1) {
            fireOptionSelection(selectedOption - 1);
            selectedOption = mainMenuHandler.selectionCycle();
        }
    }
    private void fireOptionSelection(int selectedOption) throws Exception {
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
    private void handleLoadWorldState() {
        UILoggers.OrchestratorLogger.info(!api.loadHistory() ? "Error loading history" : "History was loaded");
    }
    private void handleLoadXmlFile() throws JAXBException, FileNotFoundException {
        UILoggers.OrchestratorLogger.info(api.loadXml(xmlLoaderHandler.fileInputCycle()) ?
                "XML File loaded successfully" : "XML was not loaded.");
    }
    private void handleSaveWorldState() {
        if (!api.isXmlLoaded()) {
            UILoggers.OrchestratorLogger.info("Attempted to save world state but no XML file was loaded to the system");
            return;
        }

        else if (api.isHistoryEmpty()) {
            UILoggers.OrchestratorLogger.info("Attempted to save world state but no simulations were made");
            return;
        }

        if (!api.writeHistoryToFile())
            System.out.println("Error writing history file!");
    }
    private void handleRunSimulation() throws Exception {
        if (!api.isXmlLoaded()) {
            UILoggers.OrchestratorLogger.info("Attempted to run simulation but no xml was loaded to the system!");
            return;
        }

        currentSimulationUuid = api.createSimulation();
        environmentPropsInitializer.handlePropsSettings(api, currentSimulationUuid);

        UILoggers.OrchestratorLogger.info(String.format("Simulation [%s] is starting", currentSimulationUuid));

        api.runSimulation(currentSimulationUuid);

        UILoggers.OrchestratorLogger.info(String.format("Simulation [%s] has ended", currentSimulationUuid));
        currentSimulationUuid = "";
    }
    private void handleShowSimulationDetails() {
        if (!api.isXmlLoaded()) {
            UILoggers.OrchestratorLogger.info("Attempted to show simulation details but no XML file was loaded to the system");
            return;
        }

        if (!Objects.isNull(api.getSimulationDetails()))
            WorldDetailsPrinter.print(api.getSimulationDetails());
    }
    private void handleShowPastSimulation() throws UUIDNotFoundException {
        if (!api.isXmlLoaded()) {
            UILoggers.OrchestratorLogger.info("Attempted to show past simulation but no XML file was loaded to the system");
            return;
        }

        else if (api.isHistoryEmpty()) {
            UILoggers.OrchestratorLogger.info("Attempted to show past simulation but no simulations were made");
            return;
        }

        showPastSimulationHandler.handle();
    }
}
