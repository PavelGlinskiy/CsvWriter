package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.CsvColumn;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Student {

    @CsvColumn("Student Name")
    private String name;

    @CsvColumn("Scores")
    private List<String> score;
}