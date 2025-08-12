package statistics;

import model.Department;
import options.OutputType;
import parser.ArgumentParser;
import util.ConsoleLogger;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class Statistics {

    private final ArgumentParser options;

    public Statistics(ArgumentParser options) {
        this.options = options;
    }

    public void generate(Map<String, Department> departments) {
        if (!options.isGenerateStat()) return;

        String content = buildStatistics(departments);

        if (options.getOutputType() == OutputType.FILE) {
            writeStatisticsToFile(content);
        } else {
            System.out.println(content);
        }
    }

    private String buildStatistics(Map<String, Department> departments) {
        StringBuilder sb = new StringBuilder("department, min, max, mid").append(System.lineSeparator());

        departments.keySet().stream()
                .sorted()
                .map(departments::get)
                .forEach(dept -> {
                    DepartmentStatistics stats = new DepartmentStatistics(dept);
                    sb.append(formatLine(dept.getName(), stats));
                });

        return sb.toString();
    }

    private String formatLine(String deptName, DepartmentStatistics stats) {
        return String.format("%s, %s, %s, %s%n",
                deptName,
                format(stats.getMinSalary()),
                format(stats.getMaxSalary()),
                format(stats.getAverageSalary()));
    }

    private String format(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.UP)
                .stripTrailingZeros()
                .toPlainString();
    }

    private void writeStatisticsToFile(String content) {
        String path = options.getOutputPath();
        if (path == null || path.isBlank()) {
            ConsoleLogger.error("--path is obligatory when --output=file");
            return;
        }

        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
        } catch (IOException e) {
            ConsoleLogger.error("Error writing statistics to file: " + path);
        }
    }
}
