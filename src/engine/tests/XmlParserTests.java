package engine.tests;

import engine.parsers.XmlParser;
import engine.prototypes.jaxb.PRDWorld;
import engine.validators.PRDWorldValidators;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

public class XmlParserTests {
    private final String testFilesPath = "C:\\Users\\mayar\\OneDrive - The Academic College of Tel-Aviv Jaffa - MTA\\Desktop\\predictions\\src\\engine\\tests\\files";

    @Test
    @DisplayName("Parse valid XML")
    public void parseValidXmlTest() {
        String xmlPath = String.format("%s/master-ex1.xml", testFilesPath);

        Assertions.assertDoesNotThrow(() -> {
            XmlParser.parseWorldXml(xmlPath);
        });
    }

    @Test
    @DisplayName("Parse invalid XML")
    public void parseInvalidXmlTest() {
        List<String> xmlPaths = Arrays.asList(
                String.format("%s/ex1-error-2.xml", testFilesPath),
                String.format("%s/ex1-error-4.xml", testFilesPath),
                String.format("%s/ex1-error-6.xml", testFilesPath));

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
    @DisplayName("Validations check for valid XML")
    public void validationsForValidXml() throws JAXBException, FileNotFoundException {
        String xmlPath = String.format("%s/master-ex1.xml", testFilesPath);
        PRDWorld world = XmlParser.parseWorldXml(xmlPath);
        Assertions.assertTrue(PRDWorldValidators.validateWorld(world));
    }

    @Test
    @DisplayName("Validations check for invalid XML")
    public void validationsForInvalidXml() throws JAXBException, FileNotFoundException {
        List<String> xmlPaths = Arrays.asList(
                String.format("%s/ex1-error-2.xml", testFilesPath),
                String.format("%s/ex1-error-4.xml", testFilesPath),
                String.format("%s/ex1-error-6.xml", testFilesPath));

        PRDWorld world1 = XmlParser.parseWorldXml(xmlPaths.get(0));
        PRDWorld world2 = XmlParser.parseWorldXml(xmlPaths.get(1));
        PRDWorld world3 = XmlParser.parseWorldXml(xmlPaths.get(2));

        Assertions.assertAll(
                () -> Assertions.assertFalse(PRDWorldValidators.validateWorld(world1)),
                () -> Assertions.assertFalse(PRDWorldValidators.validateWorld(world2)),
                () -> Assertions.assertFalse(PRDWorldValidators.validateWorld(world3))
        );
    }
}
