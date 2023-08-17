package ui.handlers;

import ui.printers.XmlLoaderPrinter;
import ui.scanners.XmlLoaderScanner;

public class XmlLoaderHandler extends XmlLoaderScanner {
    public XmlLoaderHandler() {
        super();
    }
    public String fileInputCycle() {
        XmlLoaderPrinter.print();
        String filePath = scanXmlFilePath();

        while (filePath.isEmpty()) {
            System.out.println("File path entered is not a valid xml file");
            XmlLoaderPrinter.print();
            filePath = scanXmlFilePath();
        }

        return filePath;
    }
}
