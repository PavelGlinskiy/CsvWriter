package org.writer;

import com.github.javafaker.Faker;
import org.writer.model.Employee;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Главный класс приложения для демонстрации возможностей CSV Writer
 * Создает тестовые данные и сохраняет их в CSV файлы:
 *   people.csv - список людей с датами рождения
 *   students.csv - список студентов с оценками
 *   employees.csv - список сотрудников с различными типами данных
 *
 */
public class Main {
    
    public static void main(String[] args) throws IOException {
        Faker faker = new Faker();
        
        System.out.println("Starting CSV generation...");
        
        // Генерация данных о людях
        generatePeopleData(faker);
        
        // Генерация данных о студентах
        generateStudentsData(faker);
        
        // Генерация данных о сотрудниках
        generateEmployeesData(faker);
        
        System.out.println("All CSV files have been successfully created!");
        System.out.println("Generated files:");
        System.out.println(" - people.csv");
        System.out.println(" - students.csv");
        System.out.println(" - employees.csv");
    }
    
    /**
     * Генерирует данные о людях и сохраняет в CSV
     */
    private static void generatePeopleData(Faker faker) throws IOException {
        System.out.println(" Generating people data...");
        
        List<Person> people = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            people.add(Person.builder()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .dayOfBirth(faker.number().numberBetween(1, 28))
                    .monthOfBirth(Months.values()[faker.number().numberBetween(0, Months.values().length)])
                    .yearOfBirth(faker.number().numberBetween(1970, 2005))
                    .build());
        }
        
        new CsvWriter<Person>().writeToCsv(people, "people.csv");
        System.out.println(" people.csv created with " + people.size() + " records");
    }
    
    /**
     * Генерирует данные о студентах и сохраняет в CSV
     */
    private static void generateStudentsData(Faker faker) throws IOException {
        System.out.println("Generating students data...");
        
        List<Student> students = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            students.add(Student.builder()
                    .name(faker.name().fullName())
                    .score(Arrays.asList(
                            String.valueOf(faker.number().numberBetween(2, 5)),
                            String.valueOf(faker.number().numberBetween(2, 5)),
                            String.valueOf(faker.number().numberBetween(2, 5))
                    ))
                    .build());
        }
        
        new CsvWriter<Student>().writeToCsv(students, "students.csv");
        System.out.println("students.csv created with " + students.size() + " records");
    }
    
    /**
     * Генерирует данные о сотрудниках и сохраняет в CSV
     */
    private static void generateEmployeesData(Faker faker) throws IOException {
        System.out.println("Generating employees data...");
        
        List<Employee> employees = new ArrayList<>();
        String[] departments = {"IT", "HR", "Finance", "Marketing", "Sales"};
        String[] positions = {"Developer", "Manager", "Analyst", "Designer", "Consultant"};
        String[] skills = {"Java", "Python", "JavaScript", "SQL", "Spring", "React", "Angular", "Docker", "Kubernetes"};
        String[] technologies = {"PostgreSQL", "MySQL", "MongoDB", "Redis", "Elasticsearch"};
        
        for (int i = 1; i <= 4; i++) {
            employees.add(Employee.builder()
                    .id((long) i)
                    .fullName(faker.name().fullName())
                    .email(faker.internet().emailAddress())
                    .department(departments[faker.number().numberBetween(0, departments.length)])
                    .position(positions[faker.number().numberBetween(0, positions.length)])
                    .salary(BigDecimal.valueOf(faker.number().randomDouble(2, 3000, 15000)))
                    .hireDate(LocalDate.now().minusDays(faker.number().numberBetween(1, 1000)))
                    .skills(Arrays.asList(
                            skills[faker.number().numberBetween(0, skills.length)],
                            technologies[faker.number().numberBetween(0, technologies.length)],
                            skills[faker.number().numberBetween(0, skills.length)]
                    ))
                    .projects(Arrays.asList(
                            faker.company().name() + " Project",
                            faker.company().name() + " Initiative"
                    ))
                    .isActive(faker.random().nextBoolean())
                    .build());
        }
        
        new CsvWriter<Employee>().writeToCsv(employees, "employees.csv");
        System.out.println(" employees.csv created with " + employees.size() + " records");
    }
}

