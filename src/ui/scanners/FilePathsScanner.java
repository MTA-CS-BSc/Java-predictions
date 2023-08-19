package ui.scanners;

import ui.logs.UILoggers;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;

public class FilePathsScanner {
    protected Scanner scanner;

    public FilePathsScanner() {
        scanner = new Scanner(System.in);
    }
    public String scanFileName() {
        return scanner.nextLine().trim();
    }
    public String scanDirectoryToWrite() {
        String dir;

        try {
            dir = scanner.nextLine().trim();
            Path path = Paths.get(dir);

            if (!Files.exists(path))
                return "";

            return dir;
        }

        catch (Exception e) {
            UILoggers.ScannerLogger.info("Invalid directory!");
            return "";
        }
    }
    public String scanFilePathToRead(String extension) {
        String filePath;

        try {
            filePath = scanner.nextLine().trim();

            if (!Objects.isNull(extension))
                if (!filePath.endsWith(extension))
                    return "";

            try {
                new FileReader(filePath);
            }

            catch (Exception e) {
                UILoggers.ScannerLogger.info("value entered is not a valid file");
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
