package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.CsvColumn;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Employee {

    @CsvColumn("Employee ID")
    private Long id;

    @CsvColumn("Full Name")
    private String fullName;

    @CsvColumn("Email")
    private String email;

    @CsvColumn("Department")
    private String department;

    @CsvColumn("Position")
    private String position;

    @CsvColumn("Salary")
    private BigDecimal salary;

    @CsvColumn("Hire Date")
    private LocalDate hireDate;

    @CsvColumn("Skills")
    private List<String> skills;

    @CsvColumn("Projects")
    private List<String> projects;

    @CsvColumn("Is Active")
    private boolean isActive;
}