package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Department {
    private final String name;
    private final Manager manager;
    private final List<Employee> employees = new ArrayList<>();

    public Department(String name, Manager manager) {
        this.name = name != null ? name.trim() : "Unnamed";
        this.manager = manager;
    }

    public void addEmployee(Employee employee) {
        if (employee != null) {
            employees.add(employee);
        }
    }

    public String getName() {
        return name;
    }

    public Manager getManager() {
        return manager;
    }

    public List<Employee> getEmployees() {
        return Collections.unmodifiableList(employees);
    }
}
