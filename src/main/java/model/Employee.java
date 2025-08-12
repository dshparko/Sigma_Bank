package model;

import java.math.BigDecimal;

public class Employee extends Person implements Exportable {
    private final Long managerId;

    public Employee(Long id, String name, double salary, Long managerId) {
        super(id, name, salary);
        this.managerId = managerId;
    }

    public Long getManagerId() {
        return managerId;
    }

    @Override
    public String getStringForFile() {
        return String.join(",", "Employee",
                String.valueOf(id),
                name,
                BigDecimal.valueOf(salary)
                        .stripTrailingZeros()
                        .toPlainString(),
                String.valueOf(managerId));
    }
}
