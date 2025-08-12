package model;

import java.math.BigDecimal;

public class Manager extends Person implements Exportable {
    private final String department;

    public Manager(Long id, String name, double salary, String department) {
        super(id, name, salary);
        this.department = department != null ? department.trim() : "Unknown";
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String getStringForFile() {
        return String.join(",", "Manager",
                String.valueOf(id),
                name,
                BigDecimal.valueOf(salary)
                        .stripTrailingZeros()
                        .toPlainString());
    }
}
