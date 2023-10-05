package parsers;

import prototypes.prd.generated.PRDWorld;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.InputStream;

public abstract class XmlParser {
    public static PRDWorld parseWorldXml(InputStream inputStream) throws JAXBException {
        return (PRDWorld) JAXBContext.newInstance(PRDWorld.class).createUnmarshaller().unmarshal(inputStream);
    }
}
