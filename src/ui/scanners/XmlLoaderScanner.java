package ui.scanners;

import ui.logs.UILoggers;

import java.io.FileReader;
import java.util.Scanner;

public abstract class XmlLoaderScanner {
    protected Scanner scanner;

    public XmlLoaderScanner() {
        scanner = new Scanner(System.in);
    }
    protected String scanXmlFilePath() {
        String filePath;

        try {
            filePath = scanner.nextLine().trim();

            if (!filePath.endsWith(".xml"))
                return "";

            try {
                new FileReader(filePath);
            }

            catch (Exception e) {
                UILoggers.ScannerLogger.info("value entered is not a valid xml file");
                return "";
            }

            return filePath;
        }

        catch (Exception e) {
            UILoggers.ScannerLogger.info("value entered is not a string");
            return "";
        }
    }
}
