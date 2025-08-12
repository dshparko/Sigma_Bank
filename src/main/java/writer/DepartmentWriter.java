package writer;

import dto.DepartmentParseResult;
import model.Department;
import model.Employee;
import parser.ArgumentParser;
import util.EmployeeSorter;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DepartmentWriter {

    public void writeDepartments(DepartmentParseResult result,
                                 ArgumentParser options,
                                 Path outputDir) throws IOException {

        Path resolvedDir = prepareOutputDirectory(outputDir);

        for (Department department : result.departments().values()) {
            writeDepartmentFile(department, options, resolvedDir);
        }

        writeErrorLog(result.errorLines(), resolvedDir);
    }

    private Path prepareOutputDirectory(Path outputDir) throws IOException {
        Path resolved = outputDir.toAbsolutePath().getParent();
        if (resolved != null && !Files.exists(resolved)) {
            Files.createDirectories(resolved);
        }
        return resolved != null ? resolved : outputDir;
    }

    private void writeDepartmentFile(Department department,
                                     ArgumentParser options,
                                     Path outputDir) throws IOException {

        Path filePath = outputDir.resolve(department.getName() + ".sb");

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            if (department.getManager() != null) {
                writer.write(department.getManager().getStringForFile());
                writer.newLine();
            }

            List<Employee> sortedEmployees = EmployeeSorter.sort(department.getEmployees(), options.getSortType(), options.getSortOrder());

            for (Employee employee : sortedEmployees) {
                writer.write(employee.getStringForFile());
                writer.newLine();
            }
        }
    }

    private void writeErrorLog(List<String> errors, Path outputDir) throws IOException {
        if (!errors.isEmpty()) {
            Path errorFile = outputDir.resolve("error.log");
            Files.write(errorFile, errors, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
    }
}
