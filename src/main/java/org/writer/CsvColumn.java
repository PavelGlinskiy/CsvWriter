package org.writer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для указания названия колонки в CSV файле
 * Эта аннотация используется для пометки полей класса, которые должны
 * быть включены в CSV файл. Значение аннотации определяет заголовок колонки
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvColumn {
    String value();
}

