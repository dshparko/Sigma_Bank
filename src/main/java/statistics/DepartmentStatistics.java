package statistics;

import model.Department;
import model.Employee;

import java.util.DoubleSummaryStatistics;

public class DepartmentStatistics {
    private final DoubleSummaryStatistics stats;

    public DepartmentStatistics(Department department) {
        this.stats = department.getEmployees().stream()
                .map(Employee::getSalary)
                .filter(s -> s > 0)
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();
    }

    public double getMinSalary() {
        return stats.getCount() > 0 ? stats.getMin() : 0;
    }

    public double getMaxSalary() {
        return stats.getCount() > 0 ? stats.getMax() : 0;
    }

    public double getAverageSalary() {
        return stats.getCount() > 0 ? stats.getAverage() : 0;
    }
}
