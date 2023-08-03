package engine.tests;

import engine.prototypes.jaxb.PRDWorld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import engine.parsers.XmlParser;

import java.util.Arrays;
import java.util.List;

public class XmlParserTests {
    private final String testFilesPath = "/home/maya/Desktop/projects/MTA/Java/mta-java-predictions/src/engine/tests/files";

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
}
