package parser;

import model.Department;
import model.Employee;
import model.Manager;
import model.Person;
import util.ConsoleLogger;

import java.util.*;
import java.util.stream.Collectors;

public class LineParser {
    private final Map<Long, Person> idRegistry = new HashMap<>();
    private final Map<String, Department> departments = new TreeMap<>();
    private final Map<Long, Manager> managersById = new HashMap<>();
    private final List<Employee> pendingEmployees = new ArrayList<>();
    private final List<String> errorLines = new ArrayList<>();

    public List<String> getErrorLines() {
        return errorLines;
    }

    public Map<String, Department> getDepartments() {
        return departments;
    }

    public void parseLine(String line) {
        if (line == null || line.isBlank()) {
            ConsoleLogger.warn("Empty string was skipped.");
            return;
        }

        String[] parts = line.trim().split(",");
        if (parts.length != 5) {
            logError("Invalid number of fields", formatLine(parts));
            return;
        }

        String role = parts[0].trim().toLowerCase();
        Long id = parseLong(parts[1]);
        String name = parts[2].trim();
        Double salary = parseDouble(parts[3]);
        String fifthField = parts[4].trim();

        if (id == null || salary == null) {
            logWarn("Missing string due to incorrect data", formatLine(parts));
            return;
        }

        if (idRegistry.containsKey(id)) {
            logWarn("Duplicate ID: " + id + ". String was omitted.", formatLine(parts));
            return;
        }

        switch (role) {
            case "manager" -> handleManager(id, name, salary, fifthField);
            case "employee" -> handleEmployee(id, name, salary, fifthField, parts);
            default -> logError("Unknown role: " + role, formatLine(parts));
        }
    }

    private void handleManager(Long id, String name, Double salary, String departmentName) {
        Manager manager = new Manager(id, name, salary, departmentName);
        registerPerson(id, manager);
        managersById.put(id, manager);
        departments.computeIfAbsent(departmentName, d -> new Department(d, manager));
        ConsoleLogger.info("Added manager: " + name + " (ID: " + id + ")");
    }

    private void handleEmployee(Long id, String name, Double salary, String managerIdStr, String[] parts) {
        Long managerId = parseLong(managerIdStr);
        if (managerId == null) {
            logWarn("Missed employee due to incorrect managerId", formatLine(parts));
            return;
        }

        Employee employee = new Employee(id, name, salary, managerId);
        registerPerson(id, employee);
        pendingEmployees.add(employee);
        ConsoleLogger.info("Employee was added: " + name + " (ID: " + id + ")");
    }

    public void resolveEmployees() {
        for (Employee employee : pendingEmployees) {
            Manager manager = managersById.get(employee.getManagerId());
            if (manager == null) {
                logWarn("Manager was not found for employee ID: " + employee.getId(), employee.getStringForFile());
                continue;
            }

            Department department = departments.get(manager.getDepartment());
            if (department == null) {
                logWarn("Manager ID was not found: " + manager.getId(), employee.getStringForFile());
                continue;
            }

            department.addEmployee(employee);
            ConsoleLogger.info("Employee " + employee.getName() + " was added to the division " + department.getName());
        }
    }

    private void registerPerson(Long id, Person person) {
        idRegistry.put(id, person);
    }

    private Long parseLong(String value) {
        try {
            long parsed = Long.parseLong(value.trim());
            return parsed > 0 ? parsed : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseDouble(String value) {
        try {
            double parsed = Double.parseDouble(value.trim());
            return parsed > 0 ? parsed : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void logWarn(String message, String formatted) {
        ConsoleLogger.warn(message + " -> " + formatted);
        errorLines.add(formatted);
    }

    private void logError(String message, String formatted) {
        ConsoleLogger.error(message + " -> " + formatted);
        errorLines.add(formatted);
    }

    private String formatLine(String[] parts) {
        return Arrays.stream(parts)
                .map(String::trim)
                .collect(Collectors.joining(","));
    }
}
