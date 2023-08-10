package ui.handlers;

import engine.history.HistoryManager;
import engine.logs.EngineLoggers;
import engine.parsers.XmlParser;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.PRDWorld;
import engine.simulation.SingleSimulation;
import engine.validators.PRDWorldValidators;
import ui.enums.MainMenu;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class Orchestrator {
    protected HistoryManager historyManager;
    protected World world;
    protected XmlLoaderHandler xmlLoaderHandler;
    protected MainMenuHandler mainMenuHandler;

    public Orchestrator() {
        xmlLoaderHandler = new XmlLoaderHandler();
        mainMenuHandler = new MainMenuHandler();

        EngineLoggers.XML_ERRORS_LOGGER.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
    }

    private void handleLoadXmlFile() throws JAXBException, FileNotFoundException {
        String xmlPath = xmlLoaderHandler.fileInputCycle();
        PRDWorld prdWorld = XmlParser.parseWorldXml(xmlPath);

        if (PRDWorldValidators.validateWorld(prdWorld)) {
            world = new World(prdWorld);
            System.out.println("XML file was loaded successfully.");
        }
    }
    private void fireOptionSelection(int selectedOption) throws JAXBException, FileNotFoundException {
        selectedOption--;

        if (selectedOption == MainMenu.LOAD_XML_FILE.ordinal())
            handleLoadXmlFile();
    }

    public void start() throws JAXBException, FileNotFoundException {
        int selectedOption = mainMenuHandler.selectionCycle();

        while (selectedOption != MainMenu.EXIT.ordinal() + 1) {
            fireOptionSelection(selectedOption);
            selectedOption = mainMenuHandler.selectionCycle();
        }
    }
}
