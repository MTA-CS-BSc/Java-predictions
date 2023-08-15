package engine.tests;

import engine.history.HistoryManager;
import engine.logs.EngineLoggers;
import helpers.XmlParser;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.PRDWorld;
import engine.simulation.SingleSimulation;
import engine.validators.PRDWorldValidators;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.util.logging.FileHandler;

public class SimulationTests {
    public SimulationTests() throws IOException {
        FileHandler fh = new FileHandler("/home/maya/Desktop/projects/MTA/Java/mta-java-predictions/src/engine/logs/simulation.log");
        EngineLoggers.SIMULATION_LOGGER.addHandler(fh);
    }

    @Test
    @DisplayName("Termination reason")
    public void testTerminationReasonOccurs() throws Exception {
        String xmlPath = String.format("%s/master-ex1.xml", XmlParserTests.testFilesPath);
        PRDWorld prdWorld = XmlParser.parseWorldXml(xmlPath);

        if (PRDWorldValidators.validateWorld(prdWorld)) {
            World world = new World(prdWorld);
            SingleSimulation simulation = new SingleSimulation(world);
            simulation.run();
        }
    }

    @Test
    @DisplayName("Simulation log world states")
    public void testSimulationLog() throws Exception {
        String xmlPath = String.format("%s/master-ex1.xml", XmlParserTests.testFilesPath);
        PRDWorld prdWorld = XmlParser.parseWorldXml(xmlPath);

        if (PRDWorldValidators.validateWorld(prdWorld)) {
            HistoryManager historyManager = new HistoryManager();
            World world = new World(prdWorld);
            SingleSimulation simulation = new SingleSimulation(world);
            simulation.run();
            historyManager.addPastSimulation(simulation);
        }
    }
}
