package ui.handlers;

import ui.printers.FilePathsPrinter;
import ui.scanners.FilePathsScanner;

import java.nio.file.Paths;

public class FilePathsHandler extends FilePathsScanner {
    public FilePathsHandler() {
        super();
    }
    public String filePathToWriteCycle() {
        FilePathsPrinter.printDirectory();
        String dir = scanDirectoryToWrite();
        String fileName;

        if (dir.isEmpty()) {
            System.out.println("Invalid directory!");
            return "";
        }

        FilePathsPrinter.printFileName();
        fileName = scanFileName();

        return Paths.get(dir, fileName).toString();
    }
    public String filePathToReadCycle(String extension) {
        FilePathsPrinter.printRead();
        String filePath = scanFilePathToRead(extension);

        if (filePath.isEmpty())
            System.out.println("Entered file path is not a valid file!");

        return filePath;
    }
}
