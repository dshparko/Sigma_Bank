package util;

import model.Employee;
import options.SortOrder;
import options.SortType;

import java.util.Comparator;
import java.util.List;

public class EmployeeSorter {

    public static List<Employee> sort(List<Employee> employees, SortType type, SortOrder order) {
        if (type == null) return employees;

        Comparator<Employee> comparator = switch (type) {
            case NAME -> Comparator.comparing(Employee::getName, String.CASE_INSENSITIVE_ORDER);
            case SALARY -> Comparator.comparing(Employee::getSalary);
        };

        if (order == SortOrder.DESC) {
            comparator = comparator.reversed();
        }

        return employees.stream().sorted(comparator).toList();
    }
}
