package util;

import dto.DepartmentParseResult;
import model.Department;
import parser.ArgumentParser;
import reader.SbFileReader;
import statistics.Statistics;
import writer.DepartmentWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class DepartmentProcessor {

    public void run(String[] args) {
        ArgumentParser arguments = parseArguments(args);
        DepartmentParseResult result = readDepartments();

        if (result.departments().isEmpty() && result.errorLines().isEmpty()) {
            ConsoleLogger.info("No data to process.");
            return;
        }

        try {
            writeDepartments(result, arguments);
            generateStatistics(result.departments(), arguments);
        } catch (Exception e) {
            ConsoleLogger.error("Error in data processing: " + e.getMessage());
        }
    }

    private ArgumentParser parseArguments(String[] args) {
        return new ArgumentParser((args == null || args.length == 0) ? null : args);
    }

    private DepartmentParseResult readDepartments() {
        return new SbFileReader(Path.of(".")).readAndParseFiles();
    }

    private void writeDepartments(DepartmentParseResult result, ArgumentParser options) throws IOException {
        Path outputPath = "console".equalsIgnoreCase(String.valueOf(options.getOutputType()))
                ? Path.of(".")
                : Path.of(options.getOutputPath());

        new DepartmentWriter().writeDepartments(result, options, outputPath);
    }

    private void generateStatistics(Map<String, Department> departments, ArgumentParser options) {
        new Statistics(options).generate(departments);
    }
}
