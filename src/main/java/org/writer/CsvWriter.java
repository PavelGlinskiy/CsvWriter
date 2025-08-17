package org.writer;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Универсальный класс для записи объектов в CSV с помощью Reflection и аннотаций
 *
 */
public class CsvWriter<T> implements Writable<T> {

    private static final String DELIMITER = ";";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Записывает список объектов в CSV файл
     */
    @Override
    public void writeToCsv(List<T> data, String filePath) throws IOException {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Data list cannot be null or empty");
        }

        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            Class<?> clazz = data.get(0).getClass();
            Field[] fields = getAnnotatedFields(clazz);

            String header = generateHeader(fields, data);
            writer.write(header + "\n");

            for (T item : data) {
                String row = generateRow(fields, item);
                writer.write(row + "\n");
            }
        } catch (Exception e) {
            throw new IOException("Failed to write CSV file: " + e.getMessage(), e);
        }
    }

    /**
     * Получает поля класса, помеченные аннотацией @CsvColumn
     */
    private Field[] getAnnotatedFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(CsvColumn.class))
                .toArray(Field[]::new);
    }

    /**
     * Генерирует заголовок CSV на основе аннотаций полей
     */
    private String generateHeader(Field[] fields, List<T> data) {
        return Arrays.stream(fields)
                .flatMap(field -> {
                    CsvColumn ann = field.getAnnotation(CsvColumn.class);
                    String baseName = ann.value();

                    if (Collection.class.isAssignableFrom(field.getType())) {
                        int maxSize = getMaxCollectionSize(field, data);
                        if (maxSize <= 0) {
                            return Stream.empty();
                        }
                        return IntStream.range(0, maxSize)
                                .mapToObj(i -> baseName + (i + 1));
                    }else {
                        return Stream.of(baseName);
                    }
                })
                .collect(Collectors.joining(DELIMITER));
    }

    /**
     * Определяет максимальный размер коллекции среди всех объектов
     */
    private int getMaxCollectionSize(Field field, List<T> data) {

        return data.stream()
                .mapToInt(obj -> {
                    if (obj == null) {
                        return 0;
                    }
                    try {
                        field.setAccessible(true);
                        Object value = field.get(obj);
                        if (value instanceof Collection<?> col) {
                            return col.size();
                        }
                    } catch (IllegalAccessException ignored) {

                    }
                    return 0;
                })
                .max()
                .orElse(0);
    }

    /**
     * Генерирует строку данных для одного объекта
     */
    private String generateRow(Field[] fields, T item) {
        return Arrays.stream(fields)
                .flatMap(field -> {
                    try {
                        field.setAccessible(true);
                        Object value = field.get(item);

                        if (value instanceof Collection<?> col) {
                            return col.stream()
                                    .map(this::formatValue);
                        } else {
                            return Stream.of(formatValue(value));
                        }
                    } catch (IllegalAccessException e) {
                        return Stream.of("");
                    }
                })
                .collect(Collectors.joining(DELIMITER));
    }

    /**
     * Форматирует значение для записи в CSV
     */
    private String formatValue(Object value) {
        if (value == null) {
            return "";
        }

        String stringValue;
        if (value instanceof LocalDate) {
            stringValue = ((LocalDate) value).format(DATE_FORMATTER);
        } else if (value instanceof LocalDateTime) {
            stringValue = ((LocalDateTime) value).format(DATETIME_FORMATTER);
        } else if (value instanceof Boolean) {
            stringValue = ((Boolean) value) ? "Yes" : "No";
        } else {
            stringValue = value.toString();
        }

        return escapeCsv(stringValue);
    }

    /**
     * Экранирует значение для CSV формата
     */
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
