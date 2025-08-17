package org.writer;

import java.io.IOException;
import java.util.List;

/**
 * Интерфейс для записи объектов в CSV формат
 */
public interface Writable<T> {

    /**
     * Записывает список объектов в CSV файл
     */
    void writeToCsv(List<T> data, String filePath) throws IOException;
}
