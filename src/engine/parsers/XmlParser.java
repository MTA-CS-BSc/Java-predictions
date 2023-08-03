package engine.parsers;

import engine.prototypes.jaxb.PRDWorld;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class XmlParser {
    public static PRDWorld parseWorldXml(String xmlPath) throws JAXBException, FileNotFoundException {
        JAXBContext context = JAXBContext.newInstance(PRDWorld.class);
        return (PRDWorld) context.createUnmarshaller()
                .unmarshal(new FileReader(xmlPath));
    }
}
