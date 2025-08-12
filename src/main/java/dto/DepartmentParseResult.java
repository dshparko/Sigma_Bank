package dto;

import model.Department;
import java.util.List;
import java.util.Map;

public record DepartmentParseResult(
        Map<String, Department> departments,
        List<String> errorLines) {
}
