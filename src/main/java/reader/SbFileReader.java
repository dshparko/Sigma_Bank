package reader;

import dto.DepartmentParseResult;
import parser.LineParser;
import util.ConsoleLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.*;

public class SbFileReader {

    private final File root;
    private static final String EXTENSION = ".sb";
    private final LineParser parser = new LineParser();

    public SbFileReader(Path rootDirectory) {
        this.root = rootDirectory.toFile();
    }

    public DepartmentParseResult readAndParseFiles() {
        List<File> sbFiles = findSbFiles();
        List<String> allLines = readLinesFromFiles(sbFiles);

        allLines.forEach(parser::parseLine);
        parser.resolveEmployees();

        return new DepartmentParseResult(parser.getDepartments(), parser.getErrorLines());
    }

    private List<File> findSbFiles() {
        File[] files = root.listFiles((dir, name) -> name.toLowerCase().endsWith(EXTENSION));
        if (files == null || files.length == 0) {
            ConsoleLogger.error("Files with the extension " + EXTENSION + " were not found in directory: " + root.getAbsolutePath());
            return Collections.emptyList();
        }
        return Arrays.asList(files);
    }

    private List<String> readLinesFromFiles(List<File> files) {
        List<String> lines = new ArrayList<>();
        for (File file : files) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    lines.add(scanner.nextLine().trim());
                }
            } catch (FileNotFoundException e) {
                ConsoleLogger.error("File was not found: " + file.getAbsolutePath());
            }
        }
        return lines;
    }
}
