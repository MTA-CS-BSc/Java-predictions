package tests;

import history.HistoryManager;
import logs.EngineLoggers;
import parsers.XmlParser;
import prototypes.implemented.World;
import prototypes.jaxb.PRDWorld;
import simulation.SingleSimulation;
import validators.PRDWorldValidators;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
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
        PRDWorld prdWorld = XmlParser.parseWorldXml(Files.newInputStream(new File(xmlPath).toPath()));

        if (Objects.isNull(PRDWorldValidators.validateWorld(prdWorld).getErrorDescription())) {
            World world = new World(prdWorld);
            SingleSimulation simulation = new SingleSimulation(world);
            simulation.run();
        }
    }

    @Test
    @DisplayName("Simulation log world states")
    public void testSimulationLog() throws Exception {
        String xmlPath = String.format("%s/master-ex1.xml", XmlParserTests.testFilesPath);
        PRDWorld prdWorld = XmlParser.parseWorldXml(Files.newInputStream(new File(xmlPath).toPath()));

        if (Objects.isNull(PRDWorldValidators.validateWorld(prdWorld).getErrorDescription())) {
            HistoryManager historyManager = new HistoryManager();
            World world = new World(prdWorld);
            SingleSimulation simulation = new SingleSimulation(world);
            simulation.run();
            historyManager.addPastSimulation(simulation);
        }
    }
}
