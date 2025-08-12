package model;

import java.math.BigDecimal;

public abstract class Person {
    protected final Long id;
    protected final String name;
    protected final double salary;

    public Person(Long id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = BigDecimal.valueOf(salary)
                .stripTrailingZeros()
                .doubleValue();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }
}
