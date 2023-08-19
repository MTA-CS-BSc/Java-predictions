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

        while (dir.isEmpty()) {
            System.out.println("Invalid directory!");
            FilePathsPrinter.printDirectory();
            dir = scanDirectoryToWrite();
        }

        FilePathsPrinter.printFileName();
        fileName = scanFileName();

        return Paths.get(dir, fileName).toString();
    }
    public String filePathToReadCycle(String extension) {
        FilePathsPrinter.printRead();
        String filePath = scanFilePathToRead(extension);

        while (filePath.isEmpty()) {
            System.out.println("File path entered is not a valid file");
            FilePathsPrinter.printRead();
            filePath = scanFilePathToRead(extension);
        }

        return filePath;
    }
}
