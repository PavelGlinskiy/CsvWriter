package org.writer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.model.Employee;
import org.writer.model.Months;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit тесты для класса CsvWriter.
 */
class CsvWriterTest {

    @TempDir
    Path tempDir;
    
    private CsvWriter<Person> personWriter;
    private CsvWriter<Student> studentWriter;
    private CsvWriter<Employee> employeeWriter;
    
    @BeforeEach
    void setUp() {
        personWriter = new CsvWriter<>();
        studentWriter = new CsvWriter<>();
        employeeWriter = new CsvWriter<>();
    }

    @Test
    void testWritePersonToCsv() throws IOException {
        List<Person> people = Arrays.asList(
                Person.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dayOfBirth(15)
                        .monthOfBirth(Months.JANUARY)
                        .yearOfBirth(1990)
                        .build(),
                Person.builder()
                        .firstName("Jane")
                        .lastName("Smith")
                        .dayOfBirth(20)
                        .monthOfBirth(Months.FEBRUARY)
                        .yearOfBirth(1985)
                        .build()
        );
        
        Path filePath = tempDir.resolve("test_people.csv");

        personWriter.writeToCsv(people, filePath.toString());

        assertTrue(Files.exists(filePath));
        List<String> lines = Files.readAllLines(filePath);
        assertEquals(3, lines.size());

        assertTrue(lines.get(0).contains("Name"));
        assertTrue(lines.get(0).contains("LastName"));
        assertTrue(lines.get(0).contains("Day of birth"));

        assertTrue(lines.get(1).contains("John"));
        assertTrue(lines.get(1).contains("Doe"));
        assertTrue(lines.get(2).contains("Jane"));
        assertTrue(lines.get(2).contains("Smith"));
    }
    
    @Test
    void testWriteStudentToCsv() throws IOException {
        List<Student> students = Arrays.asList(
                Student.builder()
                        .name("Alice Johnson")
                        .score(Arrays.asList("5", "4", "5"))
                        .build(),
                Student.builder()
                        .name("Bob Wilson")
                        .score(Arrays.asList("3", "4", "3"))
                        .build()
        );
        
        Path filePath = tempDir.resolve("test_students.csv");

        studentWriter.writeToCsv(students, filePath.toString());

        assertTrue(Files.exists(filePath));
        List<String> lines = Files.readAllLines(filePath);
        assertEquals(3, lines.size());

        assertTrue(lines.get(0).contains("Student Name"));
        assertTrue(lines.get(0).contains("Scores1"));
        assertTrue(lines.get(0).contains("Scores2"));
        assertTrue(lines.get(0).contains("Scores3"));

        assertTrue(lines.get(1).contains("Alice Johnson"));
        assertTrue(lines.get(1).contains("5"));
        assertTrue(lines.get(1).contains("4"));
    }
    
    @Test
    void testWriteEmployeeToCsv() throws IOException {
        List<Employee> employees = Arrays.asList(
                Employee.builder()
                        .id(1L)
                        .fullName("John Developer")
                        .email("john@company.com")
                        .department("IT")
                        .position("Developer")
                        .salary(BigDecimal.valueOf(5000.00))
                        .hireDate(LocalDate.of(2020, 1, 15))
                        .skills(Arrays.asList("Java", "Spring", "PostgreSQL"))
                        .projects(Arrays.asList("Project A", "Project B"))
                        .isActive(true)
                        .build()
        );
        
        Path filePath = tempDir.resolve("test_employees.csv");

        employeeWriter.writeToCsv(employees, filePath.toString());

        assertTrue(Files.exists(filePath));
        List<String> lines = Files.readAllLines(filePath);
        assertEquals(2, lines.size());

        assertTrue(lines.get(0).contains("Employee ID"));
        assertTrue(lines.get(0).contains("Full Name"));
        assertTrue(lines.get(0).contains("Skills1"));
        assertTrue(lines.get(0).contains("Skills2"));
        assertTrue(lines.get(0).contains("Skills3"));

        assertTrue(lines.get(1).contains("John Developer"));
        assertTrue(lines.get(1).contains("john@company.com"));
        assertTrue(lines.get(1).contains("IT"));
        assertTrue(lines.get(1).contains("Yes"));
    }
    
    @Test
    void testWriteEmptyListThrowsException() {
        List<Person> emptyList = Collections.emptyList();
        Path filePath = tempDir.resolve("empty.csv");

        assertThrows(IllegalArgumentException.class, () -> {
            personWriter.writeToCsv(emptyList, filePath.toString());
        });
    }
    
    @Test
    void testWriteNullListThrowsException() {
        Path filePath = tempDir.resolve("null.csv");

        assertThrows(IllegalArgumentException.class, () -> {
            personWriter.writeToCsv(null, filePath.toString());
        });
    }
    
    @Test
    void testWriteWithNullFilePathThrowsException() {
        List<Person> people = Arrays.asList(
                Person.builder()
                        .firstName("Test")
                        .lastName("User")
                        .dayOfBirth(1)
                        .monthOfBirth(Months.JANUARY)
                        .yearOfBirth(2000)
                        .build()
        );

        assertThrows(IllegalArgumentException.class, () -> {
            personWriter.writeToCsv(people, null);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            personWriter.writeToCsv(people, "");
        });
    }
    
    @Test
    void testCsvEscaping() throws IOException {
        List<Person> people = Arrays.asList(
                Person.builder()
                        .firstName("John \"The Rock\"")
                        .lastName("Doe;Smith")
                        .dayOfBirth(15)
                        .monthOfBirth(Months.JANUARY)
                        .yearOfBirth(1990)
                        .build()
        );
        
        Path filePath = tempDir.resolve("escaping_test.csv");

        personWriter.writeToCsv(people, filePath.toString());

        List<String> lines = Files.readAllLines(filePath);
        String dataLine = lines.get(1);

        assertTrue(dataLine.contains("\"John \"\"The Rock\"\"\""));
        assertTrue(dataLine.contains("\"Doe;Smith\""));
    }
    
    @Test
    void testCollectionWithDifferentSizes() throws IOException {
        List<Student> students = Arrays.asList(
                Student.builder()
                        .name("Student 1")
                        .score(Arrays.asList("5", "4"))
                        .build(),
                Student.builder()
                        .name("Student 2")
                        .score(Arrays.asList("3", "4", "5", "4"))
                        .build()
        );
        
        Path filePath = tempDir.resolve("different_sizes.csv");

        studentWriter.writeToCsv(students, filePath.toString());

        List<String> lines = Files.readAllLines(filePath);
        assertEquals(3, lines.size());

        String header = lines.get(0);
        assertTrue(header.contains("Scores1"));
        assertTrue(header.contains("Scores2"));
        assertTrue(header.contains("Scores3"));
        assertTrue(header.contains("Scores4"));

        String firstStudentLine = lines.get(1);
        assertTrue(firstStudentLine.contains("Student 1"));
        assertTrue(firstStudentLine.contains("5"));
        assertTrue(firstStudentLine.contains("4"));

        String secondStudentLine = lines.get(2);
        assertTrue(secondStudentLine.contains("Student 2"));
        assertTrue(secondStudentLine.contains("3"));
        assertTrue(secondStudentLine.contains("4"));
        assertTrue(secondStudentLine.contains("5"));
    }
}
