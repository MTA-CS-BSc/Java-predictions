package engine.parsers;

import engine.prototypes.jaxb.PRDWorld;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;

public abstract class XmlParser {
    public static PRDWorld parseWorldXml(File file) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(PRDWorld.class);
        return (PRDWorld) context.createUnmarshaller()
                .unmarshal(file);
    }
}
