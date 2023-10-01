package engine.parsers;

import engine.prototypes.jaxb.PRDWorld;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public abstract class XmlParser {
    public static PRDWorld parseWorldXml(InputStream inputStream) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(PRDWorld.class);
        File tempFile = getTempFile(inputStream);
        PRDWorld world = (PRDWorld) context.createUnmarshaller().unmarshal(tempFile);

        tempFile.delete();
        return world;

    }

    private static File getTempFile(InputStream inputStream) throws IOException {
        File tempFile = File.createTempFile("uploaded_", "_" + UUID.randomUUID());

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1)
                fos.write(buffer, 0, bytesRead);
        }

        return tempFile;
    }
}
