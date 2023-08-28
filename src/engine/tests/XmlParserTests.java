package engine.tests;

import engine.logs.EngineLoggers;
import engine.parsers.XmlParser;
import engine.prototypes.jaxb.PRDWorld;
import engine.validators.PRDWorldValidators;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.FileHandler;

public class XmlParserTests {
//    public final static String testFilesPath = "/home/maya/Desktop/projects/MTA/Java/mta-java-predictions/src/engine/tests/files";
    public final static String testFilesPath = "C:\\Users\\mayar\\OneDrive - The Academic College of Tel-Aviv Jaffa - MTA\\Desktop\\predictions\\src\\engine\\tests\\files";
    public final static String ex1TestFilesPath = String.format("%s/ex1", testFilesPath);
    public final static String ex2TestFilesPath = String.format("%s/ex2", testFilesPath);

    public XmlParserTests() throws IOException {
//        FileHandler fh = new FileHandler("/home/maya/Desktop/projects/MTA/Java/mta-java-predictions/src/engine/logs/xml_parse_error.log");
//        EngineLoggers.XML_ERRORS_LOGGER.addHandler(fh);
    }

    @Test
    @DisplayName("Parse valid XML")
    public void parseValidXmlTest() {
        String xmlPath = String.format("%s/master-ex1.xml", ex1TestFilesPath);

        Assertions.assertDoesNotThrow(() -> {
            XmlParser.parseWorldXml(xmlPath);
        });
    }

    @Test
    @DisplayName("Parse invalid XML")
    public void parseInvalidXmlTest() {
        List<String> xmlPaths = Arrays.asList(
                String.format("%s/ex1-error-2.xml", ex1TestFilesPath),
                String.format("%s/ex1-error-4.xml", ex1TestFilesPath),
                String.format("%s/ex1-error-6.xml", ex1TestFilesPath));

        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> {
                    XmlParser.parseWorldXml(xmlPaths.get(0));
                }),
                () -> Assertions.assertDoesNotThrow(() -> {
                    XmlParser.parseWorldXml(xmlPaths.get(1));
                }),
                () -> Assertions.assertDoesNotThrow(() -> {
                    XmlParser.parseWorldXml(xmlPaths.get(2));
                })
        );
    }

    @Test
    @DisplayName("EX 1 Validations check for valid XML")
    public void ex1ValidationsForValidXml() throws JAXBException, FileNotFoundException {
        String xmlPath = String.format("%s/master-ex1.xml", ex1TestFilesPath);
        PRDWorld world = XmlParser.parseWorldXml(xmlPath);
        Assertions.assertTrue(PRDWorldValidators.validateWorld(world));
    }

    @Test
    @DisplayName("EX 1 Validations check for invalid XML")
    public void ex1ValidationsForInvalidXml() throws JAXBException, FileNotFoundException {
        List<String> xmlPaths = Arrays.asList(
                String.format("%s/err-calculation-args.xml", ex1TestFilesPath),
                String.format("%s/err-condition-args.xml", ex1TestFilesPath),
                String.format("%s/err-entity-not-found.xml", ex1TestFilesPath),
                String.format("%s/err-increase-args.xml", ex1TestFilesPath),
                String.format("%s/err-invalid-range.xml", ex1TestFilesPath),
                String.format("%s/err-negative-population.xml", ex1TestFilesPath),
                String.format("%s/err-property-not-found.xml", ex1TestFilesPath),
                String.format("%s/err-result-prop-not-found.xml", ex1TestFilesPath),
                String.format("%s/err-set-args.xml", ex1TestFilesPath),
                String.format("%s/err-unique-name.xml", ex1TestFilesPath));

        Assertions.assertAll(
                xmlPaths.stream().map(element -> ()
                        -> Assertions.assertFalse(PRDWorldValidators.validateWorld(XmlParser.parseWorldXml(element))))
        );
    }

    @Test
    @DisplayName("EX 2 Validations check for valid XML")
    public void ex2ValidationsForValidXml() throws JAXBException, FileNotFoundException {
        String xmlPath = String.format("%s\\master-ex2.xml", ex2TestFilesPath);
        PRDWorld world = XmlParser.parseWorldXml(xmlPath);
        Assertions.assertTrue(PRDWorldValidators.validateWorld(world));
    }

    @Test
    @DisplayName("EX 2 Validations check for invalid XML")
    public void ex2ValidationsForInvalidXml() throws JAXBException, FileNotFoundException {
        List<String> xmlPaths = Arrays.asList(
                String.format("%s/ex2-error-1.xml", ex2TestFilesPath),
                String.format("%s/ex2-error-3.xml", ex2TestFilesPath));

        Assertions.assertAll(
                xmlPaths.stream().map(element -> ()
                        -> Assertions.assertFalse(PRDWorldValidators.validateWorld(XmlParser.parseWorldXml(element))))
        );
    }

}
