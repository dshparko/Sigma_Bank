package parser;

import options.OutputType;
import options.SortOrder;
import options.SortType;
import util.ConsoleLogger;

public class ArgumentParser {
    private SortType sortType;
    private SortOrder sortOrder;
    private boolean generateStat = false;
    private OutputType outputType = OutputType.CONSOLE;
    private String outputPath;

    public ArgumentParser(String[] args) {
        if (args != null) {
            parse(args);
            validateArguments();
        }
    }

    public SortType getSortType() {
        return sortType;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public boolean isGenerateStat() {
        return generateStat;
    }

    public OutputType getOutputType() {
        return outputType;
    }

    public String getOutputPath() {
        return outputPath;
    }

    private void parse(String[] args) {
        for (String arg : args) {
            String normalized = arg.trim().toLowerCase();

            if (normalized.startsWith("--sort=") || normalized.startsWith("-s=")) {
                sortType = parseEnum(arg, SortType.class, "sort type");
            } else if (normalized.startsWith("--order=")) {
                sortOrder = parseEnum(arg, SortOrder.class, "order type");
            } else if (normalized.equals("--stat")) {
                generateStat = true;
            } else if (normalized.startsWith("--output=") || normalized.startsWith("-o=")) {
                outputType = parseEnum(arg, OutputType.class, "output type");
            } else if (normalized.startsWith("--path=")) {
                parsePath(arg);
            } else {
                ConsoleLogger.warn("The unknown parameter: " + arg);
            }
        }
    }

    private <T extends Enum<T>> T parseEnum(String arg, Class<T> enumType, String label) {
        String[] parts = arg.split("=", 2);
        if (parts.length < 2 || parts[1].isBlank()) {
            ConsoleLogger.error("Parameter " + label + " requires meaning.");
            return null;
        }

        try {
            return Enum.valueOf(enumType, parts[1].trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            ConsoleLogger.error("Incorrect value for " + label + ": " + parts[1].trim());
            return null;
        }
    }

    private void parsePath(String arg) {
        if (outputType != OutputType.FILE) {
            ConsoleLogger.error("--path ignored because --output is not set to file.");
            return;
        }

        String[] parts = arg.split("=", 2);
        if (parts.length < 2 || parts[1].isBlank()) {
            ConsoleLogger.warn("--path requires a non-empty path. The output will be redirected to the console.");
            outputType = OutputType.CONSOLE;
            return;
        }

        outputPath = parts[1].trim();
    }

    private void validateArguments() {
        if (sortOrder != null && sortType == null) {
            ConsoleLogger.warn("--order is specified without --sort. The sort order will be ignored.");
            sortOrder = null;
        }

        if (outputType == OutputType.FILE && (outputPath == null || outputPath.isBlank())) {
            ConsoleLogger.error("--output=file requires --path. Output will be redirected to console.");
            outputType = OutputType.CONSOLE;
        }
    }

}
