package com.pazukdev.converters;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

import java.time.LocalDate;

/**
 LocalDateToDaysConverter.java,
 convert foundation LocalDate date to Long UNIX Epoch days
 */

@SuppressWarnings("serial")
public class LocalDateToDaysConverter implements Converter<LocalDate, Long> {

    @Override
    public Result<Long> convertToModel(LocalDate value, ValueContext context) {
        if (value == null) {
            Result.ok(LocalDate.now().minusDays(1L).toEpochDay());
        }
        Long days = value.toEpochDay();
        return Result.ok(days);
    }

    @Override
    public LocalDate convertToPresentation(Long value, ValueContext context) {
        if (value == null) {
            return LocalDate.now().minusDays(1L);
        }
        return LocalDate.ofEpochDay(value);
    }

}
