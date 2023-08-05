package engine.tests;

import engine.logs.Loggers;
import engine.modules.TerminationReasons;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.PRDWorld;
import engine.simulation.SingleSimulation;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.FileHandler;

public class SimulationTests {
    XmlParserTests parserTests = new XmlParserTests();

    public SimulationTests() throws IOException {
        FileHandler fh = new FileHandler("/home/maya/Desktop/projects/MTA/Java/mta-java-predictions/src/engine/logs/simulation.log");
        Loggers.SIMULATION_LOGGER.addHandler(fh);
    }

    @Test
    @DisplayName("Termination reason")
    public void testTerminationReasonOccurs() throws JAXBException, FileNotFoundException {
        PRDWorld prdWorld = parserTests.validationsForValidXml();
        World world = new World(prdWorld);
        SingleSimulation simulation = new SingleSimulation(world);
        Assertions.assertEquals(simulation.run(), TerminationReasons.BY_TICKS);
    }
}
