package ui.handlers;

import ui.consts.Constants;
import ui.enums.MainMenu;
import ui.printers.MainMenuPrinter;
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
            System.out.println("File path entered is not an xml file");
            XmlLoaderPrinter.print();
            filePath = scanXmlFilePath();
        }

        return filePath;
    }
}
