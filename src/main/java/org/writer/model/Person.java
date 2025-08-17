package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.CsvColumn;

@Data
@Builder
@AllArgsConstructor
public class Person {

    @CsvColumn("Name")
    private String firstName;

    @CsvColumn("LastName")
    private String lastName;

    @CsvColumn("Day of birth")
    private int dayOfBirth;

    @CsvColumn("Month of birth")
    private Months monthOfBirth;

    @CsvColumn("Year of birth")
    private int yearOfBirth;
}

