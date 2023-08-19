package ui.handlers;

import engine.EngineAPI;
import engine.exceptions.UUIDNotFoundException;
import ui.enums.MainMenu;
import ui.logs.UILoggers;
import ui.printers.WorldDetailsPrinter;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.Objects;

public class Orchestrator {
    String currentSimulationUuid;
    protected EngineAPI api;
    protected MainMenuHandler mainMenuHandler;
    protected EnvPropsInitializerHandler environmentPropsInitializer;
    protected ShowPastSimulationHandler showPastSimulationHandler;
    protected FilePathsHandler filePathsHandler;

    public Orchestrator() {
        api = new EngineAPI();
        mainMenuHandler = new MainMenuHandler();
        environmentPropsInitializer = new EnvPropsInitializerHandler();
        filePathsHandler = new FilePathsHandler();
        showPastSimulationHandler = new ShowPastSimulationHandler(api);

        configureLoggers();
    }
    private void configureLoggers() {
        UILoggers.formatLogger(UILoggers.ScannerLogger);
        UILoggers.formatLogger(UILoggers.OrchestratorLogger);
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
        if (api.loadHistory(filePathsHandler.filePathToReadCycle(null)))
            System.out.println("History loaded successfully");

        else
            System.out.println("Error loading history!");
    }
    private void handleLoadXmlFile() throws JAXBException, FileNotFoundException {
        if (api.loadXml(filePathsHandler.filePathToReadCycle(".xml")))
            System.out.println("XML loaded sucessfully\n");

        else
            System.out.println("XML was not loaded. History unchanged.\n");
    }
    private void handleSaveWorldState() {
        if (!api.isXmlLoaded()) {
            System.out.println("Attempted to save world state but no XML file was loaded to the system");
            return;
        }

        else if (api.isHistoryEmpty()) {
            System.out.println("Attempted to save world state but no simulations were made");
            return;
        }

        if (!api.writeHistoryToFile(filePathsHandler.filePathToWriteCycle()))
            System.out.println("Error writing history file!");

        else
            System.out.println("History saved.");
    }
    private void handleRunSimulation() throws Exception {
        if (!api.isXmlLoaded()) {
            System.out.println("Attempted to run simulation but no xml was loaded to the system!");
            return;
        }

        currentSimulationUuid = api.createSimulation();
        environmentPropsInitializer.handlePropsSettings(api, currentSimulationUuid);

        UILoggers.OrchestratorLogger.info(String.format("Simulation [%s] is starting", currentSimulationUuid));

        api.runSimulation(currentSimulationUuid);

        System.out.printf("Simulation [%s] has ended", currentSimulationUuid);
        currentSimulationUuid = "";
    }
    private void handleShowSimulationDetails() {
        if (!api.isXmlLoaded()) {
            System.out.println("Attempted to show simulation details but no XML file was loaded to the system");
            return;
        }

        if (!Objects.isNull(api.getSimulationDetails()))
            WorldDetailsPrinter.print(api.getSimulationDetails());
    }
    private void handleShowPastSimulation() throws UUIDNotFoundException {
        if (!api.isXmlLoaded()) {
            System.out.println("Attempted to show past simulation but no XML file was loaded to the system");
            return;
        }

        else if (api.isHistoryEmpty()) {
            System.out.println("Attempted to show past simulation but no simulations were made");
            return;
        }

        showPastSimulationHandler.handle();
    }
}
